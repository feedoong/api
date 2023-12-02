package io.feedoong.api.global.security.jwt;

import io.feedoong.api.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String bearerToken = (String) authentication.getCredentials();
        String jwtToken = tokenProvider.parseBearerToken(bearerToken);
        tokenProvider.validate(jwtToken);

        String email = tokenProvider.getSubject(jwtToken);
        try {
            return getUsernamePasswordAuthenticationToken(email);
        } catch (CustomException e) {
            throw new BadCredentialsException("유효하지 않은 이메일입니다. email=" + email);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}
