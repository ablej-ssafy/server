package me.noteme.headhunting.domain.resume.service;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.resume.utils.PDFToTextConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final PDFToTextConverter pdfConverter;

    public String getText(MultipartFile pdfFile) {
        String pdfFileName = pdfFile.getOriginalFilename();
        if(pdfFileName == null || !pdfFileName.toLowerCase().endsWith(".pdf") || !"application/pdf".equals(pdfFile.getContentType())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "PDF 파일이 아닙니다.");
        }
        return pdfConverter.convertPdfToText(pdfFile);
    }

}
