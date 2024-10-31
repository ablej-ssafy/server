package me.noteme.headhunting.domain.resume.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.service.StorageService;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.member.repository.MemberRepository;
import me.noteme.headhunting.domain.resume.entity.ResumePdf;
import me.noteme.headhunting.domain.resume.repository.ResumePdfRepository;
import me.noteme.headhunting.domain.resume.utils.PDFToTextConverter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResumeService {
    private final ResumePdfRepository resumePdfRepository;
    private final PDFToTextConverter pdfConverter;
    private final StorageService storageService;
    private final EntityManager em;

    public String download(Long userId, Long resumePdfId) {
        ResumePdf resumePdf = resumePdfRepository.findById(resumePdfId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        String key = resumePdf.getKey() + ".pdf";

        return storageService.getFileUrl(userId, key);
    }

    @Transactional
    public void savePdf(Long userId, String fileName, String key) {
        Member member = em.getReference(Member.class, userId);
        ResumePdf resumePdf = ResumePdf.of(member, fileName, key);

        resumePdfRepository.save(resumePdf);
    }

    public String getText(MultipartFile pdfFile) {
        String pdfFileName = pdfFile.getOriginalFilename();
        if (pdfFileName == null || !pdfFileName.toLowerCase().endsWith(".pdf") || !"application/pdf".equals(pdfFile.getContentType())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "PDF 파일이 아닙니다.");
        }
        return pdfConverter.convertPdfToText(pdfFile);
    }
}
