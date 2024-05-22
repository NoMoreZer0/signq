package com.kz.signq.dto.signature.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SubjectResponseDto {
    @JsonProperty("commonName")
    private String commonName;

    @JsonProperty("surName")
    private String surName;

    @JsonProperty("iin")
    private String iin;

    @JsonProperty("country")
    private String country;

    @JsonProperty("dn")
    private String dn;
}
