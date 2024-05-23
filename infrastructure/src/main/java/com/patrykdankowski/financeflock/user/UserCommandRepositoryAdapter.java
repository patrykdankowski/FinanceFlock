package com.patrykdankowski.financeflock.user;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.patrykdankowski.financeflock.user.UserSqlEntity.toDomainUser;

interface UserCommandRepositoryAdapter extends Repository<UserSqlEntity, Long> {

    Optional<UserSqlEntity> findByEmail(String email);

    UserSqlEntity save(UserSqlEntity user);

    List<UserSqlEntity> saveAll(Iterable<UserSqlEntity> entities);

    List<UserSqlEntity> findAllById(Iterable<Long> ids);

    boolean existsUserByEmail(String email);
}

@org.springframework.stereotype.Repository
class UserCommandRepositoryImpl implements UserCommandRepositoryPort {

    private final UserCommandRepositoryAdapter userCommandRepository;

    public UserCommandRepositoryImpl(UserCommandRepositoryAdapter userCommandRepository) {
        this.userCommandRepository = userCommandRepository;
    }

    @Override
    public Optional<UserDomainEntity> findByEmail(String email) {
        return userCommandRepository.findByEmail(email)
                .map(UserSqlEntity::toDomainUser);
    }

    @Override
    public UserDomainEntity save(UserDomainEntity user) {
        return toDomainUser(userCommandRepository.save(UserSqlEntity.fromDomainUser(user)));
    }

    @Override
    public List<UserDomainEntity> saveAll(Iterable<UserDomainEntity> entities) {
        return userCommandRepository.saveAll(StreamSupport.stream(entities.spliterator(), false)
                        .map(UserSqlEntity::fromDomainUser).collect(Collectors.toList())).stream()
                .map(UserSqlEntity::toDomainUser).collect(Collectors.toList());
    }

    @Override
    public List<UserDomainEntity> findAllById(Iterable<Long> ids) {
        return userCommandRepository.findAllById(ids)
                .stream().map(UserSqlEntity::toDomainUser)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userCommandRepository.existsUserByEmail(email);
    }
}
