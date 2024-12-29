package com.krasuski.industries.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserRefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @OneToOne
    @JoinColumn(name = "user_details_id")
    public UserEntity userEntity;
    public String token;
    public Long expirationTime;
}
