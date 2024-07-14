package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.auth.dto.LoginDto;
import com.patrykdankowski.financeflock.auth.dto.RegisterDtoRequest;
import com.patrykdankowski.financeflock.auth.port.AuthenticationFacade;
import com.patrykdankowski.financeflock.auth.port.JwtTokenProviderPort;
import com.patrykdankowski.financeflock.user.port.UserCommandServicePort;
import com.patrykdankowski.financeflock.user.port.UserFactoryPort;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    AuthenticationFacadeImpl(final AuthenticationManager authenticationManager,
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
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        updateLastLoginAsync(authentication);

        return jwtTokenProvider.generateJwtToken(authentication);
    }

    @Override
    public String register(RegisterDtoRequest registerDtoRequest) {
        userCommandService.checkIfUserExists(registerDtoRequest.getEmail());
        var user = userFactory.createUserFromRegisterRequest(registerDtoRequest);
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
//        UserDomainEntity userToSave = user.toBuilder()
//                .lastLoggedInAt(LocalDateTime.now())
//                .build();
        user.login();
        userCommandService.saveUser(user);
    }

}
