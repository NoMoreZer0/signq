package com.kz.signq.service.impl;

import com.kz.signq.db.FileDb;
import com.kz.signq.dto.EntityIdDto;
import com.kz.signq.model.File;
import com.kz.signq.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileDb db;

    @Value("${file.storing.path}")
    private String folderPath;

    @Override
    public EntityIdDto uploadFile(MultipartFile file) throws IOException {
        var filePath = String.format("%s/%s", folderPath, file.getOriginalFilename());
        var savedFile = db.save(File.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath).build()
        );
        file.transferTo(new java.io.File(filePath));
        return EntityIdDto.fromBaseEntity(savedFile);
    }

    @Override
    public Optional<File> findById(UUID id){
        return db.findById(id);
    }

    @Override
    public byte[] downloadFileFromId(UUID id) throws IOException {
        var opt = db.findById(id);
        if (opt.isEmpty()) {
            return new byte[0];
        }
        var filePath = opt.get().getFilePath();
        return Files.readAllBytes(new java.io.File(filePath).toPath());
    }
}
