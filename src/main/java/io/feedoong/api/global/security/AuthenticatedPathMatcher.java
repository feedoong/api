package io.feedoong.api.global.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticatedPathMatcher implements RequestMatcher {

    private final OrRequestMatcher orRequestMatcher;

    public AuthenticatedPathMatcher() {
        String[] jwtProtectedPaths = new String[]{
                "/v2/subscriptions"
        };

        List<RequestMatcher> matchers = Arrays.stream(jwtProtectedPaths)
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList());

        this.orRequestMatcher = new OrRequestMatcher(matchers);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return orRequestMatcher.matches(request);
    }
}
