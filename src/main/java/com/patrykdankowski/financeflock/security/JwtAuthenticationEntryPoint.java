package com.patrykdankowski.financeflock.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrykdankowski.financeflock.dto.ErrorDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtAuthenticationEntryPoint
        implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "Unauthorized",
                authException.getMessage(),
                HttpStatus.UNAUTHORIZED
        );
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonErrorDetails = objectMapper.writeValueAsString(errorDetails);
        response.getWriter().write(jsonErrorDetails);

    }
}
