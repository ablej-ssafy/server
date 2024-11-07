package me.noteme.headhunting.domain.recruitment.feign.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class CompanyInfoRequest {
    @JsonProperty("company_name")
    private String companyName;
}
