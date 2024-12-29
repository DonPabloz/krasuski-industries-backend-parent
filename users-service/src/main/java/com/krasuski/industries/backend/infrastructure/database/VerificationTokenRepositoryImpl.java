package com.krasuski.industries.backend.infrastructure.database;

import com.krasuski.industries.backend.application.port.VerificationTokenRepository;
import com.krasuski.industries.backend.domain.VerificationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class VerificationTokenRepositoryImpl implements VerificationTokenRepository {

    private final JpaVerificationTokenRepository jpaVerificationTokenRepository;
    private final EntityMapper entityMapper;
    private final JpaUserRepository jpaUserRepository;

    VerificationTokenRepositoryImpl(JpaVerificationTokenRepository jpaVerificationTokenRepository, EntityMapper entityMapper, JpaUserRepository jpaUserRepository) {
        this.jpaVerificationTokenRepository = jpaVerificationTokenRepository;
        this.entityMapper = entityMapper;
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public void save(VerificationToken verificationToken) {
        DDDUserEntity dddUserEntity = jpaUserRepository.findByPubId(verificationToken.getUser().getPubId().value()).orElseThrow();
        DDDVerificationTokenEntity entity = entityMapper.toEntity(verificationToken);
        entity.setUser(dddUserEntity);
        jpaVerificationTokenRepository.save(entity);
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return jpaVerificationTokenRepository.findByToken(token).map(entityMapper::fromEntity);
    }
}
