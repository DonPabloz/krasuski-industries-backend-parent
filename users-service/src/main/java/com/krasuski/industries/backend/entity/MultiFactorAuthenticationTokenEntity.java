package com.krasuski.industries.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "multi_factor_authentication_token")
@Data
public class MultiFactorAuthenticationTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    @OneToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_details_id")
    private UserEntity userEntity;
    private Date expiryDate;
}
