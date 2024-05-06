package com.kz.signq.model;

import com.kz.signq.dto.petition.PetitionDto;
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
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private File file;

    @Lob
    private String body;

    private String agency;

    @Enumerated(value = EnumType.STRING)
    private PetitionStatus status;

    public void updateByDto(PetitionDto petitionDto, @Nullable File file) {
        if (petitionDto.getBody() != null) {
            this.body = petitionDto.getBody();
        }
        if (petitionDto.getAgency() != null) {
            this.agency = petitionDto.getAgency();
        }
        if (petitionDto.getTitle() != null) {
            this.title = petitionDto.getTitle();
        }
        if (file != null) {
            this.file = file;
        }
    }
}
