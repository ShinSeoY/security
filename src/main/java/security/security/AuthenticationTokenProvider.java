package security.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import security.domain.MyUser;
import security.domain.RefreshToken;
import security.dto.Payload;
import security.dto.TokenInfo;
import security.exception.error.InvalidTokenException;
import security.exception.error.UncaughtException;
import security.repository.RefreshTokenRepository;
import security.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationTokenProvider {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Value("${security.jwt.key-value}")
    String jwtKey;

    Key key;

    private final long THIRTY_MINUTES = 1000 * 60 * 30;
    private final long ONE_WEEK = 1000 * 60 * 60 * 24 * 7;

    private final String GRANT_TYPE = "Bearer ";


    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
    }


//    public String findToken(final String headerValue) {
//        if (headerValue.startsWith(GRANT_TYPE)) {
//            return headerValue.substring(GRANT_TYPE.length());
//        }
//        return null;
//    }

    public TokenInfo generateJwtToken(MyUser myUser, HttpServletResponse httpServletResponse) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", myUser.getUsername());
//        payload.put("route", authUser.getUsername().getRoute().getId());
//        payload.put("role", authUser.getRoles().stream().map(Role::getAuthority).collect(Collectors.toList()));
        TokenInfo tokenInfo = generateJwtToken(payload);

        httpServletResponse.addCookie(setCookie("ATK", tokenInfo.getAccessToken()));
        httpServletResponse.addCookie(setCookie("RTK", tokenInfo.getRefreshToken()));

        return tokenInfo;
    }

    private Cookie setCookie(String name, String token) {
        Cookie cookie = new Cookie(name, token);
        cookie.setHttpOnly(true);
        return cookie;
    }


    public TokenInfo generateJwtToken(Map<String, Object> payload) {
        final long now = System.currentTimeMillis();

        String accessToken = generateAccessToken(now, payload);
        String refreshToken = generateRefreshToken(now, (String) payload.get("username"), payload);

        return TokenInfo.builder()
                .grantType(GRANT_TYPE.trim())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String generateAccessToken(long now, Map<String, Object> payload) {
        try {
            final String jwtId = UUID.randomUUID().toString();
            final long accessTokenExpiration = now + THIRTY_MINUTES;

            Map<String, Object> headers = new HashMap<>();
            headers.put("typ", "JWT");
            headers.put("alg", "HS256");

            payload.put("type", "ATK");

            String accessToken = Jwts.builder()
                    .setId(jwtId)
                    .setAudience("my_security")
                    .setHeader(headers)
                    .setIssuedAt(new Date(now))
                    .setExpiration(new Date(accessTokenExpiration))
                    .setSubject(objectMapper.writeValueAsString(payload))
                    .addClaims(payload)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
            return accessToken;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new UncaughtException();
        }
    }

    private String generateRefreshToken(long now, String username, Map<String, Object> payload) {
        final long refreshTokenExpiration = now + ONE_WEEK;

        payload.put("type", "RTK");

        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(refreshTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // refresh token redis 저장
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .refreshToken(refreshToken)
                        .username(username)
                        .build()
        );
        return refreshToken;
    }

//    public Authentication getAuthentication(final String accessToken) {
//        try {
//            Claims claims = (Claims) (Jwts.parserBuilder().setSigningKey(key).build().parse(accessToken).getBody());
//
//            Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString()
//                    .split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//
//            UserDetails principal = new User(claims.getSubject(), "", authorities);
//
//            return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//        } catch (Exception e) {
//            log.error(e.toString());
//        }
//        return null;
//    }

    public String getUsername(Cookie[] cookies, HttpServletResponse response) {
        Payload payload = getPayload(cookies, response);
        return payload.getUsername();
    }

    private Payload getPayload(Cookie[] cookies, HttpServletResponse response) {
        String accessToken = getToken(cookies, "ATK");
        String refreshToken = getToken(cookies, "RTK");

        if (accessToken == null) {
            RefreshToken refresh = refreshTokenRepository.findById(refreshToken).orElseThrow(() -> new InvalidTokenException());
            if (refresh != null) {
                MyUser myUser = userRepository.findMyUserByUsername(refresh.getUsername()).orElseThrow();
                TokenInfo tokenInfo = generateJwtToken(myUser, response);
                return getPayload(tokenInfo.getAccessToken());
            } else {
                log.error("Expired token");
                throw new InvalidTokenException();
            }
        } else {
            return getPayload(accessToken);
        }
    }

//    private Claims getClaims(Cookie[] cookies, String name){
//        String token = getToken(cookies, name);
//        return (Claims) (Jwts.parserBuilder().setSigningKey(key).build().parse(token).getBody());
//    }

    private String getToken(Cookie[] cookies, String name) {
        try {
            String token = Arrays.stream(cookies)
                    .filter(it -> name.equals(it.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElseThrow(() -> new NullPointerException("cookie not found or its value is null"));

            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
            return token;
            // 토큰 유효성 확인
        } catch (ExpiredJwtException e) {
            if ("ATK".equals(name)) {
                return null;
            } else {
                log.error("Expired token.");
                throw new InvalidTokenException();
            }
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT signature.");
            throw new InvalidTokenException();
        }
    }

//    private boolean isNonExpired(Date date) {
//        return date != null && date.after(new Date());
//    }

    public Payload getPayload(String atk) {
        try {
//        Claims claims = (Claims) (Jwts.parserBuilder().setSigningKey(key).build().parse(atk).getBody());
            String subjectStr = Jwts.parser().setSigningKey(key).parseClaimsJws(atk).getBody().getSubject();
            return objectMapper.readValue(subjectStr, Payload.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new UncaughtException();
        }
    }


}
