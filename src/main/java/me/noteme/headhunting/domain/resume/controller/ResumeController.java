package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.service.ResumeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/resume")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;

    @GetMapping("/pdf-to-text")
    public SuccessResponse<String> pdfToText(MultipartFile pdfFile) {
        return SuccessResponse.of(
                resumeService.getText(pdfFile)
        );
    }

}
