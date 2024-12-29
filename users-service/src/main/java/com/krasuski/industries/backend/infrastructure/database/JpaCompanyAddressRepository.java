package com.krasuski.industries.backend.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCompanyAddressRepository extends JpaRepository<DDDCompanyAddressEntity, Long> {
    List<DDDCompanyAddressEntity> findAllByUser(DDDUserEntity user);
}
