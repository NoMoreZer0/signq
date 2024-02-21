package com.kz.signq.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PetitionDto {

    @JsonProperty("title")
    private String title;

//    @JsonProperty("image_id")
//    private UUID imageId;

    @JsonProperty("body")
    private String body;

    @JsonProperty("agency")
    private String agency;
}
