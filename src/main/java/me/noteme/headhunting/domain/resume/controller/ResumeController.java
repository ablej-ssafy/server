package me.noteme.headhunting.domain.resume.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.common.service.StorageService;
import me.noteme.headhunting.domain.resume.service.ResumeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/resume")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;
    private final StorageService storageService;

    /**
     * PDF 파일을 텍스트로 변환합니다.
     *
     * @param pdfFile PDF 파일
     * @return 변환된 텍스트
     */
    @PostMapping("/convert")
    public SuccessResponse<String> pdfToText(@RequestPart(name = "file", required = false) MultipartFile pdfFile) {
        if (Objects.isNull(pdfFile)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

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
    public SuccessResponse<Void> uploadPDF(@RequestPart(name = "file", required = false) MultipartFile pdfFile) {
        if (Objects.isNull(pdfFile)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        return SuccessResponse.empty();
    }

    /**
     * PDF 파일을 다운로드합니다.
     *
     * @param fileName 파일 이름
     * @return 파일 URL
     */
    @GetMapping("/pdf/{fileName}")
    public SuccessResponse<String> downloadPDF(@PathVariable(name = "fileName") String fileName) {
        return SuccessResponse.of(
                storageService.getFileUrl(fileName)
        );
    }

}
