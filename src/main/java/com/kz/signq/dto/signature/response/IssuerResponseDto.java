package com.kz.signq.dto.signature.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IssuerResponseDto {
    @JsonProperty("commonName")
    private String commonName;

    @JsonProperty("country")
    private String country;

    @JsonProperty("dn")
    private String dn;
}
