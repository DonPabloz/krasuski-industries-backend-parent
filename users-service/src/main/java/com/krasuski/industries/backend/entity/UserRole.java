package com.krasuski.industries.backend.entity;

import com.krasuski.industries.backend.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_role")
@Data
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Role role;
}
