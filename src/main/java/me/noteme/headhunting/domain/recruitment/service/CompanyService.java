package me.noteme.headhunting.domain.recruitment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.recruitment.dto.CompanyRecruitmentResponse;
import me.noteme.headhunting.domain.recruitment.dto.CompanyResponse;
import me.noteme.headhunting.domain.recruitment.dto.CompanyWithRecruitmentResponse;
import me.noteme.headhunting.domain.recruitment.entity.Company;
import me.noteme.headhunting.domain.recruitment.repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {
    private final CompanyRepository companyRepository;

    public Page<CompanyResponse> searchCompanies(String type, String query, Pageable pageable) {
        Page<Company> companies = companyRepository.searchCompanies(type, query, pageable);
        return companies.map(CompanyResponse::fromEntity);
    }

    public CompanyWithRecruitmentResponse getCompanyById(Long companyId) {
        Company company = companyRepository.findCompanyById(companyId).orElseThrow(
                () -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        return CompanyWithRecruitmentResponse.fromEntity(company);
    }
}
