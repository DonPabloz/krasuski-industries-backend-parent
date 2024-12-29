package com.krasuski.industries.backend.infrastructure.database;

import com.krasuski.industries.backend.application.port.UserRepository;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.value.Email;
import com.krasuski.industries.backend.domain.value.UserPubId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final JpaCompanyAddressRepository jpaCompanyAddressRepository;
    private final JpaPrivateAddressRepository jpaPrivateAddressRepository;
    private final JpaInpostLockerRepository jpaInpostLockerRepository;
    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;
    private final EntityMapper entityMapper;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository, JpaCompanyAddressRepository jpaCompanyAddressRepository, JpaPrivateAddressRepository jpaPrivateAddressRepository, JpaInpostLockerRepository jpaInpostLockerRepository, JpaRefreshTokenRepository jpaRefreshTokenRepository, EntityMapper entityMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.jpaCompanyAddressRepository = jpaCompanyAddressRepository;
        this.jpaPrivateAddressRepository = jpaPrivateAddressRepository;
        this.jpaInpostLockerRepository = jpaInpostLockerRepository;
        this.jpaRefreshTokenRepository = jpaRefreshTokenRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public Optional<User> findByPubId(UserPubId pubId) {
        return jpaUserRepository.findByPubId(pubId.value())
                .map(this::getUser);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaUserRepository.findByEmail(email.value())
                .map(this::getUser);
    }

    @Override
    public void save(User user) {
        DDDUserEntity dddUserEntity = entityMapper.toEntity(user);

        user.getPrivateAddresses()
                .stream()
                .map(entityMapper::toEntity)
                .forEach(dddUserEntity::addPrivateAddress);

        user.getCompanyAddresses()
                .stream()
                .map(entityMapper::toEntity)
                .forEach(dddUserEntity::addCompanyAddress);

        user.getRefreshTokens()
                .stream()
                .map(entityMapper::toEntity)
                .forEach(dddUserEntity::addRefreshToken);

        user.getInpostLockers()
                .stream()
                .map(entityMapper::toEntity)
                .forEach(dddUserEntity::addInpostLocker);

        jpaUserRepository.save(dddUserEntity);
    }

    private User getUser(DDDUserEntity dddUserEntity) {
        List<DDDCompanyAddressEntity> companyAddresses = jpaCompanyAddressRepository.findAllByUser(dddUserEntity);
        List<DDDPrivateAddressEntity> privateAddresses = jpaPrivateAddressRepository.findAllByUser(dddUserEntity);
        List<DDDInpostLockerEntity> inpostLockers = jpaInpostLockerRepository.findAllByUser(dddUserEntity);
        List<DDDRefreshTokenEntity> refreshTokens = jpaRefreshTokenRepository.findAllByUser(dddUserEntity);

        User user = entityMapper.fromEntity(dddUserEntity);
        user.getCompanyAddresses().addAll(companyAddresses.stream().map(entityMapper::fromEntity).toList());
        user.getPrivateAddresses().addAll(privateAddresses.stream().map(entityMapper::fromEntity).toList());
        user.getInpostLockers().addAll(inpostLockers.stream().map(entityMapper::fromEntity).toList());
        user.getRefreshTokens().addAll(refreshTokens.stream().map(entityMapper::fromEntity).toList());

        return user;
    }
}
