package me.noteme.headhunting.domain.recruitment.feign.response;

import lombok.Data;

@Data
public class CompanyInfoResponse {
    private boolean success;
    private String companyReport;
}
