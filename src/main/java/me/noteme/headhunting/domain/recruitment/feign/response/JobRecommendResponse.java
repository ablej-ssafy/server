package me.noteme.headhunting.domain.recruitment.feign.response;

import lombok.Data;
import me.noteme.headhunting.domain.recruitment.feign.dto.ResumeFitnessDTO;

@Data
public class JobRecommendResponse {
    private boolean success;
    private ResumeFitnessDTO resumeFitness;
}
