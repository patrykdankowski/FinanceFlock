package com.patrykdankowski.financeflock.user;

import java.util.List;

public interface UserCommandServicePort {
   UserDomainEntity findUserByEmail(String email);

   UserDomainEntity findUserById(Long id);

    void checkIfUserExists(String userEmail);

    void saveUser(UserDomainEntity user);

    void saveAllUsers(List<UserDomainEntity> users);

    List<UserDomainEntity> listOfUsersFromIds(List<Long> userIds);
}
