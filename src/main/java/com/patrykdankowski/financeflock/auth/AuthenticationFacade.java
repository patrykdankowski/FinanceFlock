package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.user.RegisterDtoRequest;
import com.patrykdankowski.financeflock.user.User;
import com.patrykdankowski.financeflock.user.UserFactory;
import com.patrykdankowski.financeflock.user.UserService;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
class AuthenticationFacade {

    AuthenticationFacade(final AuthenticationManager authenticationManager, final JwtTokenProvider jwtTokenProvider, final UserService userService, final UserFactory userFactory) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.userFactory = userFactory;
    }

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserFactory userFactory;


    String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        updateLastLoginAsync(authentication);

        return jwtTokenProvider.generateJwtToken(authentication);
    }

    String register(RegisterDtoRequest registerDtoRequest) {
        userService.checkIfUserExists(registerDtoRequest.getEmail());
        var user = userFactory.createUserFromRegisterRequest(registerDtoRequest);
        userService.saveUser(user);
        return "Registered";
    }


    //    @Async
    protected void updateLastLoginAsync(Authentication authentication) {
        //TODO duplikacja kodu + zastanowić się czy warto asynchroniecznie
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user found");
        }
        String userMail = authentication.getName();
        var user = userService.findUserByEmail(userMail);
        User userToSave = user.toBuilder()
                .lastLoggedInAt(LocalDateTime.now())
                .build();
        userService.saveUser(userToSave);
    }

}
