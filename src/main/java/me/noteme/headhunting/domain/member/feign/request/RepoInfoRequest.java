package me.noteme.headhunting.domain.member.feign.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class RepoInfoRequest {
    @JsonProperty("request_id")
    @JsonAlias("requestId")
    private String id;
    @NotEmpty(message = "owner 값은 필수입니다.")
    private String owner;
    @NotEmpty(message = "repo 값은 필수입니다.")
    private String repo;
    @NotEmpty(message = "branch 값은 필수입니다.")
    private String branch;
    private String token;
    private String email;
}

