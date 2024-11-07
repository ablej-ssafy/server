package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.controller.request.CertificationForm;
import me.noteme.headhunting.domain.resume.controller.request.CertificationRequest;
import me.noteme.headhunting.domain.resume.dto.CertificationResponse;
import me.noteme.headhunting.domain.resume.service.CertificationService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/certification")
@RequiredArgsConstructor
public class CertificationController {
    private final CertificationService certificationService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> postCertification(
            @LoginUser Long memberId,
            @Validated @RequestBody CertificationRequest request
    ) {
        certificationService.saveAllCertifications(
                memberId,
                request.getCertifications()
        );

        return SuccessResponse.empty();
    }

    @PostMapping("/add/type/{type}")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<CertificationForm> createEmptyCertification(
            @LoginUser Long memberId,
            @PathVariable("type") String type
    ) {
        return SuccessResponse.of(certificationService.createEmptyCertification(memberId, type));
    }

    @GetMapping("")
    public SuccessResponse<CertificationResponse> getCertifications(
            @RequestParam(name = "type", required = false) String type,
            @LoginUser Long memberId
    ) {
        return SuccessResponse.of(certificationService.getCertifications(memberId, type));
    }

    @DeleteMapping("/{certificationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertification(@PathVariable("certificationId") Long certificationId) {
        certificationService.deleteById(certificationId);
    }
}
