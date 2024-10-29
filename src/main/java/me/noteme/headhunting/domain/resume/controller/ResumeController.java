package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.common.service.StorageService;
import me.noteme.headhunting.domain.resume.controller.request.ResumeBasicRequest;
import me.noteme.headhunting.domain.resume.service.ResumeService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/resume")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;
    private final StorageService storageService;
    private final ApplicationEventPublisher publisher;

    /**
     * 이력서를 작성합니다.
     */
    @PostMapping("/basic")
    public SuccessResponse<Void> postResumeBase(
            @RequestPart("file") MultipartFile profile,
            @Validated @RequestBody ResumeBasicRequest request,
            Errors errors) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, errors);
        }

        resumeService.saveResumeBasic(
                request.getResumeId(),
                request.getJobId(),
                profile,
                request.getTitle(),
                request.getName(),
                request.getEmail(),
                request.getBirth(),
                request.getPhone(),
                request.getIntroduce(),
                request.getPortfolioUrl(),
                request.getResumeBasicId()
        );

        return SuccessResponse.empty();
    }

    /**
     * PDF 파일을 텍스트로 변환합니다.
     *
     * @param pdfFile PDF 파일
     * @return 변환된 텍스트
     */
    @PostMapping("/convert")
    public SuccessResponse<String> pdfToText(@RequestPart(name = "file") MultipartFile pdfFile) {
        return SuccessResponse.of(
                resumeService.getText(pdfFile)
        );
    }

    /**
     * PDF 파일을 업로드합니다.
     *
     * @param pdfFile PDF 파일
     * @return 성공 응답
     */
    @PostMapping("/pdf")
    public SuccessResponse<Void> uploadPDF(@RequestPart(name = "file") MultipartFile pdfFile) {
        return SuccessResponse.empty();
    }
}
