package me.noteme.headhunting.domain.recruitment.feign.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class JobRecommendRequest {
    private String resume;
    private int careerYear;
    private long jobTitleId;
    private int k;
}
