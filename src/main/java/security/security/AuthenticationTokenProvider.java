package security.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import security.domain.MyUser;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationTokenProvider {

    @Value("${security.jwt.key-value}")
    String jwtKey;

    Key key;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
    }


    public String findToken(final String headerValue) {
        String bearerName = "Bearer ";
        if (headerValue.startsWith(bearerName)) {
            return headerValue.substring(bearerName.length());
        }
        return null;
    }

    public String generateJwtToken(final MyUser myUser) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", myUser.getId());
        payload.put("username", myUser.getUsername());
//        payload.put("route", authUser.getUsername().getRoute().getId());
//        payload.put("role", authUser.getRoles().stream().map(Role::getAuthority).collect(Collectors.toList()));

        return generateJwtToken(payload);
    }


    public String generateJwtToken(Map<String, Object> payload) {
        final long now = System.currentTimeMillis();
        final long expiration = now + 1000 * 60 * 24 * 7;
        final String jwtId = UUID.randomUUID().toString();

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        return Jwts.builder()
                .setId(jwtId)
                .setAudience("my_security")
                .setHeader(headers)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expiration))
                .addClaims(payload)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(final String accessToken) {
        try {
            Claims claims = (Claims) (Jwts.parserBuilder().setSigningKey(key).build().parse(accessToken).getBody());
//            if (claims.get("auth") == null) {
//                throw new RuntimeException("NO_AUTH");
//            }
            Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString()
                    .split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

            UserDetails principal = new User(claims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(principal, "", authorities);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    public String getUsername(final String jwtString) {
        Map<String, Object> payload = getPayload(jwtString);
        return (String) payload.get("username");
    }

    public Map<String, Object> getPayload(final String jwtString) {
        try {
            Claims claims = (Claims) (Jwts.parserBuilder().setSigningKey(key).build().parse(jwtString).getBody());
            Map<String, Object> payload = new HashMap<>();
            for (String key : claims.keySet()) {
                payload.put(key, claims.get(key));
            }
            return payload;
        } catch (JwtException e) {
            log.error(e.toString());
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    public boolean validToken(String token) {
        return true;
    }

}
