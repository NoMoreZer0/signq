package com.kz.signq.dto.signature.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class VerifyResponseDto {

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("valid")
    private boolean valid;

    @JsonProperty("signers")
    private List<SignerResponseDto> signers;
}
