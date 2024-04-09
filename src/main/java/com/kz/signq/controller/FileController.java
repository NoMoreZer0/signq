package com.kz.signq.controller;

import com.kz.signq.service.FileService;
import com.kz.signq.utils.ErrorCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok().body(fileService.uploadFile(file));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(ErrorCodeUtil.toExceptionDto(e));
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<?> download(@PathVariable UUID fileId) {
        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("image/png"))
                    .body(fileService.downloadFileFromId(fileId));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(ErrorCodeUtil.toExceptionDto(e));
        }
    }
}
