package com.krasuski.industries.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "user_details")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID publicId;
    private String companyName;
    private String companyNip;
    private String email;
    private String name;
    private String surname;
    private String password;
    private String phoneExtension;
    private String phoneNumber;
    private Boolean enabled;
    @ManyToOne
    @JoinColumn(name = "user_role_id")
    private UserRole userRole;
    private Boolean isNewsletterSubscriber;
}
