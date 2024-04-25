package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.exception.ResourceAlreadyExists;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.patrykdankowski.financeflock.common.AppConstants.VALID_EMAIL_MESSAGE;

@Service
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email));
    }

    @Override
    public void checkIfUserExists(String userEmail) {
        if (userRepository.existsUserByEmail(userEmail)) {
            throw new ResourceAlreadyExists(userEmail, VALID_EMAIL_MESSAGE);

        }

    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void saveAllUsers(List<User> users) {
        userRepository.saveAll(users);
    }

    @Override
    public Set<UserDtoProjections> findAllUsersByShareDataTrueInSameBudgetGroup(Long budgetGroupId) {
        return userRepository.findAllByShareDataIsTrueAndBudgetGroup_Id(budgetGroupId);
    }

}
