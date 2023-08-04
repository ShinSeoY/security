package security.security;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationTokenProvider {

    @Value("${security.jwt.key-value}")
    String jwtKey;

    Key key;

    @PostConstruct
    protected void init(){
        this.key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
    }



}
