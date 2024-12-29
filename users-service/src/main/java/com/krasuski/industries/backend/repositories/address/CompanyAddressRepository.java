package com.krasuski.industries.backend.repositories.address;

import com.krasuski.industries.backend.entity.address.CompanyAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyAddressRepository extends JpaRepository<CompanyAddress, Long> {
    List<CompanyAddress> findAllByUserId(Long userId);

    Optional<CompanyAddress> findByPublicId(UUID publicId);
}
