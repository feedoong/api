package io.feedoong.api.global.config;

import io.feedoong.api.global.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenProviderConfig {

    @Value("${jwt.secret}")
    private static String SECRET_KEY;

    @Bean
    public TokenProvider tokenProvider() {
        return new TokenProvider(SECRET_KEY);
    }
}
