package me.noteme.headhunting.domain.resume.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.job.entity.Job;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.resume.entity.Resume;
import me.noteme.headhunting.domain.resume.entity.ResumeBasic;
import me.noteme.headhunting.domain.resume.repository.ResumeBasicRepository;
import me.noteme.headhunting.domain.resume.repository.ResumeRepository;
import me.noteme.headhunting.domain.resume.utils.PDFToTextConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final PDFToTextConverter pdfConverter;
    private final EntityManager em;
    private final ResumeBasicRepository resumeBasicRepository;
    private final ResumeRepository resumeRepository;

    public String getText(MultipartFile pdfFile) {
        String pdfFileName = pdfFile.getOriginalFilename();
        if (pdfFileName == null || !pdfFileName.toLowerCase().endsWith(".pdf") || !"application/pdf".equals(pdfFile.getContentType())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "PDF 파일이 아닙니다.");
        }
        return pdfConverter.convertPdfToText(pdfFile);
    }

    @Transactional
    public void saveResumeBasic(Long resumeId, Long jobId, String profile, String title, String name, String email, LocalDate birth, String phone, String introduce, String portfolioUrl, Long resumeBasicId) {
        Job job = getJobById(jobId);
        Resume resume = getResumeById(resumeId);

        ResumeBasic resumeBasic = ResumeBasic.of(resumeBasicId, title, name, email, birth, phone, introduce, portfolioUrl, resume, job, profile);

        resumeBasicRepository.save(resumeBasic);
    }

    @Transactional
    public void resumeInit(long memberId) {
        Resume resume = Resume.builder()
                .member(getMemberById(memberId))
                .build();
        resumeRepository.save(resume);
    }

    private Job getJobById(Long jobId) {
        return em.getReference(Job.class, jobId);
    }

    private Resume getResumeById(Long resumeId) {
        return em.getReference(Resume.class, resumeId);
    }

    private Member getMemberById(Long memberId) {
        return em.getReference(Member.class, memberId);
    }
}
