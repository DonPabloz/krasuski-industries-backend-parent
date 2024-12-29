package com.krasuski.industries.backend.repositories.address;

import com.krasuski.industries.backend.entity.address.LockerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LockerAddressRepository extends JpaRepository<LockerAddress, Long> {
    List<LockerAddress> findAllByUserId(Long userId);

    Optional<LockerAddress> findByPublicId(UUID publicId);
}
