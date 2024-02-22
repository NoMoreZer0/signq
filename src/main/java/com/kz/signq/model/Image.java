package com.kz.signq.model;

import com.kz.signq.dto.image.ImageDto;
import com.kz.signq.model.base.BaseEntityAudit;
import com.kz.signq.utils.ImageUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Table(name = "image")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image extends BaseEntityAudit {

    private String name;

    private String type;

    @Lob
    @Column(name = "image_data")
    private byte[] imageData;

    public ImageDto toDto() {
        return ImageDto.builder()
                .imageData(ImageUtils.decompressImage(imageData))
                .type(type)
                .name(name)
                .build();
    }
}