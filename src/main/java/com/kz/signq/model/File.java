package com.kz.signq.model;

import com.kz.signq.model.base.BaseEntityAudit;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "file")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File extends BaseEntityAudit {

    private String name;

    private String type;

    private String filePath;
}
