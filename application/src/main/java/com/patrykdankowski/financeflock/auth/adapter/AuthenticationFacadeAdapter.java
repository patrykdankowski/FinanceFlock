package com.patrykdankowski.financeflock.auth.adapter;

import com.patrykdankowski.financeflock.auth.dto.JwtAuthenticationResponse;
import com.patrykdankowski.financeflock.auth.dto.LoginDto;
import com.patrykdankowski.financeflock.auth.dto.RegisterDto;
import com.patrykdankowski.financeflock.auth.port.AuthenticationFacadePort;
import com.patrykdankowski.financeflock.auth.port.JwtTokenManagementPort;
import com.patrykdankowski.financeflock.auth.port.TokeProviderFromRequestPort;
import com.patrykdankowski.financeflock.auth.port.TokenCommandServicePort;
import com.patrykdankowski.financeflock.user.model.record.UserLoginVO;
import com.patrykdankowski.financeflock.user.model.record.UserRegisterVO;
import com.patrykdankowski.financeflock.user.port.UserCommandServicePort;
import com.patrykdankowski.financeflock.user.port.UserFactoryPort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
class AuthenticationFacadeAdapter implements AuthenticationFacadePort {

    AuthenticationFacadeAdapter(final AuthenticationManager authenticationManager,
                                final JwtTokenManagementPort jwtTokenManagement,
                                final UserCommandServicePort userCommandService,
                                final UserFactoryPort userFactory, final TokeProviderFromRequestPort tokeProviderFromRequest, final TokenCommandServicePort tokenCommandService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManagement = jwtTokenManagement;
        this.userCommandService = userCommandService;
        this.userFactory = userFactory;
        this.tokeProviderFromRequest = tokeProviderFromRequest;
        this.tokenCommandService = tokenCommandService;
    }

    private final AuthenticationManager authenticationManager;
    private final JwtTokenManagementPort jwtTokenManagement;
    private final UserCommandServicePort userCommandService;
    private final UserFactoryPort userFactory;
    private final TokeProviderFromRequestPort tokeProviderFromRequest;
    private final TokenCommandServicePort tokenCommandService;

    @Override
    @Transactional
    public JwtAuthenticationResponse login(LoginDto loginDto) {

        UserLoginVO userLoginVO = new UserLoginVO(loginDto.getEmail(),
                loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userLoginVO.email(), userLoginVO.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        updateLastLoginAsync(LocalDateTime.now(), userLoginVO.email());
        final String token = jwtTokenManagement.generateJwtToken(authentication);
        JwtAuthenticationResponse response = new JwtAuthenticationResponse(token);
        tokenCommandService.saveToken(token, userLoginVO.email());

        return response;
    }

    @Override
    @Transactional
    public String register(RegisterDto registerDto) {
        userCommandService.checkIfUserExists(registerDto.getEmail());

        UserRegisterVO userRegisterVO = new UserRegisterVO(registerDto.getName(),
                registerDto.getEmail(),
                registerDto.getPassword());

        var user = userFactory.createUserFromVO(userRegisterVO);
        userCommandService.saveUser(user);


        return "Registered";
    }

    public void logout(HttpServletRequest request) {
        final String tokenFromRequest = tokeProviderFromRequest.getTokenFromRequest(request);
        jwtTokenManagement.deactivateToken(tokenFromRequest);

    }


    @Async
    void updateLastLoginAsync(LocalDateTime now, String email) {
        try {
            userCommandService.updateLastLoggedInAt(now, email);
        } catch (Exception e) {
            log.error("Error updating last login time for user {}", email);
        }

    }
}
