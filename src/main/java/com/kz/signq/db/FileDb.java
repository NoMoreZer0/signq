package com.kz.signq.db;

import com.kz.signq.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileDb extends JpaRepository<File, UUID> {
}
