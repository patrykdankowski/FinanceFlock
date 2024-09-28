package com.patrykdankowski.financeflock.auth.security;

import com.patrykdankowski.financeflock.auth.port.JwtTokenManagementPort;
import com.patrykdankowski.financeflock.auth.port.TokeProviderFromRequestPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenManagementPort jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final TokeProviderFromRequestPort tokeProviderFromRequest;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = tokeProviderFromRequest.getTokenFromRequest(request);
            if (StringUtils.hasText(token) && jwtTokenProvider.validateJwtToken(token)) {
                String usernameFromJwtToken = jwtTokenProvider.getUsernameFromJwtToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromJwtToken);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }

    }
}
