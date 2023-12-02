package io.feedoong.api.global.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class JwtAuthenticationManager implements AuthenticationManager {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ProviderManager providerManager = new ProviderManager(jwtAuthenticationProvider);
        return providerManager.authenticate(authentication);
    }
}
