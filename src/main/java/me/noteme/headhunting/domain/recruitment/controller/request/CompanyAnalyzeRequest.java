package me.noteme.headhunting.domain.recruitment.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CompanyAnalyzeRequest {

    @NotEmpty(message = "빈 문자열입니다.")
    private String companyName;
}
