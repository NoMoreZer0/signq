package com.kz.signq.dto.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kz.signq.model.Image;
import com.kz.signq.utils.ImageUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;

@Builder
@Getter
public class ImageDto implements Serializable {

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    @With
    @JsonProperty("image_data")
    private byte[] imageData;

    public Image toImage() {
        return Image.builder()
                .name(name)
                .type(type)
                .imageData(imageData)
                .build();
    }

    public static ImageDto fromMultipartFile(MultipartFile file) throws IOException {
        return ImageDto.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes()))
                .build();
    }
}
