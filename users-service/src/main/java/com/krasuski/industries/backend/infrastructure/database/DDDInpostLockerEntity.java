package com.krasuski.industries.backend.infrastructure.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ddd_inpost_locker")
@NoArgsConstructor
@Getter
@Setter
public class DDDInpostLockerEntity {

    private static final String ID_GENERATOR_NAME = "ddd_inpost_locker_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GENERATOR_NAME)
    @SequenceGenerator(name = ID_GENERATOR_NAME, sequenceName = ID_GENERATOR_NAME, allocationSize = 1)
    private Long id;

    @Column(name = "inpost_external_pub_id")
    private String inpostExternalPubId;

    @ManyToOne(fetch = FetchType.LAZY)
    private DDDUserEntity user;
}
