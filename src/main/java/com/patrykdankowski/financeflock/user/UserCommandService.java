package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;

import java.util.List;
import java.util.Set;

public interface UserCommandService {
    User findUserByEmail(String email);

    void checkIfUserExists(String userEmail);

    void saveUser(User user);

    void saveAllUsers(List<User> users);

    List<User> listOfUsersFromIds(List<Long> userIds);
}
