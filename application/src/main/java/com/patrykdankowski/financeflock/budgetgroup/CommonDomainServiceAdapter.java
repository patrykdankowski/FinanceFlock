    package com.patrykdankowski.financeflock.budgetgroup;

    import com.patrykdankowski.financeflock.Logger;
    import com.patrykdankowski.financeflock.common.Role;
    import com.patrykdankowski.financeflock.user.UserDomainEntity;
    import lombok.extern.slf4j.Slf4j;

    @Slf4j
    public class CommonDomainServiceAdapter implements CommonDomainServicePort {

        private final org.slf4j.Logger logger = Logger.getLogger(this.getClass());


        @Override
        public void checkIfGroupExists(final UserDomainEntity userFromContext,
                                       final Long givenIdGroup) {
    //        Long groupToValidateId = userFromContext.getBudgetGroupId();
    //        if (userFromContext.getBudgetGroupId() == null) {
    //            logger.warn("User {} is not a member of any group", userFromContext.getName());
    //            throw new IllegalStateException("User does not belong to any budget group");
    //        }
    //        if (!groupToValidateId.equals(givenIdGroup)) {
    //            logger.warn("Given id group {} is not the same as your group", givenIdGroup);
    //            throw new IllegalStateException("U are not a member of given id group");
    //    }
    }

    @Override
    public Long checkIfGroupExistsOld(final UserDomainEntity userFromContext) {
        Long potentialGroupId = userFromContext.getBudgetGroupId();
        if (potentialGroupId == null) {
            logger.warn("Budget group is null");
            throw new IllegalStateException("Budget group does not exist");
        }
        return potentialGroupId;
    }

    @Override
    public void checkIdGroupWithGivenId(final Long givenIdGroup,
                                        final Long idFromUserObject) {
        if (!idFromUserObject.equals(givenIdGroup)) {
            logger.warn("Given id group {} is not the same as your group", givenIdGroup);
            throw new IllegalStateException("U are not a member of given id group");
        }


    }

    @Override
    public void checkIfGroupIsNotNull(final UserDomainEntity user) {
        if (user.getBudgetGroupId() == null) {
            logger.warn("User {} is not a member of any group", user.getName());
            throw new IllegalStateException("User does not belong to any budget group");
        }
    }

    @Override
    public void checkRoleForUser(final UserDomainEntity user,
                                 final Role role) {
        if (user.getRole() != role) {
            //TODO customowy exception
            logger.warn("User {} has wrong role ", user.getName());
            throw new IllegalStateException("Bad role");
        }
    }

    @Override
    public boolean checkIfUserIsMemberOfGroup(final UserDomainEntity user,
                                              final BudgetGroupDomainEntity group) {
        return (user.getBudgetGroupId().equals((group.getId()))) ||
                (group.getListOfMembersId().contains(user.getId()));
    }
    }
