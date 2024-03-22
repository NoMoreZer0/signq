package com.kz.signq.model;

import com.kz.signq.model.base.BaseEntityAudit;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "petition")
public class Petition extends BaseEntityAudit {

    private String title;

    @Nullable
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image img;

    private String body;

    private String agency;
}
