package me.noteme.headhunting.domain.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.resume.entity.ResumeOrder;

@Data
@AllArgsConstructor(staticName = "of")
public class ResumeOrderResponse {
    private double basic;
    private double education;
    private double company;
    private double project;
    private double activity;
    private double qualification;
    private double language;
    private double tech;

    public static ResumeOrderResponse fromEntity(ResumeOrder resumeOrder) {
        return ResumeOrderResponse.of(
                resumeOrder.getBasicOrder(),
                resumeOrder.getEducationOrder(),
                resumeOrder.getCompanyOrder(),
                resumeOrder.getProjectOrder(),
                resumeOrder.getActivityOrder(),
                resumeOrder.getQualificationOrder(),
                resumeOrder.getLanguageOrder(),
                resumeOrder.getTechOrder()
        );
    }
}
