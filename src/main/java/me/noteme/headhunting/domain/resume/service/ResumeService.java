package me.noteme.headhunting.domain.resume.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.service.OpenAiService;
import me.noteme.headhunting.common.listener.event.FileUploadEvent;
import me.noteme.headhunting.common.service.StorageService;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.member.repository.MemberRepository;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
import me.noteme.headhunting.domain.recruitment.entity.JobCategory;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeBasicRepository resumeBasicRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final MemberRepository memberRepository;
    private final ResumePdfRepository resumePdfRepository;
    private final PDFToTextConverter pdfToTextConverter;
    private final ApplicationEventPublisher publisher;
    private final ResumeRepository resumeRepository;
    private final PDFToTextConverter pdfConverter;
    private final StorageService storageService;
    private final EntityManager em;

    private final OpenAiService openAiService;

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

        log.debug("멤버 조회 ");
        JobCategory jobCategory = member.getInterestJobs().getFirst().getJobCategory();

        log.debug("카데고리 조회 ");
        return recruitmentRepository.findRecruitmentsByCategoryId(
                        jobCategory.getId(),
                        Pageable.ofSize(3))
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
        String key = String.format("%s/%s",memberId, resumePdf.getKey());
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
    public void saveResumeBasic(Long resumeId, Long jobId, String profile, String title, String name, String email, LocalDate birth, String phone, String introduce, String portfolioUrl, Long resumeBasicId) {
        JobCategory job = getJobById(jobId);
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

    private JobCategory getJobById(Long jobId) {
        return em.getReference(JobCategory.class, jobId);
    }

    private Resume getResumeById(Long resumeId) {
        return em.getReference(Resume.class, resumeId);
    }

    private Member getMemberById(Long memberId) {
        return em.getReference(Member.class, memberId);
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

    public OpenAiResponse auto(String question) {
        String response = openAiService.auto(question);

        // GsonBuilder에 LocalDate 타입의 어댑터 추가
        // LocalDate 형식을 위한 DateTimeFormatter 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class,
                        (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                                src == null ? null : new com.google.gson.JsonPrimitive(src.format(formatter))
                )
                .registerTypeAdapter(LocalDate.class,
                        (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                                json == null ? null : LocalDate.parse(json.getAsString(), formatter)
                )
                .create();
        OpenAiResponse openAiResponse = gson.fromJson(response, OpenAiResponse.class);

        log.debug("{}",openAiResponse);
        return openAiResponse;
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
