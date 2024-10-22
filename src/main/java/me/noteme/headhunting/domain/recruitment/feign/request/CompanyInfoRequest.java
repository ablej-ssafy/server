package me.noteme.headhunting.domain.recruitment.feign.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CompanyInfoRequest {
    @JsonProperty("company_name")
    private String companyName;
}
