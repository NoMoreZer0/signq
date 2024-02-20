package com.kz.signq.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthenticationDto {

    @JsonProperty("token")
    private String token;

}
