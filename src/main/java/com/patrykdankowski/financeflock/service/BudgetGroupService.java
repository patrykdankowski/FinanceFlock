package com.patrykdankowski.financeflock.service;

import com.patrykdankowski.financeflock.dto.BudgetGroupDto;
import com.patrykdankowski.financeflock.dto.EmailDto;
import com.patrykdankowski.financeflock.dto.UserDto;
import com.patrykdankowski.financeflock.entity.BudgetGroup;
import com.patrykdankowski.financeflock.entity.Role;
import com.patrykdankowski.financeflock.entity.User;
import com.patrykdankowski.financeflock.exception.UserNotFoundException;
import com.patrykdankowski.financeflock.repository.BudgetGroupRepository;
import com.patrykdankowski.financeflock.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.patrykdankowski.financeflock.constants.AppConstants.MAX_BUDGET_GROUP_SIZE;
import static com.patrykdankowski.financeflock.entity.Role.USER;

@Service
@RequiredArgsConstructor
public class BudgetGroupService {
    private final BudgetGroupRepository budgetGroupRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createBudgetGroup(BudgetGroupDto budgetGroupDto) {
        Authentication authentication = getAuthentication();

        String ownerEmail = authentication.getName();
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new UserNotFoundException(ownerEmail));
        owner.setRole(Role.GROUP_ADMIN);
        BudgetGroup budgetGroup = BudgetGroup.create(owner, budgetGroupDto);
        owner.setBudgetGroup(budgetGroup);
        budgetGroupRepository.save(budgetGroup);


    }

    @Transactional
    public void closeBudgetGroup() {
        Authentication authentication = getAuthentication();
        String ownerEmail = authentication.getName();
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new UserNotFoundException(ownerEmail));
        BudgetGroup budgetGroup = owner.getBudgetGroup();
        if (budgetGroup == null) {
            throw new IllegalStateException("There is no group to close");
        }
        if (!budgetGroup.getOwner().equals(owner)) {
            throw new IllegalStateException("Only the group owner can close the group");
        }
        //TODO zaimplementować metode informujaca all userów z grupy ze grupa została zamknięta
        List<User> listOfUsers = budgetGroup.getListOfMembers().stream().map(
                user ->
                {
                    user.setRole(USER);
                    user.setBudgetGroup(null);
                    return user;
                }).toList();
        userRepository.saveAll(listOfUsers);
        budgetGroupRepository.delete(budgetGroup);
    }

    @Transactional
    public void addUserToGroup(String email) {
        Authentication authentication = getAuthentication();

        String ownerEmail = authentication.getName();
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new UserNotFoundException(ownerEmail));

        BudgetGroup budgetGroup = owner.getBudgetGroup();
        if (budgetGroup == null) {
            throw new IllegalStateException(owner.getName() + " does not have a group");
        }
        User userToAdd = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));


        if (budgetGroup.getListOfMembers().contains(userToAdd)) {
            throw new IllegalStateException("User is already a member of the group");
        }
        if (budgetGroup.getListOfMembers().size() >= MAX_BUDGET_GROUP_SIZE) {
            throw new IllegalStateException("Budget group size is full, remove someone first");
        }
        budgetGroup.getListOfMembers().add(userToAdd);
        userToAdd.setRole(Role.GROUP_MEMBER);
        userToAdd.setBudgetGroup(budgetGroup);
        budgetGroupRepository.save(budgetGroup);


    }

    @Transactional
    public void removeUserFromGroup(String email) {
        Authentication authentication = getAuthentication();

        String ownerEmail = authentication.getName();
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new UserNotFoundException(ownerEmail));

        BudgetGroup budgetGroup = owner.getBudgetGroup();
        if (budgetGroup == null) {
            throw new IllegalStateException(owner.getName() + " is not a member of a group");
        }
        User userToRemove = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        if (!(budgetGroup.getListOfMembers().contains(userToRemove))) {
            throw new IllegalStateException("User is not a member of the group");
        }
        budgetGroup.getListOfMembers().remove(userToRemove);
        userToRemove.setRole(USER);
        userToRemove.setBudgetGroup(null);
        budgetGroupRepository.save(budgetGroup);

    }

    public List<UserDto> listOfUsersInGroup() {
        Authentication authentication = getAuthentication();
        String loggedUserEmail = authentication.getName();
        User userLoggedIn = userRepository.findByEmail(loggedUserEmail)
                .orElseThrow(() -> new UserNotFoundException(loggedUserEmail));
        BudgetGroup budgetGroup = userLoggedIn.getBudgetGroup();
        if (budgetGroup == null) {
            throw new IllegalStateException(userLoggedIn.getName() + " is not a member of a group");
        }
        return budgetGroupRepository.findById(budgetGroup.getId()).map(
                group -> group.getListOfMembers().stream().map(
                        user -> new UserDto(user)
                ).collect(Collectors.toList())).orElseThrow(
        );
    }


    private static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user found");
        }
        return authentication;
    }
}
