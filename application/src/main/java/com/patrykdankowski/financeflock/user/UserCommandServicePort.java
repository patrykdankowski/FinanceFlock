package com.patrykdankowski.financeflock.user;

import java.util.List;

public interface UserCommandServicePort {
   User findUserByEmail(String email);

    void checkIfUserExists(String userEmail);

    void saveUser(User user);

    void saveAllUsers(List<User> users);

    List<User> listOfUsersFromIds(List<Long> userIds);
}
