package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.controller.request.CertificationForm;
import me.noteme.headhunting.domain.resume.controller.request.CertificationRequest;
import me.noteme.headhunting.domain.resume.dto.CertificationResponse;
import me.noteme.headhunting.domain.resume.entity.CertificationType;
import me.noteme.headhunting.domain.resume.service.CertificationService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
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
            @Validated @RequestBody CertificationRequest request,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, errors);
        }

        certificationService.saveAllCertifications(
                request.getCertifications()
        );

        return SuccessResponse.empty();
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse<CertificationResponse> getCertification(
            @LoginUser Long userId
    ) {
        CertificationResponse response = certificationService.getCertifications(userId);

        return SuccessResponse.of(response);
    }

    @GetMapping("/language")
    public SuccessResponse<CertificationResponse> getLanguageCertifications(
            @LoginUser Long userId
    ) {
        CertificationResponse response = certificationService.findLanguageCertifications(userId, CertificationType.LANGUAGE);

        return SuccessResponse.of(response);
    }

    @GetMapping("/qualification")
    public SuccessResponse<CertificationResponse> getQualificationCertifications(
            @LoginUser Long userId
    ) {
        CertificationResponse response = certificationService.findLanguageCertifications(userId, CertificationType.QUALIFICATION);

        return SuccessResponse.of(response);
    }

    @GetMapping("/{certificationId}")
    public SuccessResponse<CertificationForm> getCertificationById(
            @PathVariable("certificationId") Long certificationId
    ) {
        CertificationForm response = certificationService.findCertification(certificationId);

        return SuccessResponse.of(response);
    }
}
