package com.krasuski.industries.backend.infrastructure.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ddd_verification_token")
@NoArgsConstructor
@Getter
@Setter
class DDDVerificationTokenEntity {

    private static final String ID_GENERATOR_NAME = "ddd_verification_token_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GENERATOR_NAME)
    @SequenceGenerator(name = ID_GENERATOR_NAME, sequenceName = ID_GENERATOR_NAME, allocationSize = 1)
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private DDDUserEntity user;

}
