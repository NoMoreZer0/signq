package com.kz.signq.db;

import com.kz.signq.model.DigitalSignature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DigitalSignatureDb extends JpaRepository<DigitalSignature, UUID> {
    Optional<DigitalSignature> findByData(String data);
}
