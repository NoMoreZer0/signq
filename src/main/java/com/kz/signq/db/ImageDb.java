package com.kz.signq.db;

import com.kz.signq.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageDb extends JpaRepository<Image, UUID> {

    Optional<Image> findImageByName(String fileName);

}
