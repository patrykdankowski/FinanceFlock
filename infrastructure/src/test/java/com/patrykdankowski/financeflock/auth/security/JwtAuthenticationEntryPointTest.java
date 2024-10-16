package com.patrykdankowski.financeflock.auth.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrykdankowski.financeflock.common.ErrorDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class JwtAuthenticationEntryPointTest {

    @InjectMocks
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @Mock
    private PrintWriter printWriter;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void whenCommence_thenReturnUnauthorizedResponse() throws IOException, ServletException {
        // given
        String exceptionMessage = "Access denied";
        when(authException.getMessage()).thenReturn(exceptionMessage);

        StringWriter responseWriterCapture = new StringWriter();
        PrintWriter responsePrintWriter = new PrintWriter(responseWriterCapture);
        when(response.getWriter()).thenReturn(responsePrintWriter);

        // when
        jwtAuthenticationEntryPoint.commence(request, response, authException);
        responsePrintWriter.flush();

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorDetails expectedErrorDetails = new ErrorDetails(
                new Date(),
                "Unauthorized",
                exceptionMessage,
                HttpStatus.UNAUTHORIZED
        );
        String expectedJson = objectMapper.writeValueAsString(expectedErrorDetails);

        assertThat(responseWriterCapture.toString()).isEqualToIgnoringWhitespace(expectedJson);
    }

    @Test
    void whenGetWriterThrowsIOException_thenHandleException() throws IOException, ServletException {
        // given
        String exceptionMessage = "Access denied";
        when(authException.getMessage()).thenReturn(exceptionMessage);
        when(response.getWriter()).thenThrow(new IOException("Failed to get writer"));

        // when & then
        assertThatThrownBy(() -> jwtAuthenticationEntryPoint.commence(request, response, authException))
                .isInstanceOf(IOException.class)
                .hasMessage("Failed to get writer");

        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
