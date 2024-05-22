package com.kz.signq.model;

import com.kz.signq.model.base.BaseEntityAudit;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "digital_signature_xml")
public class DigitalSignatureXml extends BaseEntityAudit {

    @Column(name = "petition_id")
    private UUID petitionId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "signature_time")
    private Date dateTime;

    @Column(name = "user_iin")
    private String userIin;

    @Lob
    @Column(name = "signature", unique = true)
    private String signature;
}
