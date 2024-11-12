package me.noteme.headhunting.domain.member.feign.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class RepoInfoRequest {
    private String id;
    @NotEmpty(message = "owner 값은 필수입니다.")
    private String owner;
    @NotEmpty(message = "repo 값은 필수입니다.")
    private String repo;
    @NotEmpty(message = "branch 값은 필수입니다.")
    private String branch;
    private String token;
}

