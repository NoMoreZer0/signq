package com.kz.signq.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kz.signq.model.Petition;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PetitionsDto {

    @JsonProperty("petitions")
    private List<Petition> petitions;

}
