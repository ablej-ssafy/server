package me.noteme.headhunting.domain.resume.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.common.service.StorageService;
import me.noteme.headhunting.domain.resume.service.ResumeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/resume")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;
    private final StorageService storageService;

    @GetMapping("/pdf-to-text")
    public SuccessResponse<String> pdfToText(MultipartFile pdfFile) {
        return SuccessResponse.of(
                resumeService.getText(pdfFile)
        );
    }

    @PostMapping("/upload/pdf")
    public SuccessResponse<String> uploadPDF(MultipartFile pdfFile) {
        return SuccessResponse.of(
                storageService.uploadFile(pdfFile)
        );
    }

    @GetMapping("/pdf/{fileName}")
    public SuccessResponse<String> downloadPDF(@PathVariable String fileName) {
        return SuccessResponse.of(
                storageService.getFileUrl(fileName)
        );
    }

}
