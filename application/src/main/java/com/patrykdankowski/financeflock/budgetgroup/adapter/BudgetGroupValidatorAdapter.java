package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.common.AppConstants;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupValidationException;
import com.patrykdankowski.financeflock.budgetgroup.exception.MaxUserCountInBudgetGroupException;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupValidatorPort;
import com.patrykdankowski.financeflock.user.port.UserValidatorPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class BudgetGroupValidatorAdapter implements BudgetGroupValidatorPort {
    private final UserValidatorPort userValidator;

    BudgetGroupValidatorAdapter(final UserValidatorPort userValidator) {
        this.userValidator = userValidator;
    }


    @Override
    public boolean belongsToSameBudgetGroup(final UserDomainEntity loggedUser,
                                            final UserDomainEntity otherUser) {
        if (loggedUser.getBudgetGroupId() == null) {
            return false;
        }
        checkUserIdGroupWithGivenId(otherUser.getBudgetGroupId(), loggedUser.getBudgetGroupId());

        return true;
    }

    @Override
    public void validateGroupForPotentialOwner(final UserDomainEntity loggedUser,
                                               final Long groupId,
                                               final BudgetGroupDomainEntity budgetGroupDomainEntity) {
        validateGroup(loggedUser, groupId, budgetGroupDomainEntity);

    }

    @Override
    public void isMemberOfGivenGroup(final UserDomainEntity loggedUser,
                                     final BudgetGroupDomainEntity budgetGroupDomainEntity,
                                     final Long groupId) {
        validateGroup(loggedUser, groupId, budgetGroupDomainEntity);

    }

    private void validateGroup(final UserDomainEntity loggedUser,
                               final Long groupId,
                               final BudgetGroupDomainEntity budgetGroupDomainEntity) {
        checkUserIdGroupWithGivenId(groupId, loggedUser.getBudgetGroupId());
        checkIfUserIsMemberOfGroup(loggedUser, budgetGroupDomainEntity);
    }

    public boolean isMember(final UserDomainEntity userToRemove,
                            final BudgetGroupDomainEntity budgetGroupDomainEntity,
                            final Long groupId) {
        return budgetGroupDomainEntity.getListOfMembersId().contains(userToRemove.getId()) &&
                userToRemove.getBudgetGroupId().equals(groupId);

    }


    private void checkIfUserIsMemberOfGroup(final UserDomainEntity user,
                                            final BudgetGroupDomainEntity group) {


        if (!group.getListOfMembersId().contains(user.getId()) ||
                !group.getOwnerId().equals(user.getId())) {
            log.warn("User {} is not a  a member of this group id: {}", user.getName(), group.getId());
            throw new BudgetGroupValidationException("User is not a member of budget group");
        }
    }

    private void checkUserIdGroupWithGivenId(final Long givenIdGroup,
                                             final Long idFromUserObject) {
        if (!idFromUserObject.equals(givenIdGroup)) {
            log.warn("Given id group {} is not the same as your group", givenIdGroup);
            throw new BudgetGroupValidationException("U are not a member of given id group");
        }

    }

    @Override
    public void validateSizeOfGroup(final BudgetGroupDomainEntity budgetGroup) {
        if (budgetGroup.getListOfMembersId().size() >= AppConstants.MAX_BUDGET_GROUP_SIZE) {
            log.warn("Budget group reached full size '{}'", AppConstants.MAX_BUDGET_GROUP_SIZE);
            throw new MaxUserCountInBudgetGroupException();
        }
    }

    @Override
    public boolean isMemberOfGivenGroup(final UserDomainEntity user, final BudgetGroupDomainEntity budgetGroup) {
        boolean hasRole = userValidator.hasGivenRole(user, Role.GROUP_MEMBER);
        boolean groupIsNullNotNull = !userValidator.groupIsNull(user);
        boolean isMemberOfGroup = isMember(user, budgetGroup, user.getBudgetGroupId());
        return hasRole && groupIsNullNotNull && isMemberOfGroup;
    }

    @Override
    public boolean isNotMemberOfAnyGroup(final UserDomainEntity loggedUser) {
        boolean hasRole = userValidator.hasGivenRole(loggedUser, Role.USER);
        boolean GroupIsNull = userValidator.groupIsNull(loggedUser);
        return hasRole && GroupIsNull;
    }

    @Override
    public void validateIfUserIsAdmin(final UserDomainEntity loggedUser, final Long id, final BudgetGroupDomainEntity budgetGroup) {
        validateGroupForPotentialOwner(loggedUser, id, budgetGroup);
        userValidator.hasGivenRole(loggedUser, Role.GROUP_ADMIN);
    }
}
