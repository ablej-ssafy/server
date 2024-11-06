package me.noteme.headhunting.domain.recruitment.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.recruitment.dto.CompanyWithRecruitmentResponse;
import me.noteme.headhunting.domain.recruitment.service.CompanyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/company")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/{companyId}")
    public SuccessResponse<CompanyWithRecruitmentResponse> getCompanyById(
            @LoginUser Long memberId,
            @PathVariable(name = "companyId") Long companyId
    ) {
        return SuccessResponse.of(
                companyService.getCompanyById(memberId, companyId)
        );
    }
}
