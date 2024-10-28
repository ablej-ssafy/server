package me.noteme.headhunting.domain.resume.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.listener.event.FileUploadEvent;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.common.service.StorageService;
import me.noteme.headhunting.domain.resume.controller.request.ResumeRequest;
import me.noteme.headhunting.domain.resume.entity.Resume;
import me.noteme.headhunting.domain.resume.service.ResumeService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

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
    @PostMapping("/")
    public SuccessResponse<Void> postResumeBase(@RequestPart("file") MultipartFile profile, @RequestBody ResumeRequest request) {
        // TODO: member_id 받아서 처리
        long memberId = 1;
        // TODO: job_id 받아서 처리
        long jobId = 1;

        resumeService.saveResume(
                memberId, jobId, profile, request.getTitle(), request.getName(), request.getEmail(), request.getBirth(), request.getPhone(), request.getIntroduce(), request.getPortfolioUrl()
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
