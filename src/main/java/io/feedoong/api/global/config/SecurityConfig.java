package io.feedoong.api.global.config;

import io.feedoong.api.global.security.AuthenticatedPathMatcher;
import io.feedoong.api.global.security.exception.BadCredentialsExceptionFilter;
import io.feedoong.api.global.security.exception.GlobalExceptionFilter;
import io.feedoong.api.global.security.exception.JwtExceptionFilter;
import io.feedoong.api.global.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticatedPathMatcher authenticatedPathMatcher;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final BadCredentialsExceptionFilter badCredentialsExceptionFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final GlobalExceptionFilter globalExceptionFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((httpRequest) ->
                        httpRequest
                                .requestMatchers(authenticatedPathMatcher).authenticated()
                                .anyRequest().permitAll())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(badCredentialsExceptionFilter, JwtAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, BadCredentialsExceptionFilter.class)
                .addFilterBefore(globalExceptionFilter, JwtExceptionFilter.class)
                .exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(authenticationEntryPoint))
                .build();
    }
}
