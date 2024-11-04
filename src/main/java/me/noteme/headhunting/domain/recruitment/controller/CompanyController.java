package me.noteme.headhunting.domain.recruitment.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.recruitment.dto.CompanyRecruitmentResponse;
import me.noteme.headhunting.domain.recruitment.dto.CompanyResponse;
import me.noteme.headhunting.domain.recruitment.dto.CompanyWithRecruitmentResponse;
import me.noteme.headhunting.domain.recruitment.service.CompanyService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/company")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public SuccessResponse<PagedModel<CompanyResponse>> searchCompanies(
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "q", required = false) String query,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        if (type == null) {
            type = "all";
        }

        return SuccessResponse.of(
                new PagedModel<>(companyService.searchCompanies(type, query, pageable))
        );
    }

    @GetMapping("/{companyId}")
    public SuccessResponse<CompanyWithRecruitmentResponse> getCompanyById(
            @PathVariable(name = "companyId") Long companyId
    ) {
        return SuccessResponse.of(
                companyService.getCompanyById(companyId)
        );
    }
}
