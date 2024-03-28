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
@Table(name = "digital_signature")
public class DigitalSignature extends BaseEntityAudit {

    @Column(name = "petition_id")
    private UUID petitionId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "signature_time")
    private Date dateTime;

    @Column(name = "user_iin")
    private String userIin;

    @Column(name = "data", unique = true)
    private String data;

    @Column(name = "certificate")
    private byte[] certificate;

    @Column(name = "signature")
    private byte[] sign;
}
