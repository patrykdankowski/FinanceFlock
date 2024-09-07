package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.user.exception.UserAlreadyExistsException;
import com.patrykdankowski.financeflock.user.exception.UserNotFoundException;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserCommandRepositoryPort;
import com.patrykdankowski.financeflock.user.port.UserCommandServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.patrykdankowski.financeflock.common.AppConstants.VALID_EMAIL_MESSAGE;

@Service
@Slf4j
public class UserCommandServiceAdapter implements UserCommandServicePort {
    private final UserCommandRepositoryPort userCommandRepository;

    UserCommandServiceAdapter(final UserCommandRepositoryPort userCommandRepository) {
        this.userCommandRepository = userCommandRepository;


    }

    @Override
    public UserDomainEntity findUserByEmail(String email) {
        return userCommandRepository.findByEmail(email).orElseThrow(
                () -> {
                    log.warn("User with email {} not found", email);
                    return new UserNotFoundException(email);
                }
        );
    }

    @Override
    public UserDomainEntity findUserById(final Long id) {
        return userCommandRepository.findById(id)
                .orElseThrow(
                        () -> {
                            log.warn("User with id {} not found", id);
                            return new UserNotFoundException(id);
                        });
    }

    @Override
    public void checkIfUserExists(String userEmail) {
        if (userCommandRepository.existsUserByEmail(userEmail)) {
            log.warn("User with email {} already exists", userEmail);
            throw new UserAlreadyExistsException(userEmail, VALID_EMAIL_MESSAGE);

        }

    }

    @Override
    public UserDomainEntity saveUser(UserDomainEntity user) {
        return userCommandRepository.save(user);
    }

    @Override
    public List<UserDomainEntity> saveAllUsers(List<UserDomainEntity> users) {
        return userCommandRepository.saveAll(users);
    }


    @Override
    public List<UserDomainEntity> listOfUsersFromIds(final List<Long> userIds) {
        return userCommandRepository.findAllByIdIn(userIds);
    }


}
