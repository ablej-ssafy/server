package me.noteme.headhunting.domain.recruitment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.recruitment.entity.Recruitment;

@Data
@AllArgsConstructor(staticName = "of")
public class RecruitmentSummaryResponse {
    private Long recruitmentId;
    private String name;
    private String thumbnail;
    private Long companyId;
    private String companyName;
    private String location;
    private String strict;
    private String category;
    private boolean isScrapped;

    public static RecruitmentSummaryResponse fromEntity(Recruitment recruitment) {
        return RecruitmentSummaryResponse.of(
                recruitment.getId(),
                recruitment.getName(),
                recruitment.getThumbnail(),
                recruitment.getCompany().getId(),
                recruitment.getCompany().getName(),
                recruitment.getCompany().getLocation(),
                recruitment.getCompany().getStrict(),
                recruitment.getCategory().getName(),
                false
        );
    }

    public static RecruitmentSummaryResponse fromEntity(Recruitment recruitment, boolean isScrapped) {
        return RecruitmentSummaryResponse.of(
                recruitment.getId(),
                recruitment.getName(),
                recruitment.getThumbnail(),
                recruitment.getCompany().getId(),
                recruitment.getCompany().getName(),
                recruitment.getCompany().getLocation(),
                recruitment.getCompany().getStrict(),
                recruitment.getCategory().getName(),
                isScrapped
        );
    }
}
