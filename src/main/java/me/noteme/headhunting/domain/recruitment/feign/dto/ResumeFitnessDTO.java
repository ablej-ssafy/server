package me.noteme.headhunting.domain.recruitment.feign.dto;

import lombok.Data;

@Data
public class ResumeFitnessDTO {
    private String resumeId;
    private long successPossibility;
}
