package com.krasuski.industries.backend.infrastructure.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ddd_refresh_token")
@NoArgsConstructor
@Getter
@Setter
class DDDRefreshTokenEntity {

    private static final String ID_GENERATOR_NAME = "ddd_refresh_token_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GENERATOR_NAME)
    @SequenceGenerator(name = ID_GENERATOR_NAME, sequenceName = ID_GENERATOR_NAME, allocationSize = 1)
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "time_to_live_in_seconds")
    private Long timeToLiveInSeconds;

    @ManyToOne(fetch = FetchType.LAZY)
    private DDDUserEntity user;
}
