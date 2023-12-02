package io.feedoong.api.global.config;

import io.feedoong.api.global.security.AuthenticatedPathMatcher;
import io.feedoong.api.global.security.CustomAuthenticationEntryPoint;
import io.feedoong.api.global.security.exception.BadCredentialsExceptionFilter;
import io.feedoong.api.global.security.exception.GlobalExceptionFilter;
import io.feedoong.api.global.security.exception.JwtExceptionFilter;
import io.feedoong.api.global.security.jwt.JwtAuthenticationFilter;
import io.feedoong.api.global.security.jwt.JwtAuthenticationManager;
import io.feedoong.api.global.security.jwt.JwtAuthenticationProvider;
import io.feedoong.api.global.security.jwt.TokenProvider;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Value("${jwt.secret}")
    private String SECRET_STRING;

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((httpRequest) ->
                        httpRequest
                                .requestMatchers(authenticatedPathMatcher()).authenticated()
                                .anyRequest().permitAll())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(badCredentialsExceptionFilter(), JwtAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter(), BadCredentialsExceptionFilter.class)
                .addFilterBefore(globalExceptionFilter(), JwtExceptionFilter.class)
                .exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(authenticationEntryPoint()))
                .build();
    }

    @Bean
    public TokenProvider tokenProvider() {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
        return new TokenProvider(key);
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(tokenProvider(), userDetailsService);
    }

    @Bean
    public JwtAuthenticationManager jwtAuthenticationManager() {
        return new JwtAuthenticationManager(jwtAuthenticationProvider());
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtAuthenticationManager());
    }

    @Bean
    public BadCredentialsExceptionFilter badCredentialsExceptionFilter() {
        return new BadCredentialsExceptionFilter();
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        return new JwtExceptionFilter();
    }

    @Bean
    public GlobalExceptionFilter globalExceptionFilter() {
        return new GlobalExceptionFilter();
    }

    @Bean
    public AuthenticatedPathMatcher authenticatedPathMatcher() {
        return new AuthenticatedPathMatcher();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }
}
