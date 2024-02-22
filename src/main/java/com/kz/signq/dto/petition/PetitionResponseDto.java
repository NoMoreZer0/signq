package com.kz.signq.dto.petition;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kz.signq.model.Petition;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PetitionResponseDto {
    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    @JsonProperty("agency")
    private String agency;

    @JsonProperty("is_owner")
    private Boolean isOwner;

    public static PetitionResponseDto fromPetition(Petition petition, boolean isOwner) {
        return PetitionResponseDto.builder()
                .body(petition.getBody())
                .title(petition.getTitle())
                .agency(petition.getAgency())
                .isOwner(isOwner)
                .build();
    }
}
