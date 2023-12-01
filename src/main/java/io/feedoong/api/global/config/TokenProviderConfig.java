package io.feedoong.api.global.config;

import io.feedoong.api.global.security.jwt.TokenProvider;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class TokenProviderConfig {

    @Value("${jwt.secret}")
    private String SECRET_STRING;

    @Bean
    public TokenProvider tokenProvider() {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
        return new TokenProvider(key);
    }
}
