package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.controller.request.ResumeBasicRequest;
import me.noteme.headhunting.domain.resume.dto.ResumeBasicResponse;
import me.noteme.headhunting.domain.resume.dto.ResumeResponse;
import me.noteme.headhunting.domain.resume.dto.ResumePdfResponse;
import me.noteme.headhunting.domain.resume.service.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resume")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;

    @GetMapping("/download/{resumePdfId}")
    public SuccessResponse<String> download(@LoginUser Long userId, @PathVariable Long resumePdfId) {
        return SuccessResponse.of(resumeService.download(userId, resumePdfId));
    }
  
    @GetMapping("")
    public SuccessResponse<ResumeResponse> getResumeInfo(@LoginUser Long memberId) {
        ResumeResponse response = resumeService.getResume(memberId);

        return SuccessResponse.of(response);
    }

    @GetMapping("/pdf")
    public SuccessResponse<List<ResumePdfResponse>> getPdfList(@LoginUser Long memberId) {
        return SuccessResponse.of(resumeService.getPdfList(memberId));
    }

    @GetMapping("/basic")
    public SuccessResponse<ResumeBasicResponse> getBasic(@LoginUser Long memberId) {
        return SuccessResponse.of(resumeService.getBasicInfo(memberId));
    }

    // TODO: 테스트 용도
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> postResume() {
        // TODO: 로그인 된 사용자 이력서 저장
        long memberId = 1L;

        resumeService.resumeInit(memberId);

        return SuccessResponse.empty();
    }

    /**
     * 이력서를 작성합니다.
     */
    @PostMapping("/basic")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> postResumeBase(
            @Validated @RequestBody ResumeBasicRequest request,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, errors);
        }

        // TODO: Profile 업로드 로직 분리
        resumeService.saveResumeBasic(
                request.getResumeId(),
                request.getJobId(),
                request.getProfile(),
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
    @Deprecated
    @PostMapping("/convert")
    public SuccessResponse<String> pdfToText(@RequestPart(name = "file") MultipartFile pdfFile) {
        return SuccessResponse.of(resumeService.getText(pdfFile));
    }
}
