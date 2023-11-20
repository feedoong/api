package io.feedoong.api.global.security.jwt;

import io.feedoong.api.global.security.AuthenticatedPathMatcher;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final AuthenticatedPathMatcher authenticatedPathMatcher;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (authenticatedPathMatcher.matches(request)) {
            String authorization = request.getHeader(AUTHORIZATION_HEADER);
            JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(AuthorityUtils.NO_AUTHORITIES, authorization);
            Authentication authenticate = jwtAuthenticationManager.authenticate(jwtAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        }

        filterChain.doFilter(request, response);
    }
}
