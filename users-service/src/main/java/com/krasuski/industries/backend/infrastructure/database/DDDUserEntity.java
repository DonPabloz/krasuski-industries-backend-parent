package com.krasuski.industries.backend.infrastructure.database;

import com.krasuski.industries.backend.domain.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ddd_user")
@NoArgsConstructor
@Getter
@Setter
class DDDUserEntity {

    private static final String ID_GENERATOR_NAME = "ddd_user_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GENERATOR_NAME)
    @SequenceGenerator(name = ID_GENERATOR_NAME, sequenceName = ID_GENERATOR_NAME, allocationSize = 1)
    private Long id;

    @Column(name = "pub_id")
    private String pubId;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "hashed_password")
    private String hashedPassword;

    @Column(name = "is_account_verified")
    private Boolean isAccountVerified;

    @Column(name = "anonymous_user_session")
    private String anonymousUserSession;

    @Column(name = "removed")
    private Boolean removed;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DDDPrivateAddressEntity> privateAddressEntities = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DDDCompanyAddressEntity> companyAddressEntities = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DDDRefreshTokenEntity> refreshTokenEntities = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DDDInpostLockerEntity> inpostLockerEntities = new ArrayList<>();

    public void addPrivateAddress(DDDPrivateAddressEntity privateAddressEntity) {
        privateAddressEntities.add(privateAddressEntity);
        privateAddressEntity.setUser(this);
    }

    public void removePrivateAddress(DDDPrivateAddressEntity privateAddressEntity) {
        privateAddressEntities.remove(privateAddressEntity);
        privateAddressEntity.setUser(this);
    }

    public void addCompanyAddress(DDDCompanyAddressEntity dddCompanyAddressEntity) {
        companyAddressEntities.add(dddCompanyAddressEntity);
        dddCompanyAddressEntity.setUser(this);
    }

    public void removeCompanyAddress(DDDCompanyAddressEntity dddCompanyAddressEntity) {
        companyAddressEntities.remove(dddCompanyAddressEntity);
        dddCompanyAddressEntity.setUser(this);
    }

    public void addRefreshToken(DDDRefreshTokenEntity refreshTokenEntity) {
        refreshTokenEntities.add(refreshTokenEntity);
        refreshTokenEntity.setUser(this);
    }

    public void removeRefreshToken(DDDRefreshTokenEntity refreshTokenEntity) {
        refreshTokenEntities.remove(refreshTokenEntity);
        refreshTokenEntity.setUser(this);
    }

    public void addInpostLocker(DDDInpostLockerEntity inpostLockerEntity) {
        inpostLockerEntities.add(inpostLockerEntity);
        inpostLockerEntity.setUser(this);
    }

    public void removeInpostLocker(DDDInpostLockerEntity inpostLockerEntity) {
        inpostLockerEntities.remove(inpostLockerEntity);
        inpostLockerEntity.setUser(this);
    }
}
