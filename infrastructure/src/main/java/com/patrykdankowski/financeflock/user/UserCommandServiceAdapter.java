package com.patrykdankowski.financeflock.user;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.patrykdankowski.financeflock.AppConstants.VALID_EMAIL_MESSAGE;

@Service
public class UserCommandServiceAdapter implements UserCommandServicePort {
    private final UserCommandRepository userCommandRepository;

    UserCommandServiceAdapter(final UserCommandRepository userCommandRepository) {
        this.userCommandRepository = userCommandRepository;
    }

    @Override
    public User findUserByEmail(String email) {
        return userCommandRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email));
    }

    @Override
    public void checkIfUserExists(String userEmail) {
        if (userCommandRepository.existsUserByEmail(userEmail)) {
            throw new UserAlreadyExistsException(userEmail, VALID_EMAIL_MESSAGE);

        }

    }

    @Override
    public void saveUser(User user) {
        userCommandRepository.save(user);
    }

    @Override
    public void saveAllUsers(List<User> users) {
        userCommandRepository.saveAll(users);
    }


    @Override
    public List<User> listOfUsersFromIds(final List<Long> userIds) {
        return userCommandRepository.findAllById(userIds);
    }

}
