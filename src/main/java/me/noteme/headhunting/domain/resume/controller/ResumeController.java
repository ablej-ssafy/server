package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.service.ResumeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/resume")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;

    @GetMapping("/download/{resumePdfId}")
    public SuccessResponse<String> download(
            @LoginUser Long userId,
            @PathVariable Long resumePdfId
    ) {
        return SuccessResponse.of(
                resumeService.download(userId,resumePdfId)
        );
    }

    @Deprecated
    @PostMapping("/convert")
    public SuccessResponse<String> pdfToText(@RequestPart(name = "file") MultipartFile pdfFile) {
        return SuccessResponse.of(resumeService.getText(pdfFile));
    }

    @Deprecated
    @PostMapping("/pdf")
    public SuccessResponse<Void> uploadPDF(@RequestPart(name = "file") MultipartFile pdfFile) {
        return SuccessResponse.empty();
    }
}
