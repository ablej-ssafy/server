package me.noteme.headhunting.domain.recruitment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.recruitment.entity.Recruitment;

import java.time.LocalDate;

@Data
@AllArgsConstructor(staticName = "of")
public class CompanyRecruitmentResponse {
    private Long recruitmentId;
    private String name;
    private String thumbnail;
    private int annualTo;
    private int annualFrom;
    private LocalDate dueTime;
    private boolean isScrapped;

    public static CompanyRecruitmentResponse fromEntity(Recruitment recruitment) {
        return CompanyRecruitmentResponse.of(
                recruitment.getId(),
                recruitment.getName(),
                recruitment.getThumbnail(),
                recruitment.getAnnualTo(),
                recruitment.getAnnualFrom(),
                recruitment.getDueTime(),
                false
        );
    }

    public static CompanyRecruitmentResponse fromEntity(Recruitment recruitment, boolean isScrapped) {
        return CompanyRecruitmentResponse.of(
                recruitment.getId(),
                recruitment.getName(),
                recruitment.getThumbnail(),
                recruitment.getAnnualTo(),
                recruitment.getAnnualFrom(),
                recruitment.getDueTime(),
                isScrapped
        );
    }
}
