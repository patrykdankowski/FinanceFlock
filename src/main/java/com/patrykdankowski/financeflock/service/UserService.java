package com.patrykdankowski.financeflock.service;

import com.patrykdankowski.financeflock.dto.SubUserDto;
import com.patrykdankowski.financeflock.exception.SubUserNotBelongToMainUserException;
import com.patrykdankowski.financeflock.dto.SubUserReadModel;
import com.patrykdankowski.financeflock.entity.Role;
import com.patrykdankowski.financeflock.entity.User;
import com.patrykdankowski.financeflock.exception.EmailAlreadyExistsException;
import com.patrykdankowski.financeflock.exception.MaxSubUsersCountException;
import com.patrykdankowski.financeflock.exception.UserNotFoundException;
import com.patrykdankowski.financeflock.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.patrykdankowski.financeflock.constants.AppConstants.MAX_SUB_USERS;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SubUserReadModel createSubUser(SubUserDto subUserDto) {
        Authentication authentication = getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user found");
        }

        if (userRepository.existsUserByEmail(subUserDto.getEmail())) {
            throw new EmailAlreadyExistsException(subUserDto.getEmail());
        }

        String mainUserMail = authentication.getName();
        User loggedUser = userRepository.findByEmail(mainUserMail)
                .orElseThrow(() -> new UserNotFoundException(mainUserMail));
        if (!(loggedUser.getSubUsers().size() < MAX_SUB_USERS)) {
            throw new MaxSubUsersCountException();
        }

        User userToSave = User.builder()
                .name(subUserDto.getName())
                .password(passwordEncoder.encode(subUserDto.getPassword()))
                .email(subUserDto.getEmail())
                .role(Role.USER)
                .mainUser(loggedUser)
                .build();


        User userSaved = userRepository.save(userToSave);
        loggedUser.getSubUsers().add(userSaved);
        SubUserReadModel savedUserEmail = new SubUserReadModel();
        savedUserEmail.setEmail(userSaved.getEmail());
        return savedUserEmail;

    }

    @Transactional
    public void removeSubUser(Long id) {
        log.info("Trying to remove");
        User subUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        Authentication authentication = getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user found");
        }
        String mainUserMail = authentication.getName();
        User loggedUser = userRepository.findByEmail(mainUserMail)
                .orElseThrow(() -> new UserNotFoundException(mainUserMail));
        if (subUser.getMainUser() == null || !(subUser.getMainUser().equals(loggedUser))) {

            throw new SubUserNotBelongToMainUserException(subUser.getId());
        }

        subUser.setMainUser(null);
        loggedUser.getSubUsers().remove(subUser);
        log.info("removed");
//        userRepository.save(loggedUser);

    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
