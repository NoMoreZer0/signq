package com.kz.signq.dto.eds;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class EdsDto {

    @JsonProperty("petition_id")
    private UUID petitionId;

    @JsonProperty("certificate_store")
    private String certificateStore;

    @JsonProperty("password")
    private String password;
}
