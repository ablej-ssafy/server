package me.noteme.headhunting.domain.member.feign.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class RepoInfoRequest {
    private String id;
    private String owner;
    private String repo;
    private String branch;
    private String token;
}

