package me.noteme.headhunting.domain.recruitment.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

@Data
public class CompanyAnalyzeRequest {
    @NotEmpty
    private String companyName;
}
