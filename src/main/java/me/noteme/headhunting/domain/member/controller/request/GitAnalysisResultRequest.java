package me.noteme.headhunting.domain.member.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import me.noteme.headhunting.domain.member.dto.AnalysisSummary;

@Data
public class GitAnalysisResultRequest {
    @NotEmpty(message = "email은 필수 값 입니다.")
    private String memberId;

    @NotEmpty(message = "분석한 리포지토리 이름이 작성되지 않았습니다.")
    private String repositoryName;

    @NotNull(message = "분석 결과가 작성되지 않았습니다.")
    private AnalysisSummary analysisSummary;
}
