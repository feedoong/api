package io.feedoong.api.global.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.feedoong.api.global.exception.ErrorCode;
import io.feedoong.api.global.exception.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class BadCredentialsExceptionFilter extends OncePerRequestFilter {

    private static final String RESPONSE_CONTENT_TYPE = "application/json; charset=UTF-8";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BadCredentialsException e) {
            log.warn(e.getMessage());
            setErrorResponse(e, response);
        }
    }

    private void setErrorResponse(Exception e, HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.UNAUTHORIZED);
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(RESPONSE_CONTENT_TYPE);
        response.getWriter().write(jsonResponse);
    }
}
