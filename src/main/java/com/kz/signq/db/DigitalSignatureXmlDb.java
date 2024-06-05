package com.kz.signq.db;

import com.kz.signq.model.DigitalSignatureXml;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DigitalSignatureXmlDb extends JpaRepository<DigitalSignatureXml, Long> {
    boolean existsByUserIinAndPetitionId(String userIin, UUID petitionId);
    long countByPetitionId(UUID petitionId);
}
