package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.mapper.UserMapper;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


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
    private final UserMapper mapper;

    public UserCommandRepositoryImpl(UserCommandRepositoryAdapter userCommandRepository, final UserMapper mapper) {
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
    public UserDomainEntity save(UserDomainEntity user) {

        return mapper.toDomainEntity(userCommandRepository.save(mapper.toSqlEntity(user)));
    }

    @Override
    public List<UserDomainEntity> saveAll(Iterable<UserDomainEntity> entities) {

        return userCommandRepository.saveAll(StreamSupport.stream(entities.spliterator(), false)
                        .map(user -> mapper.toSqlEntity(user)).collect(Collectors.toList())).stream()
                .map(user -> mapper.toDomainEntity(user)).collect(Collectors.toList());
    }

    @Override
    public List<UserDomainEntity> findAllById(Iterable<Long> ids) {
        return userCommandRepository.findAllById(ids)
                .stream().map(user -> mapper.toDomainEntity(user))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userCommandRepository.existsUserByEmail(email);
    }
}
