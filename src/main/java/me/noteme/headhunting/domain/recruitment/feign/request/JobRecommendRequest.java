package me.noteme.headhunting.domain.recruitment.feign.request;

import lombok.Data;

@Data
public class JobRecommendRequest {
    private String resume;
    private int jobTitleId;
    private String jobTitle;
    private int careerYear;
}
