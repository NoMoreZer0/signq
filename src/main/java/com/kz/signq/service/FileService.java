package com.kz.signq.service;

import com.kz.signq.dto.EntityIdDto;
import com.kz.signq.model.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface FileService {

    EntityIdDto uploadFile(MultipartFile file) throws IOException;

    Optional<File> findById(UUID id);

    byte[] downloadFileFromId(UUID id) throws IOException;
}
