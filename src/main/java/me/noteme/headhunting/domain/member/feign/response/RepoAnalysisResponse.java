package me.noteme.headhunting.domain.member.feign.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RepoAnalysisResponse {
    @JsonProperty("request_id")
    @JsonAlias("requestId")
    private String requestId;
    private String state;
    private String step;
    private String result;
}
