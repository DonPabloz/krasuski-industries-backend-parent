package com.krasuski.industries.backend.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaInpostLockerRepository extends JpaRepository<DDDInpostLockerEntity, Long> {
    List<DDDInpostLockerEntity> findAllByUser(DDDUserEntity DDDUserEntity);
}
