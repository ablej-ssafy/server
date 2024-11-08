package me.noteme.headhunting.domain.recruitment.feign.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class AiRecommendResponse {
    private long id;
    private double similarity;
}
