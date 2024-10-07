package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDto;
import com.patrykdankowski.financeflock.user.dto.SimpleUserDomainEntity;
import com.patrykdankowski.financeflock.user.dto.UserDetailsDto;
import com.patrykdankowski.financeflock.user.dto.UserDto;
import com.patrykdankowski.financeflock.user.dto.UserLightDto;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.patrykdankowski.financeflock.common.AppConstants.minutesInHour;

@Component
public class UserDtoMapper {

    public SimpleUserDomainEntity toSimpleUserDomainEntity(UserSqlEntity userSqlEntity) {
        if (userSqlEntity == null) {
            return null;
        } else {
            Long budgetGroupId = userSqlEntity.getBudgetGroup() != null ? userSqlEntity.getBudgetGroup().getId() : null;
            return SimpleUserDomainEntity.buildUser(userSqlEntity.getId(),
                    userSqlEntity.getName(),
                    userSqlEntity.getEmail(),
                    budgetGroupId,
                    userSqlEntity.getRole(),
                    userSqlEntity.getLastLoggedInAt(),
                    userSqlEntity.isShareData(),
                    userSqlEntity.getLastToggledShareData(),
                    userSqlEntity.getCreatedAt());
        }
    }

    public UserDetailsDto toUserDetailsDto(UserSqlEntity userSqlEntity) {
        if (userSqlEntity == null) {
            return null;
        } else {
            return new UserDetailsDto(userSqlEntity.getEmail(), userSqlEntity.getPassword(), userSqlEntity.getRole().toString());
        }
    }



    public List<UserDto> toUserDtos(List<Object[]> results) {
        Map<Long, UserDto> userDtoMap = new LinkedHashMap<>();


        results.forEach(row -> {
            Long userId = ((Number) row[0]).longValue();
            String userName = (String) row[1];
            Long expenseId = row[2] != null ? ((Number) row[2]).longValue() : null;
            String description = (String) row[3];
            BigDecimal amount = row[4] != null ? (BigDecimal) row[4] : BigDecimal.ZERO;
            String location = (String) row[5];
            BigDecimal totalExpenses = (BigDecimal) row[6];

            ExpenseDto expenseDto = createExpenseDto(expenseId, description, amount, location);
            updateUserDtoMap(userDtoMap, userId, userName, expenseDto, totalExpenses);
        });

        return new ArrayList<>(userDtoMap.values());
    }

    private ExpenseDto createExpenseDto(Long expenseId, String description, BigDecimal amount, String location) {
        if (expenseId != null) {
            return new ExpenseDto(description, amount, location);
        }
        return null;
    }

    private void updateUserDtoMap(Map<Long, UserDto> userDtoMap, Long userId, String userName,
                                  ExpenseDto expenseDto, BigDecimal totalExpenses) {
        if (userDtoMap.containsKey(userId)) {
            if (expenseDto != null) {
                userDtoMap.get(userId).getExpenses().add(expenseDto);
            }
        } else {
            UserDto userDto = new UserDto(userName, new ArrayList<>(), totalExpenses);
            if (expenseDto != null) {
                userDto.getExpenses().add(expenseDto);
            }
            userDtoMap.put(userId, userDto);
        }
    }

//    }
    public List<UserLightDto> toUserLightDtos(List<UserSqlEntity> users) {
        List<UserLightDto> userLightDtos = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        users.forEach(user -> {
            String formattedTime = calculateFormattedTime(user.getLastLoggedInAt(), now);
            userLightDtos.add(new UserLightDto(user.getName(), formattedTime));
        });

        return userLightDtos;
    }

    private String calculateFormattedTime(LocalDateTime lastLoggedInAt, LocalDateTime now) {
        if (lastLoggedInAt != null) {
            long minutesBetween = ChronoUnit.MINUTES.between(lastLoggedInAt, now);

            if (minutesBetween >= minutesInHour) {
                long hours = minutesBetween / minutesInHour;
                long minutes = minutesBetween % minutesInHour;
                return String.format("%dh %dmin", hours, minutes);
            } else {
                return String.format("%dmin", minutesBetween);
            }
        } else {
            return "N/A";
        }

    }
}