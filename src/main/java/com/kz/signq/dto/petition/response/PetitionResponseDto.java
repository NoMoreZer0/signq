package com.kz.signq.dto.petition.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kz.signq.model.Petition;
import com.kz.signq.model.PetitionStatus;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetitionResponseDto {
    @JsonProperty("id")
    private UUID id;

    @Nullable
    @JsonProperty("title")
    private String title;

    @Nullable
    @JsonProperty("body")
    private String body;

    @Nullable
    @JsonProperty("agency")
    private String agency;

    @Nullable
    @JsonProperty("is_owner")
    private Boolean isOwner;

    @Nullable
    @JsonProperty("status")
    private PetitionStatus status;

    @Nullable
    @JsonProperty("file_id")
    private UUID fileId;

    public static PetitionResponseDto fromPetition(Petition petition, boolean isOwner) {
        return PetitionResponseDto.builder()
                .id(petition.getId())
                .body(petition.getBody())
                .title(petition.getTitle())
                .agency(petition.getAgency())
                .isOwner(isOwner)
                .status(petition.getStatus())
                .fileId(petition.getFile() != null ? petition.getFile().getId() : null)
                .build();
    }
}
