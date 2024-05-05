package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.exception.ResourceAlreadyExists;
import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.patrykdankowski.financeflock.common.AppConstants.VALID_EMAIL_MESSAGE;

@Service
class UserServiceImpl implements UserService {
    private final UserQueryRepository userQueryRepository;
    private final UserCommandRepository userCommandRepository;

    UserServiceImpl(final UserQueryRepository userQueryRepository, final UserCommandRepository userCommandRepository) {
        this.userQueryRepository = userQueryRepository;
        this.userCommandRepository = userCommandRepository;
    }

    @Override
    public User findUserByEmail(String email) {
        return userCommandRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email));
    }

    @Override
    public void checkIfUserExists(String userEmail) {
        if (userQueryRepository.existsUserByEmail(userEmail)) {
            throw new ResourceAlreadyExists(userEmail, VALID_EMAIL_MESSAGE);

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
    public Set<UserDtoProjections> findAllUsersByShareDataTrueInSameBudgetGroup(Long budgetGroupId) {
        return userQueryRepository.findAllByShareDataIsTrueAndBudgetGroup_Id(budgetGroupId);
    }

}
