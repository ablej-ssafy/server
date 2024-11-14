package me.noteme.headhunting.common.listener.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.member.dto.AnalysisSummary;

@Data
@AllArgsConstructor(staticName = "of")
public class GithubAnalysisEvent {
    private String email;
    private String nickname;
    private String repositoryName;
    private AnalysisSummary analysisSummary;
}
