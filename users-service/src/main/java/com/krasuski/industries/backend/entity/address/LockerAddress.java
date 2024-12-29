package com.krasuski.industries.backend.entity.address;

import com.krasuski.industries.backend.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class LockerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private UUID publicId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private String name;
    private String externalId;
    private String street;
    private Integer buildingNumber;
    private String zipCode;
    private String city;
}
