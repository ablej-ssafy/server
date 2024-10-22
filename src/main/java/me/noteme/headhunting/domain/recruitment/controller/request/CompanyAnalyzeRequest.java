package me.noteme.headhunting.domain.recruitment.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CompanyAnalyzeRequest {

    @NotEmpty(message = "유효하지 않은 입력값입니다.")
    private String companyName;
}
