package com.kz.signq.dto.signature.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignerResponseDto {
    @JsonProperty("valid")
    private boolean valid;

    @JsonProperty("subject")
    private SubjectResponseDto subject;

    @JsonProperty("issuer")
    private IssuerResponseDto issuer;

    @JsonProperty("signature")
    private String signature;
}
