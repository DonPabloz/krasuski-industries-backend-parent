package com.krasuski.industries.backend.repositories.address;

import com.krasuski.industries.backend.entity.address.PrivateAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrivateAddressRepository extends JpaRepository<PrivateAddress, Long> {
    List<PrivateAddress> findAllByUserId(Long userId);

    Optional<PrivateAddress> findByPublicId(UUID publicId);
}
