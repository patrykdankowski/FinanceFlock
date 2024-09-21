package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.mapper.UserDtoMapper;
import com.patrykdankowski.financeflock.user.dto.SimpleUserDomainEntity;
import com.patrykdankowski.financeflock.user.dto.UserDetailsDto;
import com.patrykdankowski.financeflock.user.dto.UserDto;
import com.patrykdankowski.financeflock.user.dto.UserLight;
import com.patrykdankowski.financeflock.user.dto.UserLightDto;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import com.patrykdankowski.financeflock.user.port.UserQueryRepositoryPort;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserQueryRepositoryAdapter extends Repository<UserSqlEntity, Long> {


    List<UserSqlEntity> findAllByBudgetGroup_Id(@Param("groupId") Long groupId, Pageable pageable);

    Optional<UserSqlEntity> findSimpleUserByEmail(String email);

    @Query("SELECT u FROM UserSqlEntity u LEFT JOIN FETCH u.expenseList WHERE u.email = :email")
    Optional<UserSqlEntity> findUserDetailsByEmail(String email);

    //    @EntityGraph(attributePaths = "expenseList")
//    @Query("SELECT u FROM UserSqlEntity u WHERE u.budgetGroup.id = :groupId")
//    List<UserSqlEntity> findUsersWithTotalExpenses(@Param("groupId") Long groupId);
//    @Query(value = "SELECT u.id AS userId, u.name, e.id AS expenseId, e.description, e.amount, e.location, " +
//            "s.total_expenses AS totalExpenses " +
//            "FROM users u " +
//            "JOIN user_expenses_summary s ON s.user_id = u.id " +
//            "LEFT JOIN expanses e ON e.user_id = u.id " +
//            "WHERE u.group_id = :groupId AND s.share_data = TRUE " +
//            "ORDER BY s.total_expenses DESC " +
//            "LIMIT :limit OFFSET :offset",
//            nativeQuery = true)
//    List<Map<String, Object>> findUsersWithTotalExpenses(@Param("groupId") Long groupId,
//                                                         @Param("limit") int limit,
//                                                         @Param("offset") int offset);
//}

    // działa 2 wersja
//    @Query(value = "SELECT u.userId, u.name, e.id AS expenseId, e.description, e.amount, e.location, u.totalExpenses " +
//            "FROM (" +
//            "   SELECT u.id AS userId, u.name, s.total_expenses AS totalExpenses " +
//            "   FROM users u " +
//            "   JOIN user_expenses_summary s ON s.user_id = u.id " +
//            "   WHERE u.group_id = :groupId AND s.share_data = TRUE " +
//            "   ORDER BY s.total_expenses DESC " +
//            "   LIMIT :limit OFFSET :offset" +
//            ") u " +
//            "LEFT JOIN expanses e ON e.user_id = u.userId " +
//            "ORDER BY u.totalExpenses DESC",
//            nativeQuery = true)
//    List<Map<String, Object>> findUsersWithTotalExpenses(@Param("groupId") Long groupId,
//                                                         @Param("limit") int limit,
//                                                         @Param("offset") int offset);

    @org.springframework.stereotype.Repository
    class UserQueryRepositoryImpl implements UserQueryRepositoryPort {
        private final UserQueryRepositoryAdapter userQueryRepository;
        private final EntityManager entityManager;
        private final UserDtoMapper userDtoMapper;

        UserQueryRepositoryImpl(UserQueryRepositoryAdapter userQueryRepository, final EntityManager entityManager,
                                final UserDtoMapper userDtoMapper) {
            this.userQueryRepository = userQueryRepository;
            this.entityManager = entityManager;
            this.userDtoMapper = userDtoMapper;
        }


        @Override
        public List<UserLightDto> findAllByBudgetGroup_Id(Long id, final Pageable pageable) {

            final List<UserSqlEntity> allUsersFromGroup = userQueryRepository.findAllByBudgetGroup_Id(id, pageable);
            return userDtoMapper.toUserLightDtos(allUsersFromGroup);

//        return userQueryRepository.findAllByBudgetGroup_Id(id, pageable).stream().map(
//                userSql -> userMapper.toDomainEntity(userSql)
//        ).collect(Collectors.toList());
        }

        @Override
        public Optional<SimpleUserDomainEntity> retrieveUserFromMail(final String mail) {
            return userQueryRepository.findSimpleUserByEmail(mail).map(user -> userDtoMapper.toSimpleUserDomainEntity(user));
        }

        @Override
        public Optional<UserDetailsDto> retrieveUserFromEmail(final String email) {
            return userQueryRepository.findUserDetailsByEmail(email).map(user -> userDtoMapper.toUserDetailsDto(user));
        }

        @Override
        public List<UserDto> findUserExpenseSummaries(final Long groupId, final Pageable pageable) {
            // Pobieramy surowe dane z repozytorium
//            int offset = (int) pageable.getOffset();
//            int limit = pageable.getPageSize();
//
//            List<Map<String, Object>> rawResults = userQueryRepository.findUsersWithTotalExpenses(groupId, limit, offset);
//
//            // Mapujemy na DTO za pomocą mappera
//            return userDtoMapper.toUserWithExpenseListDto(rawResults);
            // Pobieramy limit i offset z Pageable
            // działa 2 wersja
//            int limit = pageable.getPageSize();
//            int offset = (int) pageable.getOffset();
//
//            // Pobieramy dane z bazy danych
//            List<Map<String, Object>> rawResults = userQueryRepository.findUsersWithTotalExpenses(groupId, limit, offset);
//
//            // Wywołujemy mapper, aby zmapować wyniki na UserDto
//            return userDtoMapper.toUserWithExpenseListDto(rawResults);
            // Pobieramy limit i offset z Pageable
            // Pobieramy kierunek sortowania z Pageable

            // Tworzenie natywnego zapytania z dynamicznym sortowaniem
//            String sql = "SELECT u.userId, u.name, e.id AS expenseId, e.description, e.amount, e.location, u.totalExpenses " +
//                    "FROM (" +
//                    "   SELECT u.id AS userId, u.name, s.total_expenses AS totalExpenses " +
//                    "   FROM users u " +
//                    "   JOIN user_expenses_summary s ON s.user_id = u.id " +
//                    "   WHERE u.group_id = :groupId AND s.share_data = TRUE " +
//                    "   ORDER BY s.total_expenses " + sortDirection + " " +
//                    "   LIMIT :limit OFFSET :offset" +
//                    ") u " +
//                    "LEFT JOIN expanses e ON e.user_id = u.userId " +
//                    "ORDER BY u.totalExpenses " + sortDirection;
            String sql = getSqlPrompt(pageable);


            jakarta.persistence.Query query = setParameters(groupId, pageable, sql);

            List<Object[]> rawResults = query.getResultList();
            rawResults.forEach(row -> System.out.println("user id" + row[0] + "total expanses " + row[6]));

            return userDtoMapper.toUserDtos(rawResults);

        }

        private jakarta.persistence.Query setParameters(final Long groupId, final Pageable pageable, final String sql) {
            jakarta.persistence.Query query = entityManager.createNativeQuery(sql);
            query.setParameter("groupId", groupId);
            query.setParameter("limit", pageable.getPageSize());
            query.setParameter("offset", pageable.getOffset());
            return query;
        }

        private String getSqlPrompt(final Pageable pageable) {
            String sortDirection = pageable.getSort().getOrderFor("totalExpensesForUser").isAscending() ? "ASC" : "DESC";
            String sql = "SELECT u.userId, u.name, e.id AS expenseId, e.description, e.amount, e.location, u.totalExpenses " +
                    "FROM (" +
                    "   SELECT u.id AS userId, u.name, s.total_expenses AS totalExpenses " +
                    "   FROM users u " +
                    "   JOIN user_expenses_summary s ON s.user_id = u.id " +
                    "   WHERE u.group_id = :groupId AND u.share_data = TRUE " +
                    "   ORDER BY s.total_expenses " + sortDirection + " " +
                    "   LIMIT :limit OFFSET :offset" +
                    ") u " +
                    "LEFT JOIN expanses e ON e.user_id = u.userId " +
                    "ORDER BY u.totalExpenses " + sortDirection;
//            String sql = "SELECT u.id, u.name, e.id AS expenseId, e.description, e.amount, e.location, s.total_expenses AS totalExpenses " +
//                    "FROM users u " +
//                    "JOIN user_expenses_summary s ON s.user_id = u.id " +
//                    "LEFT JOIN expanses e ON e.user_id = u.id " +
//                    "WHERE u.group_id = :groupId AND u.share_data = TRUE " +
//                    "ORDER BY s.total_expenses " + sortDirection + " " +
//                    "LIMIT :limit OFFSET :offset";
            return sql;
        }


    }
}
