package me.noteme.headhunting.domain.recruitment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.recruitment.entity.Recruitment;
import me.noteme.headhunting.domain.recruitment.entity.RecruitmentImage;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class RecruitmentResponse {
    private Long recruitmentId;
    private String name;
    private CategoryResponse category;
    private List<CategoryResponse> childCategories;
    private List<String> images;
    private CompanyResponse company;
    private String intro;
    private String task;
    private String requirement;
    private String preference;
    private String benefit;
    private String hireRound;
    private LocalDate dueTime;
    private int annualTo;
    private int annualFrom;

    public static RecruitmentResponse fromEntity(Recruitment recruitment) {
        return RecruitmentResponse.of(
                recruitment.getId(),
                recruitment.getName(),
                CategoryResponse.fromEntity(recruitment.getCategory()),
                recruitment.getChildCategories().stream().map(CategoryResponse::fromEntity).toList(),
//                recruitment.getImages().stream().map(RecruitmentImage::getImageUrl).toList(),
                recruitment.getImages().stream().map(i -> "쓰지 말라고 했지").toList(),
                CompanyResponse.fromEntity(recruitment.getCompany()),
                recruitment.getIntro(),
                recruitment.getTask(),
                recruitment.getRequirement(),
                recruitment.getPreference(),
                recruitment.getBenefit(),
                recruitment.getHireRound(),
                recruitment.getDueTime(),
                recruitment.getAnnualTo(),
                recruitment.getAnnualFrom()
        );
    }
}
