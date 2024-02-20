package com.kz.signq.service;

import com.kz.signq.dto.EntityIdDto;
import com.kz.signq.dto.ImageDto;
import com.kz.signq.model.Image;

import java.util.Optional;
import java.util.UUID;

public interface ImageService {

    Optional<Image> findImageByName(String fileName);

    EntityIdDto saveImage(ImageDto imageDto);

    Optional<Image> findById(UUID id);
}
