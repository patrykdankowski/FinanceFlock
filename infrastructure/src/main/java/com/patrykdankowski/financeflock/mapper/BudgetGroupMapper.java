package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.user.UserNotFoundException;
import com.patrykdankowski.financeflock.user.UserSqlEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface BudgetGroupMapper {

    BudgetGroupMapper INSTANCE = Mappers.getMapper(BudgetGroupMapper.class);

    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "listOfMembersId", ignore = true)
    BudgetGroupDomainEntity toDomainEntity(BudgetGroupSqlEntity budgetGroupSqlEntity);

    @AfterMapping
    default void handleAfterMapping(BudgetGroupSqlEntity source,
                                    @MappingTarget BudgetGroupDomainEntity target) {
        var ownerId = source.getOwner().getId();
        target.setOwnerId(ownerId);

        Set<Long> membersId = source.getListOfMembers().stream()
                .map(member -> {
                    var memberId = member.getId();
                    return memberId;
                }).collect(Collectors.toSet());
        target.setListOfMembersId(membersId);
    }

    @Mapping(target = "listOfMembers", ignore = true)
    @Mapping(target = "owner", ignore = true)
    BudgetGroupSqlEntity toSqlEntity(BudgetGroupDomainEntity budgetGroupDomainEntity);

    @AfterMapping
    default void handleAfterMapping(BudgetGroupDomainEntity source,
                                    @MappingTarget BudgetGroupSqlEntity target,
                                    @Autowired UserMapperRepository userRepository) {
        if (source.getId() != null) {
            var ownerId = source.getOwnerId();
            UserSqlEntity userSqlEntity = userRepository.findById(ownerId)
                    .orElseThrow(() -> new UserNotFoundException(ownerId));
            target.setOwner(userSqlEntity);


        }
        final Set<Long> listOfMembersId = source.getListOfMembersId();
        Set<UserSqlEntity> setOfUsers = userRepository.findAllById(listOfMembersId).stream().collect(Collectors.toSet());
        target.setListOfMembers(setOfUsers);

    }


//    @AfterMapping
//    default void handleBudgetGroupMembers(BudgetGroupDomainEntity budgetGroupDomainEntity,
//                                          @MappingTarget BudgetGroupSqlEntity budgetGroupSqlEntity) {
//
//        if (budgetGroupDomainEntity.getListOfMembersId() != null) {
//
//            Set<UserSqlEntity> members = budgetGroupDomainEntity.getListOfMembersId().stream()
//                    .map(user -> {
//                        UserSqlEntity userSql = UserMapper.INSTANCE.toSqlEntity(user);
//                        return userSql;
//                    }).collect(Collectors.toSet());
//            budgetGroupSqlEntity.setListOfMembers(members);
//        }
//
//    }
}

//    @AfterMapping
//    default void handleBudgetGroupMembers(BudgetGroupSqlEntity budgetGroupSqlEntity, @MappingTarget BudgetGroupDomainEntity budgetGroupDomainEntity) {
//
//        if (budgetGroupSqlEntity.getListOfMembers() != null) {
//            Set<UserDomainEntity> members = budgetGroupSqlEntity.getListOfMembers().stream()
//                    .map(user -> {
//                        UserDomainEntity userDomain = UserMapper.INSTANCE.toDomainEntity(user);
//                        return userDomain;
//                    }).collect(Collectors.toSet());
//
//            budgetGroupDomainEntity.setListOfMembers(members);
//
//        }
//
//    }
////
//    @AfterMapping
//    default void handleOwnerOfBudgetGroup(BudgetGroupDomainEntity budgetGroupDomainEntity, @MappingTarget BudgetGroupSqlEntity budgetGroupSqlEntity) {
//
//        UserSqlEntity owner = UserMapper.INSTANCE.toSqlEntity(budgetGroupDomainEntity.getOwnerId());
//        budgetGroupSqlEntity.setOwner(owner);
//
//    }
//
//    @AfterMapping
//    default void handleOwnerOfBudgetGroup(BudgetGroupSqlEntity budgetGroupSql, @MappingTarget BudgetGroupDomainEntity budgetGroupDomainEntity) {
//
//        UserDomainEntity owner = UserMapper.INSTANCE.toDomainEntity(budgetGroupSql.getOwner());
//        budgetGroupDomainEntity.setOwner(owner);
//
//    }




