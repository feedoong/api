package io.feedoong.api.global.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.feedoong.api.global.exception.ErrorCode;
import io.feedoong.api.global.exception.ErrorResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private static final String RESPONSE_CONTENT_TYPE = "application/json; charset=UTF-8";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            log.warn(e.getMessage());
            setErrorResponse(e, response);
        }
    }

    private void setErrorResponse(JwtException e, HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.UNAUTHORIZED);
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(RESPONSE_CONTENT_TYPE);
        response.getWriter().write(jsonResponse);
    }
}
