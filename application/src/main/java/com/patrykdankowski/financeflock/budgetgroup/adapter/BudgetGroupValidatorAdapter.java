package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.common.AppConstants;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupValidationException;
import com.patrykdankowski.financeflock.budgetgroup.exception.MaxUserCountInBudgetGroupException;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupValidatorPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class BudgetGroupValidatorAdapter implements BudgetGroupValidatorPort {


    @Override
    public boolean belongsToSameBudgetGroup(final UserDomainEntity loggedUser,
                                            final UserDomainEntity otherUser) {
        if (loggedUser.getBudgetGroupId() == null) {
            return false;
        }
        checkIdGroupWithGivenId(otherUser.getBudgetGroupId(), loggedUser.getBudgetGroupId());

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
        checkIdGroupWithGivenId(groupId, loggedUser.getBudgetGroupId());
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


        if (!user.getBudgetGroupId().equals((group.getId())) ||
                !group.getListOfMembersId().contains(user.getId())) {
            log.warn("User {} is not a  a member of this group id: {}", user.getName(), group.getId());
            throw new BudgetGroupValidationException("User is not a member of budget group ");
        }
    }

    private void checkIdGroupWithGivenId(final Long givenIdGroup,
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
}
