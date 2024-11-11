package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.common.service.StorageService;
import me.noteme.headhunting.domain.recruitment.dto.RecommendResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
import me.noteme.headhunting.domain.resume.controller.request.ResumeBasicRequest;
import me.noteme.headhunting.domain.resume.dto.OpenAiResponse;
import me.noteme.headhunting.domain.resume.dto.ResumeBasicResponse;
import me.noteme.headhunting.domain.resume.dto.ResumeResponse;
import me.noteme.headhunting.domain.resume.dto.ResumePdfResponse;
import me.noteme.headhunting.domain.resume.service.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resume")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;
    private final StorageService storageService;

    @GetMapping("/download/{resumePdfId}")
    public SuccessResponse<String> download(@LoginUser Long userId, @PathVariable Long resumePdfId) {
        return SuccessResponse.of(resumeService.download(userId, resumePdfId));
    }

    @GetMapping("")
    public SuccessResponse<ResumeResponse> getResumeInfo(@LoginUser Long memberId) {
        return SuccessResponse.of(resumeService.getResume(memberId));
    }

    @PostMapping("/pdf")
    public SuccessResponse<List<RecommendResponse>> uploadResumePdf(@LoginUser Long memberId, @RequestPart("file") MultipartFile resumePdf) {
        return SuccessResponse.of(resumeService.upload(memberId, resumePdf));
    }

    @GetMapping("/pdf")
    public SuccessResponse<List<ResumePdfResponse>> getPdfList(@LoginUser Long memberId) {
        return SuccessResponse.of(resumeService.getPdfList(memberId));
    }

    @DeleteMapping("/pdf/{resumePdfId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResumePdf(@LoginUser Long memberId, @PathVariable Long resumePdfId) {
        resumeService.delete(memberId, resumePdfId);
    }

    @GetMapping("/basic")
    public SuccessResponse<ResumeBasicResponse> getBasic(@LoginUser Long memberId) {
        return SuccessResponse.of(resumeService.getBasicInfo(memberId));
    }

    // TODO: 테스트 용도
    @PostMapping("/basic/test")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> postResume(@LoginUser Long memberId) {
        resumeService.resumeInit(memberId);

        return SuccessResponse.empty();
    }

    /**
     * 이력서를 작성합니다.
     */
    @PostMapping("/basic")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> postResumeBase(
            @LoginUser Long memberId,
            @Validated @RequestBody ResumeBasicRequest request
    ) {
        resumeService.saveResumeBasic(
                memberId,
                request.getJob(),
                request.getProfile(),
                request.getTitle(),
                request.getName(),
                request.getEmail(),
                request.getBirth(),
                request.getPhone(),
                request.getIntroduce(),
                request.getPortfolioUrl()
        );

        return SuccessResponse.empty();
    }

    @PostMapping("/basic/profile")
    public SuccessResponse<String> uploadProfile(
            @LoginUser Long memberId,
            @RequestParam("file") MultipartFile profile
    ) {
        storageService.uploadFile(memberId, profile.getOriginalFilename(), profile);

        return SuccessResponse.of(storageService.getFileUrl(memberId, profile.getOriginalFilename()));
    }

    // TODO : 이력서 자동완성 TEST
    @GetMapping("/auto")
    public SuccessResponse<OpenAiResponse> test(@RequestBody String resume) {
        return SuccessResponse.of(resumeService.auto(resume));
    }
}
