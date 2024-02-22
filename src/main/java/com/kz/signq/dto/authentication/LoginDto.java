package com.kz.signq.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginDto {

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;
}
