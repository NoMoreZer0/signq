package com.kz.signq.dto.signature;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class VerifyDto {

    @JsonProperty("revocationCheck")
    private List<String> revocationCheck;

    @JsonProperty("xml")
    private String xml;
}
