package com.kz.signq.controller;

import com.kz.signq.dto.EntityIdDto;
import com.kz.signq.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<EntityIdDto> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(fileService.uploadFile(file));
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> download(@PathVariable UUID fileId) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("image/png"))
                .body(fileService.downloadFileFromId(fileId));
    }
}
