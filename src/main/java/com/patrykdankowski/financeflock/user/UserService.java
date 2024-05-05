package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;

import java.util.List;
import java.util.Set;

public interface UserService {
    User findUserByEmail(String email);

    void checkIfUserExists(String userEmail);

    void saveUser(User user);

    void saveAllUsers(List<User> users);

    Set<UserDtoProjections> findAllUsersByShareDataTrueInSameBudgetGroup(Long budgetGroupId);
}
