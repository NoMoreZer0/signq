package com.kz.signq.db;

import com.kz.signq.model.Petition;
import com.kz.signq.model.PetitionStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface PetitionDb extends JpaRepository<Petition, UUID> {

    List<Petition> findAllByCreatedBy(UUID userId);

    List<Petition> findAllByStatusIn(Collection<PetitionStatus> status, Pageable pageable);

    List<Petition> findAllByStatus(PetitionStatus status, Pageable pageable);
}
