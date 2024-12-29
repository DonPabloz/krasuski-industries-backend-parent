package com.krasuski.industries.backend.infrastructure.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ddd_private_address")
@NoArgsConstructor
@Getter
@Setter
class DDDPrivateAddressEntity {

    private static final String ID_GENERATOR_NAME = "ddd_private_address_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GENERATOR_NAME)
    @SequenceGenerator(name = ID_GENERATOR_NAME, sequenceName = ID_GENERATOR_NAME, allocationSize = 1)
    private Long id;

    @Column(name = "pub_id")
    private String pubId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number_extension")
    private String phoneNumberExtension;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "street")
    private String street;

    @Column(name = "building_number")
    private String buildingNumber;

    @Column(name = "apartment_number")
    private String apartmentNumber;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "city")
    private String city;

    @ManyToOne(fetch = FetchType.LAZY)
    private DDDUserEntity user;
}
