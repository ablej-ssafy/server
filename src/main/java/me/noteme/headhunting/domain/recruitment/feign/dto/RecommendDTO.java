package me.noteme.headhunting.domain.recruitment.feign.dto;

import lombok.Data;

@Data
public class RecommendDTO {
    private int jobPostingId;
    private String jobPostingTitle;
    private String jobPostingContents;
    private String company;
    private String companyLocation;
    private String careerYear;
    private String jobTitle;
    private boolean isClose;
    private long successPossibility;
}
