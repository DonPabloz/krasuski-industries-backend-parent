package com.krasuski.industries.backend.entity.address;

import com.krasuski.industries.backend.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class PrivateAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private UUID publicId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneExtension;
    private String phoneNumber;
    private String street;
    private Integer buildingNumber;
    private Integer apartmentNumber;
    private String zipCode;
    private String city;
}
