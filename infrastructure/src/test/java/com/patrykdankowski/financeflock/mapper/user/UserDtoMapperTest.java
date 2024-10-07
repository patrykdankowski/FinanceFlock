package com.patrykdankowski.financeflock.mapper.user;


import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.mapper.UserDtoMapper;
import com.patrykdankowski.financeflock.user.dto.SimpleUserDomainEntity;
import com.patrykdankowski.financeflock.user.dto.UserDetailsDto;
import com.patrykdankowski.financeflock.user.dto.UserDto;
import com.patrykdankowski.financeflock.user.dto.UserLightDto;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoMapperTest {

    private UserDtoMapper userDtoMapper;

    @BeforeEach
    void setUp() {
        userDtoMapper = new UserDtoMapper();
    }


    @Test
    void toSimpleUserDomainEntity_shouldMapUserSqlEntityToSimpleUserDomainEntity() {
        Long userId = 1L;
        UserSqlEntity userSqlEntity = new UserSqlEntity();
        userSqlEntity.setId(userId);
        userSqlEntity.setName("John Doe");
        userSqlEntity.setEmail("john@example.com");
        userSqlEntity.setRole(Role.USER);
        userSqlEntity.setLastLoggedInAt(LocalDateTime.now().minusDays(1));
        userSqlEntity.setShareData(true);
        userSqlEntity.setCreatedAt(LocalDateTime.now().minusYears(1));

        SimpleUserDomainEntity result = userDtoMapper.toSimpleUserDomainEntity(userSqlEntity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        assertThat(result.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void toSimpleUserDomainEntity_shouldReturnNull_whenUserSqlEntityIsNull() {
        SimpleUserDomainEntity result = userDtoMapper.toSimpleUserDomainEntity(null);

        assertThat(result).isNull();
    }


    @Test
    void toUserDetailsDto_shouldMapUserSqlEntityToUserDetailsDto() {
        UserSqlEntity userSqlEntity = new UserSqlEntity();
        userSqlEntity.setEmail("john@example.com");
        userSqlEntity.setPassword("password123");
        userSqlEntity.setRole(Role.USER);

        UserDetailsDto result = userDtoMapper.toUserDetailsDto(userSqlEntity);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("john@example.com");
        assertThat(result.password()).isEqualTo("password123");
        assertThat(result.role()).isEqualTo(Role.USER.toString());
    }

    @Test
    void toUserDetailsDto_shouldReturnNull_whenUserSqlEntityIsNull() {
        UserDetailsDto result = userDtoMapper.toUserDetailsDto(null);

        assertThat(result).isNull();
    }


    @Test
    void toUserDtos_shouldMapResultsToUserDtoList() {
        List<Object[]> results = Arrays.asList(
                new Object[]{1L, "John Doe", 10L, "Groceries", new BigDecimal("50.00"), "Store A", new BigDecimal("100.00")},
                new Object[]{1L, "John Doe", 11L, "Gas", new BigDecimal("30.00"), "Station B", new BigDecimal("100.00")},
                new Object[]{2L, "Jane Smith", 12L, "Books", new BigDecimal("20.00"), "Bookstore", new BigDecimal("20.00")}
        );

        List<UserDto> result = userDtoMapper.toUserDtos(results);

        assertThat(result).hasSize(2);
        UserDto john = result.get(0);
        assertThat(john.getName()).isEqualTo("John Doe");
        assertThat(john.getExpenses()).hasSize(2);
        assertThat(john.getTotalExpensesForUser()).isEqualTo(new BigDecimal("100.00"));

        UserDto jane = result.get(1);
        assertThat(jane.getName()).isEqualTo("Jane Smith");
        assertThat(jane.getExpenses()).hasSize(1);
        assertThat(jane.getTotalExpensesForUser()).isEqualTo(new BigDecimal("20.00"));
    }

    @Test
    void toUserDtos_shouldReturnEmptyList_whenResultsAreEmpty() {
        List<Object[]> results = List.of();

        List<UserDto> result = userDtoMapper.toUserDtos(results);

        assertThat(result).isEmpty();
    }


    @Test
    void toUserLightDtos_shouldMapUserSqlEntitiesToUserLightDtoList() {
        UserSqlEntity user1 = new UserSqlEntity();
        user1.setName("John Doe");
        user1.setLastLoggedInAt(LocalDateTime.now().minusMinutes(90));

        UserSqlEntity user2 = new UserSqlEntity();
        user2.setName("Jane Smith");
        user2.setLastLoggedInAt(LocalDateTime.now().minusMinutes(30));

        List<UserSqlEntity> users = Arrays.asList(user1, user2);

        List<UserLightDto> result = userDtoMapper.toUserLightDtos(users);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("John Doe");
        assertThat(result.get(0).lastLoggedIn()).isEqualTo("1h 30min");
        assertThat(result.get(1).name()).isEqualTo("Jane Smith");
        assertThat(result.get(1).lastLoggedIn()).isEqualTo("30min");
    }

    @Test
    void toUserLightDtos_shouldReturnEmptyList_whenUsersAreEmpty() {
        List<UserSqlEntity> users = List.of();

        List<UserLightDto> result = userDtoMapper.toUserLightDtos(users);

        assertThat(result).isEmpty();
    }
}
