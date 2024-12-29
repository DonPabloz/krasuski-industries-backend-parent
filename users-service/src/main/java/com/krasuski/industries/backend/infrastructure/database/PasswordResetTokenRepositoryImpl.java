package com.krasuski.industries.backend.infrastructure.database;

import com.krasuski.industries.backend.application.port.PasswordResetTokenRepository;
import com.krasuski.industries.backend.domain.PasswordResetToken;
import com.krasuski.industries.backend.domain.value.PasswordResetTokenValue;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordResetTokenRepositoryImpl implements PasswordResetTokenRepository {

    private final JpaUserRepository jpaUserRepository;
    private final JpaPasswordResetTokenRepository jpaPasswordResetTokenRepository;
    private final EntityMapper entityMapper;

    public PasswordResetTokenRepositoryImpl(JpaUserRepository jpaUserRepository, JpaPasswordResetTokenRepository jpaPasswordResetTokenRepository, EntityMapper entityMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.jpaPasswordResetTokenRepository = jpaPasswordResetTokenRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public void save(PasswordResetToken passwordResetToken) {
        String userPubId = passwordResetToken.getUser().getPubId().value();
        DDDUserEntity user = jpaUserRepository.findByPubId(userPubId).orElseThrow(RuntimeException::new);

        DDDPasswordResetTokenEntity dddPasswordResetTokenEntity = entityMapper.toEntity(passwordResetToken);
        dddPasswordResetTokenEntity.setUser(user);
        jpaPasswordResetTokenRepository.saveAndFlush(dddPasswordResetTokenEntity);
    }

    @Override
    public Optional<PasswordResetToken> findByToken(PasswordResetTokenValue passwordResetTokenValue) {
        return jpaPasswordResetTokenRepository.findByToken(passwordResetTokenValue.value())
                .map(entityMapper::fromEntity);
    }
}
