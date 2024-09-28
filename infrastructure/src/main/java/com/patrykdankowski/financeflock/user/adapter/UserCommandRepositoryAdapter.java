package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.mapper.UserDtoMapper;
import com.patrykdankowski.financeflock.mapper.UserMapper;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserCommandRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


interface UserCommandRepositoryAdapter extends JpaRepository<UserSqlEntity, Long> {

    @Query("SELECT u FROM UserSqlEntity u " +
            "LEFT JOIN FETCH u.expenseList " +
            "LEFT JOIN FETCH u.budgetGroup g " +
            "LEFT JOIN FETCH g.listOfMembers " +
            "WHERE u.email = :email")
    Optional<UserSqlEntity> findByEmail(String email);

    @Query("SELECT u FROM UserSqlEntity u WHERE u.id IN :ids")
    List<UserSqlEntity> findAllByIdIn(List<Long> ids);


    boolean existsUserByEmail(String email);

    @Modifying
    @Query("UPDATE UserSqlEntity u SET u.lastLoggedInAt = :lastLoggedInAt WHERE u.email = :email")
    void update(@Param("lastLoggedInAt") LocalDateTime lastLoggedInAt, @Param("email") String email);
}

@Slf4j
@org.springframework.stereotype.Repository
class UserCommandRepositoryImpl implements UserCommandRepositoryPort {

    private final UserCommandRepositoryAdapter userCommandRepository;
    private final UserMapper mapper;

     UserCommandRepositoryImpl(UserCommandRepositoryAdapter userCommandRepository,
                                     final UserMapper mapper) {
        this.userCommandRepository = userCommandRepository;
        this.mapper = mapper;
    }


    @Override
    public Optional<UserDomainEntity> findByEmail(String email) {

        return userCommandRepository.findByEmail(email)
                .map(user -> mapper.toDomainEntity(user));
    }

    @Override
    public Optional<UserDomainEntity> findById(final Long id) {
        return userCommandRepository.findById(id)
                .map(user -> mapper.toDomainEntity(user));
    }


    @Override
    public UserDomainEntity save(UserDomainEntity user) {
        UserSqlEntity sqlUserSaved = userCommandRepository.save(mapper.toSqlEntity(user));
        final UserDomainEntity domainEntity = mapper.toDomainEntity(sqlUserSaved);
        return domainEntity;
    }

    @Override
    public List<UserDomainEntity> saveAll(List<UserDomainEntity> entities) {
        List<UserSqlEntity> usersSqlToSave = entities.stream()
                .map(user -> {
                    return mapper.toSqlEntity(user);
                }).collect(Collectors.toList());


        List<UserSqlEntity> saved = userCommandRepository.saveAll(usersSqlToSave);


        return saved.stream()
                .map(user -> {
                    return mapper.toDomainEntity(user);
                }).collect(Collectors.toList());
    }

    @Override
    public List<UserDomainEntity> findAllByIdIn(List<Long> ids) {
        return userCommandRepository.findAllByIdIn(ids)
                .stream().map(user -> mapper.toDomainEntity(user))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userCommandRepository.existsUserByEmail(email);
    }

    @Override
    public void updateLastLoginDate(final LocalDateTime lastLoginDate, final String email) {
        userCommandRepository.update(lastLoginDate, email);
    }


}
