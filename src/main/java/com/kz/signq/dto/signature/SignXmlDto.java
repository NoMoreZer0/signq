package com.kz.signq.dto.signature;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class SignXmlDto {
    @JsonProperty("petition_id")
    private UUID petitionId;

    @JsonProperty("xml")
    private String xml;
}
