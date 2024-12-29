package com.krasuski.industries.backend.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface JpaPrivateAddressRepository extends JpaRepository<DDDPrivateAddressEntity, Long> {
    List<DDDPrivateAddressEntity> findAllByUser(DDDUserEntity user);
}
