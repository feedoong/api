package io.feedoong.api.global.util;

import io.feedoong.api.global.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthenticationHeaderHandler {
    private final TokenProvider tokenProvider;

    private final String AUTHORIZATION_HEADER = "Authorization";

    public HttpHeaders createAuthorizationHeader(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, createAuthHeaderValue(accessToken));

        return httpHeaders;
    }

    private String createAuthHeaderValue(String accessToken) {
        return tokenProvider.getBearerPrefix() + accessToken;
    }
}
