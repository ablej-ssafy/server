package me.noteme.headhunting.domain.resume.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.service.StorageService;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.resume.controller.request.CertificationForm;
import me.noteme.headhunting.domain.resume.controller.request.EducationalForm;
import me.noteme.headhunting.domain.resume.controller.request.ExperienceForm;
import me.noteme.headhunting.domain.resume.dto.*;
import me.noteme.headhunting.domain.resume.entity.*;
import me.noteme.headhunting.domain.resume.dto.ResumeBasicResponse;
import me.noteme.headhunting.domain.resume.dto.ResumePdfResponse;
import me.noteme.headhunting.domain.resume.entity.ResumePdf;
import me.noteme.headhunting.domain.resume.repository.ResumePdfRepository;
import me.noteme.headhunting.domain.job.entity.Job;
import me.noteme.headhunting.domain.resume.repository.ResumeBasicRepository;
import me.noteme.headhunting.domain.resume.repository.ResumeRepository;
import me.noteme.headhunting.domain.resume.utils.PDFToTextConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeBasicRepository resumeBasicRepository;
    private final ResumePdfRepository resumePdfRepository;
    private final ResumeRepository resumeRepository;
    private final PDFToTextConverter pdfConverter;
    private final StorageService storageService;
    private final EntityManager em;

    public String download(Long memberId, Long resumePdfId) {
        ResumePdf resumePdf = resumePdfRepository.findById(resumePdfId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        String key = resumePdf.getKey() + ".pdf";

        return storageService.getFileUrl(memberId, key);
    }

    @Transactional
    public void savePdf(Long memberId, String fileName, String key) {
        Member member = em.getReference(Member.class, memberId);
        ResumePdf resumePdf = ResumePdf.of(member, fileName, key);

        resumePdfRepository.save(resumePdf);
    }

    public List<ResumePdfResponse> getPdfList(Long memberId) {
        List<ResumePdf> resumePdfList = resumePdfRepository.findAllByMemberId(memberId);

        return resumePdfList.stream()
                .map(o -> ResumePdfResponse.of(
                                o.getId(),
                                o.getFileName(),
                                LocalDate.from(o.getCreatedAt())
                )).toList();
    }

    @Transactional
    public void saveResumeBasic(Long resumeId, Long jobId, String profile, String title, String name, String email, LocalDate birth, String phone, String introduce, String portfolioUrl, Long resumeBasicId) {
        Job job = getJobById(jobId);
        Resume resume = getResumeById(resumeId);

        ResumeBasic resumeBasic = ResumeBasic
                .of(resumeBasicId, title, name, email, birth, phone, introduce, portfolioUrl, resume, job, profile);

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

    // TODO: 테스트 용도 추후 삭제
    public String getText(MultipartFile pdfFile) {
        String pdfFileName = pdfFile.getOriginalFilename();
        if (pdfFileName == null || !pdfFileName.toLowerCase().endsWith(".pdf") || !"application/pdf".equals(pdfFile.getContentType())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "PDF 파일이 아닙니다.");
        }
        return pdfConverter.convertPdfToText(pdfFile);
    }

    public ResumeBasicResponse getBasicInfo(Long memberId) {
        return ResumeBasicResponse.fromEntity(resumeBasicRepository.findByMemberId(memberId));
    }

    public ResumeResponse getResume(Long memberId) {
        ResumeBasicResponse basic = ResumeBasicResponse.fromEntity(resumeBasicRepository.findByMemberId(memberId));
        List<Educational> edu = resumeRepository.findAllEducationalByMemberId(memberId);

        List<EducationalForm> educationals = edu.stream()
                .map(EducationalForm::fromEntity)
                .toList();

        List<Experience> experiences = resumeRepository.findAllExperienceByMemberId(memberId);

        List<ExperienceForm> companies = filterByEnum(experiences, ExperienceType.COMPANY, Experience::getExperienceType, ExperienceForm::fromEntity);
        List<ExperienceForm> activities = filterByEnum(experiences, ExperienceType.ACTIVITY, Experience::getExperienceType, ExperienceForm::fromEntity);
        List<ExperienceForm> projects = filterByEnum(experiences, ExperienceType.PROJECT, Experience::getExperienceType, ExperienceForm::fromEntity);

        List<Certification> certifications = resumeRepository.findAllCertificationsByMemberId(memberId);

        List<CertificationForm> languages = filterByEnum(certifications, CertificationType.LANGUAGE, Certification::getCertificationType, CertificationForm::fromEntity);
        List<CertificationForm> qualifications = filterByEnum(certifications, CertificationType.QUALIFICATION, Certification::getCertificationType, CertificationForm::fromEntity);

        TechStack techStack = resumeRepository.findTechByMemberId(memberId);
        TechResponse tech = TechResponse.fromEntity(techStack);

        return ResumeResponse.of(basic, educationals, companies, activities, projects, languages, qualifications, tech);
    }

    private <T, R, E extends Enum<E>> List<R> filterByEnum(
            List<T> items,
            E enumValue,
            Function<T, E> enumExtractor,
            Function<T, R> mapper
    ) {
        return items.stream()
                .filter(e -> enumExtractor.apply(e).equals(enumValue))
                .map(mapper)
                .toList();
    }
}
