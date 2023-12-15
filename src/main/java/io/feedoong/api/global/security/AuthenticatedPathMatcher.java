package io.feedoong.api.global.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AuthenticatedPathMatcher implements RequestMatcher {

    private final OrRequestMatcher orRequestMatcher;

    public AuthenticatedPathMatcher() {
        String[] jwtProtectedPaths = new String[]{
                "/v2/subscriptions",
                "/v2/items",
                "/v2/items/liked"
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
