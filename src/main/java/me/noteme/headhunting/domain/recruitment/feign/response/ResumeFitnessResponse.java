package me.noteme.headhunting.domain.recruitment.feign.response;

import lombok.Data;

@Data
public class ResumeFitnessResponse {
    private String resumeId;
    private long successPossibility;
}
