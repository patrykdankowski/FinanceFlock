package com.patrykdankowski.financeflock.auth.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class JwtAccessDeniedTest {

    @InjectMocks
    private JwtAccessDenied jwtAccessDenied;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AccessDeniedException accessDeniedException;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenAccessDenied_thenSendForbiddenError() throws IOException, ServletException {
        // given
        String exceptionMessage = "Access is denied";
        when(accessDeniedException.getMessage()).thenReturn(exceptionMessage);

        // when
        jwtAccessDenied.handle(request, response, accessDeniedException);

        // then
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, exceptionMessage);
    }

    @Test
    void whenSendErrorThrowsIOException_thenHandleIOException() throws IOException, ServletException {
        // given
        String exceptionMessage = "Access is denied";
        when(accessDeniedException.getMessage()).thenReturn(exceptionMessage);
        doThrow(new IOException("IOException occurred")).when(response).sendError(HttpServletResponse.SC_FORBIDDEN, exceptionMessage);

        // when & then
        assertThatThrownBy(() -> jwtAccessDenied.handle(request, response, accessDeniedException))
                .isInstanceOf(IOException.class)
                .hasMessage("IOException occurred");

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, exceptionMessage);
    }

    @Test
    void whenAccessDeniedWithDifferentMessage_thenSendForbiddenErrorWithCorrectMessage() throws IOException, ServletException {
        // given
        String exceptionMessage = "Custom access denied message";
        when(accessDeniedException.getMessage()).thenReturn(exceptionMessage);

        // when
        jwtAccessDenied.handle(request, response, accessDeniedException);

        // then
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, exceptionMessage);
    }


}