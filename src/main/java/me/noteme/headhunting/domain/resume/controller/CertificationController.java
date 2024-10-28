package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.controller.request.CertificationRequest;
import me.noteme.headhunting.domain.resume.service.CertificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/certification")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping("/{resume}")
    public SuccessResponse<Void> postCertification(@RequestBody CertificationRequest request) {
        // TODO: MemberId, 이력서 PK 요구사항 검토

        return SuccessResponse.empty();
    }
}
