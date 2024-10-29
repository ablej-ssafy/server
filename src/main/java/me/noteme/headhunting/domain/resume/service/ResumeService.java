package me.noteme.headhunting.domain.resume.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.service.StorageService;
import me.noteme.headhunting.domain.job.entity.Job;
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
    private final ResumeRepository resumeRepository;
    private final EntityManager em;
    private final StorageService storageService;
    private final ResumeBasicRepository resumeBasicRepository;

    public String getText(MultipartFile pdfFile) {
        String pdfFileName = pdfFile.getOriginalFilename();
        if(pdfFileName == null || !pdfFileName.toLowerCase().endsWith(".pdf") || !"application/pdf".equals(pdfFile.getContentType())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "PDF 파일이 아닙니다.");
        }
        return pdfConverter.convertPdfToText(pdfFile);
    }

    @Transactional
    public void saveResumeBasic(Long resumeId, Long jobId, MultipartFile profileFile, String title, String name, String email, LocalDate birth, String phone, String introduce, String portfolioUrl, Long resumeBasicId) {
        Job job = getJobById(jobId);
        Resume resume = getResumeById(resumeId);

        // TODO: profile 이미지가 존재한다면, 저장 후 UUID 활용한 profileUrl 찾아오기,
        //                  default image url 지정 or null 저장 방식 정하기
        String profile = "image/url";
//        if(profileFile != null)
//        storageService.uploadFile(memberId, profileFile.getOriginalFilename(), profileFile);
//        String profile = storageService.getFileUrl(memberId, profileFile.getOriginalFilename());

        ResumeBasic resumeBasic = generateResumeBasic(resumeBasicId, title, name, email, birth, phone, introduce, portfolioUrl, resume, job, profile);

        resumeBasicRepository.save(resumeBasic);
    }

    private Job getJobById(Long jobId) {
        return em.getReference(Job.class, jobId);
    }

    private Resume getResumeById(Long resumeId) {
        return em.getReference(Resume.class, resumeId);
    }

    private static ResumeBasic generateResumeBasic(Long id, String title, String name, String email, LocalDate birth, String phone, String introduce, String portfolioUrl, Resume resume, Job job, String profile) {
        return ResumeBasic.builder()
                .id(id)
                .resume(resume)
                .job(job)
                .title(title)
                .profileImage(profile)
                .name(name)
                .email(email)
                .birth(birth)
                .phone(phone)
                .introduce(introduce)
                .portfolioUrl(portfolioUrl)
                .build();
    }

}
