package com.patrykdankowski.financeflock.user;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.patrykdankowski.financeflock.AppConstants.VALID_EMAIL_MESSAGE;

@Service
public class UserCommandServiceAdapter implements UserCommandServicePort {
    private final UserCommandRepositoryPort userCommandRepository;

    UserCommandServiceAdapter(final UserCommandRepositoryPort userCommandRepository) {
        this.userCommandRepository = userCommandRepository;
    }

    @Override
    public UserDomainEntity findUserByEmail(String email) {
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
    public void saveUser(UserDomainEntity user) {
        userCommandRepository.save(user);
    }

    @Override
    public void saveAllUsers(List<UserDomainEntity> users) {
        userCommandRepository.saveAll(users);
    }


    @Override
    public List<UserDomainEntity> listOfUsersFromIds(final List<Long> userIds) {
        return userCommandRepository.findAllById(userIds);
    }

}
