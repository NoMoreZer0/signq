package com.kz.signq.dto.petition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class PetitionDto {

    @JsonProperty("title")
    private String title;

    @JsonProperty("file_id")
    private UUID fileId;

    @JsonProperty("body")
    private String body;

    @JsonProperty("agency")
    private String agency;
}
