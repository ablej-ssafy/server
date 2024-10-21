package me.noteme.headhunting.domain.recruitment.feign.response;

import lombok.Data;

@Data
public class CompanyInfoResponse {
    boolean success;
    String companyReport;
}
