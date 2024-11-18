package me.noteme.headhunting.domain.resume.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.adapter.LocalDateAdapter;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.service.OpenAiService;
import me.noteme.headhunting.common.listener.event.FileUploadEvent;
import me.noteme.headhunting.common.service.StorageService;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.member.repository.MemberRepository;
import me.noteme.headhunting.domain.member.repository.ScrapRepository;
import me.noteme.headhunting.domain.recruitment.dto.RecommendResponse;
import me.noteme.headhunting.domain.recruitment.repository.RecruitmentCategoryRepository;
import me.noteme.headhunting.domain.recruitment.repository.RecruitmentRepository;
import me.noteme.headhunting.domain.resume.controller.request.CertificationForm;
import me.noteme.headhunting.domain.resume.controller.request.EducationForm;
import me.noteme.headhunting.domain.resume.controller.request.ExperienceForm;
import me.noteme.headhunting.domain.resume.dto.*;
import me.noteme.headhunting.domain.resume.entity.*;
import me.noteme.headhunting.domain.resume.dto.ResumeBasicResponse;
import me.noteme.headhunting.domain.resume.dto.ResumePdfResponse;
import me.noteme.headhunting.domain.resume.entity.ResumePdf;
import me.noteme.headhunting.domain.resume.entity.mongo.*;
import me.noteme.headhunting.domain.resume.repository.*;
import me.noteme.headhunting.domain.resume.utils.PDFToTextUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResumeService {
    private final RecruitmentCategoryRepository recruitmentCategoryRepository;
    private final ResumeBasicRepository resumeBasicRepository;
    private final CertificationRepository certificationRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;

    private final RecruitmentRepository recruitmentRepository;
    private final ScrapRepository scrapRepository;
    private final MemberRepository memberRepository;
    private final ResumePdfRepository resumePdfRepository;
    private final ApplicationEventPublisher publisher;
    private final ResumeRepository resumeRepository;
    private final ResumeOrderRepository resumeOrderRepository;
    private final StorageService storageService;
    private final MongoResumeRepository mongoResumeRepository;
    private final ResumeCacheRepository resumeCacheRepository;
    private final EntityManager em;

    private final OpenAiService openAiService;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public String download(Long memberId, Long resumePdfId) {
        ResumePdf resumePdf = resumePdfRepository.findById(resumePdfId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        return storageService.getFileUrl(memberId, resumePdf.getPdfKey());
    }

    @Transactional
    public List<RecommendResponse> upload(Long memberId, MultipartFile resumePdf) {
        String resumeText = PDFToTextUtil.convertPdfToText(resumePdf);

        publisher.publishEvent(FileUploadEvent.of(memberId, resumePdf, resumeText));

        Member member = getMember(memberId);

        List<Long> recruitmentIds = recruitmentCategoryRepository.
                findRecruitmentIdsByCategoryIds(
                        member.getJobCategory(),
                        Pageable.ofSize(3))
                .getContent();

        Set<Long> scrappedIds = scrapRepository.isScrapped(memberId, recruitmentIds);

        return recruitmentRepository.findRecruitmentsById(recruitmentIds).stream()
                .map(recruitment -> RecommendResponse.create(
                        recruitment,
                        scrappedIds.contains(recruitment.getId()))
                )
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
        mongoResumeRepository.updateBasic(memberId, MongoResumeBasic.from(resumeBasicRepository.save(resumeBasic)));
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void autoResume(Long memberId, MultipartFile file) {
        String pdfText = PDFToTextUtil.convertPdfToText(file);
        String resumeAutoData = openAiService.resume(pdfText);

        log.debug("{}", resumeAutoData);
        resumeCacheRepository.save(memberId, resumeAutoData);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public OpenAiResponse getAutoResume(Long memberId) {
        return getOpenAiResponse(memberId);
    }

    @Transactional
    public void changeAutoResume(Long memberId) {
        OpenAiResponse openAiResponse = getOpenAiResponse(memberId);
        Resume resume = resumeRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        memberResumeClear(memberId);

        // ResumeBasic
        ResumeBasic resumeBasic = ResumeBasic.from(openAiResponse.getAiBasic(), resume);
        resumeBasicRepository.save(resumeBasic);

        // Education
        List<Education> educations = converterResumeEntities(openAiResponse.getAiEducationals(), resume, Education::from);
        educationRepository.saveAll(educations);

        // Certification
        List<Certification> certifications = converterResumeEntities(openAiResponse.getAiCertifications(), resume, Certification::from);
        certificationRepository.saveAll(certifications);

        // Experience
        List<Experience> experiences = converterResumeEntities(openAiResponse.getAiExperiences(), resume, Experience::from);
        experienceRepository.saveAll(experiences);

        mongoResumeRepository.findByMemberId(memberId)
                .ifPresent(mongoResumeRepository::delete);

        MongoResume mongoResume = createMongoResume(memberId, resume.getHashKey(), resumeBasic, educations, experiences, certifications);
        mongoResumeRepository.save(mongoResume);
    }

    private MongoResume createMongoResume(Long memberId, String hashKey, ResumeBasic resumeBasic, List<Education> educations, List<Experience> experiences, List<Certification> certifications) {
        return MongoResume.builder()
                .memberId(memberId)
                .hashKey(hashKey)
                .basic(MongoResumeBasic.from(resumeBasic))
                .educations(educations.stream().map(MongoEducation::from).toList())
                .companies(filterByEnum(experiences, ExperienceType.COMPANY, Experience::getExperienceType, MongoExperience::from))
                .activities(filterByEnum(experiences, ExperienceType.ACTIVITY, Experience::getExperienceType, MongoExperience::from))
                .projects(filterByEnum(experiences, ExperienceType.PROJECT, Experience::getExperienceType, MongoExperience::from))
                .qualifications(filterByEnum(certifications, CertificationType.QUALIFICATION, Certification::getCertificationType, MongoCertification::from))
                .languages(filterByEnum(certifications, CertificationType.LANGUAGE, Certification::getCertificationType, MongoCertification::from))
                .build();
    }

    private <T, R> List<R> converterResumeEntities(List<T> list, Resume resume, BiFunction<T, Resume, R> converter) {
        return list.stream()
                .map(item -> converter.apply(item, resume))
                .toList();
    }


    private void memberResumeClear(Long memberId) {
        resumeBasicRepository.deleteAllByMemberId(memberId);
        educationRepository.deleteAllByMemberId(memberId);
        certificationRepository.deleteAllByMemberId(memberId);
        experienceRepository.deleteAllByMemberId(memberId);
    }

    @Transactional
    public void resumeInit(Long memberId) {
        Member member = getMember(memberId);

        Resume resume = Resume.builder()
                .member(member)
                .build();

        resumeRepository.save(resume);
    }

    public ResumeBasicResponse getBasicInfo(Long memberId) {
        Member member = getMember(memberId);

        return resumeBasicRepository.findByMemberId(memberId)
                .map(ResumeBasicResponse::fromEntity)
                .orElseGet(ResumeBasicResponse::new)
                .setInfoIfEmpty(member);
    }

    public ResumeOrderResponse getResumeOrder(Long memberId) {
        ResumeOrder resumeOrder = resumeOrderRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        if (!resumeOrder.getResume().getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        return ResumeOrderResponse.fromEntity(resumeOrder);
    }

    private OpenAiResponse getOpenAiResponse(Long memberId) {
        return gson.fromJson(
                resumeCacheRepository.getData(memberId),
                OpenAiResponse.class
        );
    }

    private Resume getResumeByMemberId(Long memberId) {
        return resumeRepository.findByMemberId(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 Member가 지니고 있는 Resume가 없습니다."));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findFetchById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
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

    @Transactional
    public void changeTemplate(Long memberId, ResumeTemplateType templateType) {
        Resume resume = resumeRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        resume.changeTemplate(templateType);
        mongoResumeRepository.updateTemplate(memberId, templateType);
    }

    @Transactional
    public void updateVisible(Long memberId, boolean visible) {
        Resume resume = resumeRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        resume.updateVisible(visible);
        mongoResumeRepository.updateVisible(memberId, visible);
    }

    public ResumeResponse getResume(Long memberId) {
        Resume resume = resumeRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        return ResumeResponse.fromEntity(resume);
    }

    @Transactional
    public void updateResumeOrder(Long memberId, int education, int company, int project, int activity, int qualification, int language, int tech) {
        ResumeOrder resumeOrder = resumeOrderRepository.findByMemberId(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        resumeOrder.update(education, company, project, activity, qualification, language, tech);
    }
}
