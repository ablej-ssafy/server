package me.noteme.headhunting.common.listener.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class GithubAnalysisEvent {
    private String email;
    private String repositoryName;
    private String analysisSummary;
}
