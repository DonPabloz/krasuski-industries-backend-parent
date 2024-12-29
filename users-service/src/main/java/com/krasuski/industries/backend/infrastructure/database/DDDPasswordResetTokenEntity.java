package com.krasuski.industries.backend.infrastructure.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ddd_password_reset_token")
@NoArgsConstructor
@Getter
@Setter
public class DDDPasswordResetTokenEntity {

        private static final String ID_GENERATOR_NAME = "ddd_password_reset_token_id_seq";

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GENERATOR_NAME)
        @SequenceGenerator(name = ID_GENERATOR_NAME, sequenceName = ID_GENERATOR_NAME, allocationSize = 1)
        private Long id;

        @Column(name = "creation_date")
        private LocalDateTime creationDate;

        @Column(name = "token")
        private String token;

        @Column(name = "expiration_date")
        private LocalDateTime expirationDate;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private DDDUserEntity user;
}
