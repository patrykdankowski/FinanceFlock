package com.patrykdankowski.financeflock.user.port;

import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface UserCommandServicePort {
    UserDomainEntity findUserByEmail(String email);

    void checkIfUserExists(String userEmail);

    UserDomainEntity saveUser(UserDomainEntity user);

    List<UserDomainEntity> saveAllUsers(List<UserDomainEntity> users);

    List<UserDomainEntity> listOfUsersFromIds(List<Long> userIds);

    void updateLastLoggedInAt(LocalDateTime lastLoggedInAt, String userEmail);


}
