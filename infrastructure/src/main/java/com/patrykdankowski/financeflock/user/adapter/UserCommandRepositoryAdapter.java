package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.mapper.UserMapper;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserCommandRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


interface UserCommandRepositoryAdapter extends JpaRepository<UserSqlEntity, Long> {

    Optional<UserSqlEntity> findByEmail(String email);
//
//    Optional<UserSqlEntity> findById(Long id);
//
//    UserSqlEntity save(UserSqlEntity user);
//
//    List<UserSqlEntity> saveAll(List<UserSqlEntity> entities);
//
    @Query("SELECT u FROM UserSqlEntity u WHERE u.id IN :ids")
    List<UserSqlEntity> findAllByIdIn(List<Long> ids);
//
    boolean existsUserByEmail(String email);
}

@Slf4j
@org.springframework.stereotype.Repository
class UserCommandRepositoryImpl implements UserCommandRepositoryPort {

    private final UserCommandRepositoryAdapter userCommandRepository;
    private final UserMapper mapper;

    public UserCommandRepositoryImpl(UserCommandRepositoryAdapter userCommandRepository,
                                     final UserMapper mapper) {
        this.userCommandRepository = userCommandRepository;
        this.mapper = mapper;
    }

    //    @Override
//    public Optional<UserDomainEntity> findByEmail(String email) {
//        return userCommandRepository.findByEmail(email)
//                .map(UserSqlEntity::toDomainUser);
//    }
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
        log.info("before saving");
UserSqlEntity sqlUserSaved = userCommandRepository.save(mapper.toSqlEntity(user));
        log.info("after saving");
        final UserDomainEntity domainEntity = mapper.toDomainEntity(sqlUserSaved);
//        domainEntity.
        return domainEntity;
    }

    @Override
    public List<UserDomainEntity> saveAll(List<UserDomainEntity> entities) {
        List<UserSqlEntity> usersSqlToSave = entities.stream()
                .map(user -> {
                    log.info("Mapping user to SQL entity: {}", user);
                    return mapper.toSqlEntity(user);
                }).collect(Collectors.toList());

        log.info("Mapping to SQL entities repo {}", usersSqlToSave);

        List<UserSqlEntity> saved = userCommandRepository.saveAll(usersSqlToSave);

        log.info("After saving: {}", saved);

        return saved.stream()
                .map(user -> {
                    log.info("Mapping SQL entity to domain entity: {}", user);
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
}
