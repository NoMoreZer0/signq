package com.kz.signq.service.impl;

import com.kz.signq.db.ImageDb;
import com.kz.signq.dto.EntityIdDto;
import com.kz.signq.dto.ImageDto;
import com.kz.signq.model.Image;
import com.kz.signq.service.ImageService;
import com.kz.signq.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageDb db;

    @Autowired
    public ImageServiceImpl(ImageDb db) {
        this.db = db;
    }

    @Override
    public Optional<Image> findImageByName(String fileName) {
        return db.findImageByName(fileName);
    }

    @Override
    public EntityIdDto saveImage(ImageDto imageDto) {
        imageDto = imageDto.withImageData(ImageUtils.compressImage(imageDto.getImageData()));
        return EntityIdDto.fromBaseEntity(
                db.save(imageDto.toImage())
        );
    }

    @Override
    public Optional<Image> findById(UUID id) {
        return db.findById(id);
    }
}
