package me.noteme.headhunting.domain.resume.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.listener.event.FileUploadEvent;
import me.noteme.headhunting.common.service.StorageService;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.member.repository.MemberRepository;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
import me.noteme.headhunting.domain.recruitment.repository.RecruitmentCategoryRepository;
import me.noteme.headhunting.domain.recruitment.repository.RecruitmentRepository;
import me.noteme.headhunting.domain.resume.controller.request.CertificationForm;
import me.noteme.headhunting.domain.resume.controller.request.EducationalForm;
import me.noteme.headhunting.domain.resume.controller.request.ExperienceForm;
import me.noteme.headhunting.domain.resume.dto.*;
import me.noteme.headhunting.domain.resume.entity.*;
import me.noteme.headhunting.domain.resume.dto.ResumeBasicResponse;
import me.noteme.headhunting.domain.resume.dto.ResumePdfResponse;
import me.noteme.headhunting.domain.resume.entity.ResumePdf;
import me.noteme.headhunting.domain.resume.repository.ResumePdfRepository;
import me.noteme.headhunting.domain.resume.repository.ResumeBasicRepository;
import me.noteme.headhunting.domain.resume.repository.ResumeRepository;
import me.noteme.headhunting.domain.resume.utils.PDFToTextConverter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResumeService {
    private final RecruitmentCategoryRepository recruitmentCategoryRepository;
    private final ResumeBasicRepository resumeBasicRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final MemberRepository memberRepository;
    private final ResumePdfRepository resumePdfRepository;
    private final PDFToTextConverter pdfToTextConverter;
    private final ApplicationEventPublisher publisher;
    private final ResumeRepository resumeRepository;
    private final StorageService storageService;
    private final EntityManager em;

    public String download(Long memberId, Long resumePdfId) {
        ResumePdf resumePdf = resumePdfRepository.findById(resumePdfId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        return storageService.getFileUrl(memberId, resumePdf.getPdfKey());
    }

    @Transactional
    public List<RecruitmentSummaryResponse> upload(Long memberId, MultipartFile resumePdf) {
        String resumeText = pdfToTextConverter.convertPdfToText(resumePdf);

        publisher.publishEvent(FileUploadEvent.of(memberId, resumePdf, resumeText));

        Member member = memberRepository.findFetchById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        List<Long> jobCategoryIds = member.getInterestJobs().stream()
                .map(interestJob -> interestJob.getJobCategory().getId())
                .toList();

        List<Long> recruitmentIds = recruitmentCategoryRepository.
                findRecruitmentIdsByCategoryIds(
                        jobCategoryIds,
                        Pageable.ofSize(3))
                .getContent();

        return recruitmentRepository.findRecruitmentsById(recruitmentIds).stream()
                .map(RecruitmentSummaryResponse::fromEntity)
                .toList();
    }

    @Transactional
    public void savePdf(Long memberId, String fileName, String key) {
        Member member = em.getReference(Member.class, memberId);
        ResumePdf resumePdf = ResumePdf.of(member, fileName, key);

        resumePdfRepository.save(resumePdf);
    }

    @Transactional
    public void delete(Long memberId, Long resumePdfId) {
        ResumePdf resumePdf = resumePdfRepository.findByIdAndMemberId(resumePdfId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCESS_DENIED));

        resumePdfRepository.deleteById(resumePdfId);
        String key = String.format("%s/%s", memberId, resumePdf.getKey());
        storageService.delete(key);
        storageService.delete(key + ".pdf");
    }

    public List<ResumePdfResponse> getPdfList(Long memberId) {
        List<ResumePdf> resumePdfList = resumePdfRepository.findAllByMemberId(memberId);

        return resumePdfList.stream()
                .map(o -> ResumePdfResponse.of(
                        o.getId(),
                        o.getFileName(),
                        storageService.getFileUrl(memberId, o.getPdfKey()),
                        LocalDate.from(o.getCreatedAt())
                )).toList();
    }

    @Transactional
    public void saveResumeBasic(Long memberId, String job, String profile, String title, String name, String email, LocalDate birth, String phone, String introduce, String portfolioUrl) {
        Resume resume = getResumeByMemberId(memberId);
        Long resumeBasicId = Objects.isNull(resume.getResumeBasic()) ? null : resume.getResumeBasic().getId();
        ResumeBasic resumeBasic = ResumeBasic.of(resumeBasicId, title, name, email, birth, phone, introduce, portfolioUrl, resume, job, profile);
        resumeBasicRepository.save(resumeBasic);
    }

    @Transactional
    public void resumeInit(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        Resume resume = Resume.builder()
                .member(member)
                .build();
        resumeRepository.save(resume);
    }

    public ResumeBasicResponse getBasicInfo(Long memberId) {
        Member member = memberRepository.findFetchById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        return resumeBasicRepository.findByMemberId(memberId)
                .map(ResumeBasicResponse::fromEntity)
                .orElseGet(ResumeBasicResponse::new)
                .setInfoIfEmpty(member);
    }

    public ResumeResponse getResume(Long memberId) {
        ResumeBasicResponse basic = resumeBasicRepository.findByMemberId(memberId)
                .map(ResumeBasicResponse::fromEntity)
                .orElse(null);
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

        TechResponse tech = resumeRepository.findTechByMemberId(memberId)
                .map(TechResponse::fromEntity)
                .orElse(null);

        return ResumeResponse.of(basic, educationals, companies, activities, projects, qualifications, languages, tech);
    }

    private Resume getResumeByMemberId(Long memberId) {
        return resumeRepository.findByMemberId(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 Member가 지니고 있는 Resume가 없습니다."));
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
