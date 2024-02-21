package com.kz.signq.db;

import com.kz.signq.model.Petition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetitionDb extends JpaRepository<Petition, UUID> {

    List<Petition> findAllByCreatedBy(UUID userId);

}
