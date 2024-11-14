package me.noteme.headhunting.domain.member.feign.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GitAccessResponse {
    @JsonProperty("access_token")
    @JsonAlias("accessToken")
    private String accessToken;
    private String scope;
    @JsonProperty("token_type")
    @JsonAlias("tokenType")
    private String tokenType;
}