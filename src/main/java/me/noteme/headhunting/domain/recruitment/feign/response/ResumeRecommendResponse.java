package me.noteme.headhunting.domain.recruitment.feign.response;

import lombok.Data;
import me.noteme.headhunting.domain.recruitment.feign.dto.RecommendDTO;

@Data
public class ResumeRecommendResponse {
    private boolean success;
    private RecommendDTO recommend;
}
