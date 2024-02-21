package com.kz.signq.model;

import com.kz.signq.model.base.BaseEntityAudit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_petition_sign")
public class UserPetitionSign extends BaseEntityAudit {

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "petition_id")
    Petition petition;

}
