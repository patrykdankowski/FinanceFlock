package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.auth.dto.JwtAuthenticationResponse;
import com.patrykdankowski.financeflock.auth.dto.LoginDto;
import com.patrykdankowski.financeflock.auth.dto.RegisterDto;
import com.patrykdankowski.financeflock.auth.port.AuthenticationFacadePort;
import com.patrykdankowski.financeflock.auth.port.JwtTokenProviderPort;
import com.patrykdankowski.financeflock.user.model.record.UserLoginVO;
import com.patrykdankowski.financeflock.user.model.record.UserRegisterVO;
import com.patrykdankowski.financeflock.user.port.UserCommandServicePort;
import com.patrykdankowski.financeflock.user.port.UserFactoryPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Driver;

@Service
@Slf4j
class AuthenticationFacadeAdapter implements AuthenticationFacadePort {

    AuthenticationFacadeAdapter(final AuthenticationManager authenticationManager,
                                final JwtTokenProviderPort jwtTokenProvider,
                                final UserCommandServicePort userCommandService,
                                final UserFactoryPort userFactory) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userCommandService = userCommandService;
        this.userFactory = userFactory;
    }

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProviderPort jwtTokenProvider;
    private final UserCommandServicePort userCommandService;
    private final UserFactoryPort userFactory;

    @Override
    public JwtAuthenticationResponse login(LoginDto loginDto) {

        UserLoginVO userLoginVO = new UserLoginVO(loginDto.getEmail(),
                loginDto.getPassword());
        log.info("1");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userLoginVO.email(), userLoginVO.password()));
        log.info("2");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("3");

//        updateLastLoginAsync(authentication);
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        final String token = jwtTokenProvider.generateJwtToken(authentication);
        response.setToken(token);

        return response;
    }

    @Override
    public String register(RegisterDto registerDto) {
        userCommandService.checkIfUserExists(registerDto.getEmail());

        UserRegisterVO userRegisterVO = new UserRegisterVO(registerDto.getName(),
                registerDto.getEmail(),
                registerDto.getPassword());

        var user = userFactory.createUserFromVO(userRegisterVO);
        userCommandService.saveUser(user);
        return "Registered";
    }


    //    @Async = protected
    @Override
    public void updateLastLoginAsync(Authentication authentication) {
        //TODO duplikacja kodu + zastanowić się czy warto asynchroniecznie
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user found");
        }
        String userMail = authentication.getName();
        var user = userCommandService.findUserByEmail(userMail);
        user.login();
        userCommandService.saveUser(user);
    }

}
