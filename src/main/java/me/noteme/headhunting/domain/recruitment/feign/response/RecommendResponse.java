package me.noteme.headhunting.domain.recruitment.feign.response;

import lombok.Data;

@Data
public class RecommendResponse {
    private int jobPostingId;
    private String jobPostingTitle;
    private String jobPostingContent;
    private String company;
    private String companyLocation;
    private String careerYear;
    private String jobTitle;
    private Integer jobTitleId;
}
