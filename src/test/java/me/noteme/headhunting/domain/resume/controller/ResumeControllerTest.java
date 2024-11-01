package me.noteme.headhunting.domain.resume.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.annotation.CustomMockUser;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.resume.controller.request.CertificationForm;
import me.noteme.headhunting.domain.resume.controller.request.EducationalForm;
import me.noteme.headhunting.domain.resume.controller.request.ExperienceForm;
import me.noteme.headhunting.domain.resume.controller.request.ResumeBasicRequest;
import me.noteme.headhunting.domain.resume.dto.*;
import me.noteme.headhunting.domain.resume.entity.CertificationType;
import me.noteme.headhunting.domain.resume.entity.EducationalType;
import me.noteme.headhunting.domain.resume.entity.ExperienceType;
import me.noteme.headhunting.domain.resume.entity.GradeType;
import me.noteme.headhunting.domain.resume.service.ResumeService;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("이력서 컨트롤러 테스트")
@WebMvcTest(value = ResumeController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTFilter.class),
        }
)
class ResumeControllerTest extends RestDocsSupport {
    @MockBean
    private ResumeService resumeService;

    @Autowired
    private ResourceLoader loader;

    @Test
    @DisplayName("이력서_전체_정보_조회_테스트")
    @CustomMockUser
    void 이력서_전체_정보_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;

        ResumeBasicResponse basicResponse = ResumeBasicResponse.of(
                1L, 10L, "Sample Title", "profile.png", "John Doe", "johndoe@example.com",
                LocalDate.of(1990, 1, 1), "010-1234-5678", "Software Engineer",
                "A passionate developer", "https://portfolio.example.com"
        );

        List<EducationalForm> educationals = List.of(
                EducationalForm.of(1L, "바밤대학교", "바밤과", EducationalType.BACHELOR, "4.3", GradeType.FOUR_POINT_FIVE,
                        "학부재밌었다.", LocalDate.of(2018, 2, 12), LocalDate.of(2024, 2, 15), 1L),
                EducationalForm.of(1L, "바밤대학교", "바밤과", EducationalType.MASTER, "4.3", GradeType.FOUR_POINT_FIVE,
                        "학부재밌었다.", LocalDate.of(2018, 2, 12), LocalDate.of(2024, 2, 15), 2L)
        );

        List<ExperienceForm> companies = List.of(
                ExperienceForm.of(1L, ExperienceType.COMPANY, "프로젝트 경험", "회사명", LocalDate.of(2021, 3, 1), LocalDate.of(2022, 3, 1), "설명", "https://example.com", 1L),
                ExperienceForm.of(1L, ExperienceType.COMPANY, "봉사 활동", "봉사단체", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 6, 1), "설명", "https://volunteer.com", 2L)
        );
        List<ExperienceForm> activities = List.of(
                ExperienceForm.of(1L, ExperienceType.ACTIVITY, "프로젝트 경험", "회사명", LocalDate.of(2021, 3, 1), LocalDate.of(2022, 3, 1), "설명", "https://example.com", 3L),
                ExperienceForm.of(1L, ExperienceType.ACTIVITY, "봉사 활동", "봉사단체", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 6, 1), "설명", "https://volunteer.com", 4L)
        );
        List<ExperienceForm> projects = List.of(
                ExperienceForm.of(1L, ExperienceType.PROJECT, "프로젝트 경험", "회사명", LocalDate.of(2021, 3, 1), LocalDate.of(2022, 3, 1), "설명", "https://example.com", 5L),
                ExperienceForm.of(1L, ExperienceType.PROJECT, "봉사 활동", "봉사단체", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 6, 1), "설명", "https://volunteer.com", 6L)
        );

        List<CertificationForm> languages = List.of(
                CertificationForm.of(1L, "자격증 이름", "발행 기관", "인증 번호", LocalDate.of(2020, 5, 20), "A", CertificationType.LANGUAGE, 1L),
                CertificationForm.of(1L, "어학 이름", "발행 기관", "인증 번호", LocalDate.of(2019, 8, 15), "B", CertificationType.LANGUAGE, 2L)
        );
        List<CertificationForm> qualifications = List.of(
                CertificationForm.of(1L, "자격증 이름", "발행 기관", "인증 번호", LocalDate.of(2020, 5, 20), "A", CertificationType.QUALIFICATION, 1L),
                CertificationForm.of(1L, "어학 이름", "발행 기관", "인증 번호", LocalDate.of(2019, 8, 15), "B", CertificationType.QUALIFICATION, 2L)
        );

        TechResponse techResponse = TechResponse.of(
                1L,
                1L,
                List.of(
                        TechSkillResponse.of(1L, "Java", "java-icon.png"),
                        TechSkillResponse.of(2L, "Spring", "spring-icon.png")
                ),
                List.of(
                        ReferenceUrlResponse.of(1L, "https://example.com/java"),
                        ReferenceUrlResponse.of(2L, "https://example.com/spring")
                )
        );

        ResumeResponse mockResponse = ResumeResponse.of(basicResponse, educationals, companies, activities, projects, languages, qualifications, techResponse);

        when(resumeService.getResume(memberId))
                .thenReturn(mockResponse);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(
                get("/api/v1/resume")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서 전체 정보 조회")
                                .summary("로그인 사용자 기반 이력서 정보 조회 API")
                                .description("로그인한 사용자의 전체 이력서 정보를 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data.basic.resumeId").type(JsonFieldType.NUMBER).description("이력서 ID"),
                                        fieldWithPath("data.basic.resumeBasicId").type(JsonFieldType.NUMBER).description("기본 이력서 정보 ID"),
                                        fieldWithPath("data.basic.title").type(JsonFieldType.STRING).description("이력서 제목"),
                                        fieldWithPath("data.basic.profile").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
                                        fieldWithPath("data.basic.name").type(JsonFieldType.STRING).description("사용자 이름"),
                                        fieldWithPath("data.basic.email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                        fieldWithPath("data.basic.birth").type(JsonFieldType.STRING).description("생년월일"),
                                        fieldWithPath("data.basic.phone").type(JsonFieldType.STRING).description("전화번호"),
                                        fieldWithPath("data.basic.job").type(JsonFieldType.STRING).description("직업"),
                                        fieldWithPath("data.basic.introduce").type(JsonFieldType.STRING).description("자기소개"),
                                        fieldWithPath("data.basic.portfolioUrl").type(JsonFieldType.STRING).description("포트폴리오 URL"),
                                        fieldWithPath("data.educationals[].resumeId").type(JsonFieldType.NUMBER).description("이력서 ID"),
                                        fieldWithPath("data.educationals[].name").type(JsonFieldType.STRING).description("학교 이름"),
                                        fieldWithPath("data.educationals[].major").type(JsonFieldType.STRING).description("전공"),
                                        fieldWithPath("data.educationals[].category").type(JsonFieldType.STRING).description("학교 타입"),
                                        fieldWithPath("data.educationals[].grade").type(JsonFieldType.STRING).description("학점"),
                                        fieldWithPath("data.educationals[].gradeType").type(JsonFieldType.STRING).description("최대 학점"),
                                        fieldWithPath("data.educationals[].description").type(JsonFieldType.STRING).description("활동 내용 설명"),
                                        fieldWithPath("data.educationals[].startAt").type(JsonFieldType.STRING).description("입학 날짜"),
                                        fieldWithPath("data.educationals[].endAt").type(JsonFieldType.STRING).description("졸업 날짜"),
                                        fieldWithPath("data.educationals[].educationalId").type(JsonFieldType.NUMBER).description("학력 ID"),
                                        fieldWithPath("data.companies[].resumeId").type(JsonFieldType.NUMBER).description("이력서 ID"),
                                        fieldWithPath("data.companies[].experienceType").type(JsonFieldType.STRING).description("경험 타입 (COMPANY)"),
                                        fieldWithPath("data.companies[].title").type(JsonFieldType.STRING).description("회사 이름"),
                                        fieldWithPath("data.companies[].affiliation").type(JsonFieldType.STRING).description("회사 소속"),
                                        fieldWithPath("data.companies[].startAt").type(JsonFieldType.STRING).description("근무 시작 날짜"),
                                        fieldWithPath("data.companies[].endAt").type(JsonFieldType.STRING).description("근무 종료 날짜"),
                                        fieldWithPath("data.companies[].description").type(JsonFieldType.STRING).description("직무 설명"),
                                        fieldWithPath("data.companies[].referenceUrl").type(JsonFieldType.STRING).description("참조 URL"),
                                        fieldWithPath("data.companies[].experienceId").type(JsonFieldType.NUMBER).description("경험 ID"),
                                        fieldWithPath("data.activities[].resumeId").type(JsonFieldType.NUMBER).description("이력서 ID"),
                                        fieldWithPath("data.activities[].experienceType").type(JsonFieldType.STRING).description("경험 타입 (ACTIVITY)"),
                                        fieldWithPath("data.activities[].title").type(JsonFieldType.STRING).description("활동 제목"),
                                        fieldWithPath("data.activities[].affiliation").type(JsonFieldType.STRING).description("활동 소속"),
                                        fieldWithPath("data.activities[].startAt").type(JsonFieldType.STRING).description("활동 시작 날짜"),
                                        fieldWithPath("data.activities[].endAt").type(JsonFieldType.STRING).description("활동 종료 날짜"),
                                        fieldWithPath("data.activities[].description").type(JsonFieldType.STRING).description("활동 설명"),
                                        fieldWithPath("data.activities[].referenceUrl").type(JsonFieldType.STRING).description("참조 URL"),
                                        fieldWithPath("data.activities[].experienceId").type(JsonFieldType.NUMBER).description("경험 ID"),
                                        fieldWithPath("data.projects[].resumeId").type(JsonFieldType.NUMBER).description("이력서 ID"),
                                        fieldWithPath("data.projects[].experienceType").type(JsonFieldType.STRING).description("경험 타입 (PROJECT)"),
                                        fieldWithPath("data.projects[].title").type(JsonFieldType.STRING).description("프로젝트 제목"),
                                        fieldWithPath("data.projects[].affiliation").type(JsonFieldType.STRING).description("프로젝트 소속"),
                                        fieldWithPath("data.projects[].startAt").type(JsonFieldType.STRING).description("프로젝트 시작 날짜"),
                                        fieldWithPath("data.projects[].endAt").type(JsonFieldType.STRING).description("프로젝트 종료 날짜"),
                                        fieldWithPath("data.projects[].description").type(JsonFieldType.STRING).description("프로젝트 설명"),
                                        fieldWithPath("data.projects[].referenceUrl").type(JsonFieldType.STRING).description("참조 URL"),
                                        fieldWithPath("data.projects[].experienceId").type(JsonFieldType.NUMBER).description("경험 ID"),
                                        fieldWithPath("data.qualifications[].resumeId").type(JsonFieldType.NUMBER).description("이력서 ID"),
                                        fieldWithPath("data.qualifications[].name").type(JsonFieldType.STRING).description("자격증 이름"),
                                        fieldWithPath("data.qualifications[].organization").type(JsonFieldType.STRING).description("발급 기관"),
                                        fieldWithPath("data.qualifications[].credential").type(JsonFieldType.STRING).description("자격 번호"),
                                        fieldWithPath("data.qualifications[].acquisitionAt").type(JsonFieldType.STRING).description("취득 날짜"),
                                        fieldWithPath("data.qualifications[].grade").type(JsonFieldType.STRING).description("등급"),
                                        fieldWithPath("data.qualifications[].certificationType").type(JsonFieldType.STRING).description("자격증 유형"),
                                        fieldWithPath("data.qualifications[].certificationId").type(JsonFieldType.NUMBER).description("자격증 ID"),
                                        fieldWithPath("data.languages[].resumeId").type(JsonFieldType.NUMBER).description("이력서 ID"),
                                        fieldWithPath("data.languages[].name").type(JsonFieldType.STRING).description("어학 자격증 이름"),
                                        fieldWithPath("data.languages[].organization").type(JsonFieldType.STRING).description("발급 기관"),
                                        fieldWithPath("data.languages[].credential").type(JsonFieldType.STRING).description("자격 번호"),
                                        fieldWithPath("data.languages[].acquisitionAt").type(JsonFieldType.STRING).description("취득 날짜"),
                                        fieldWithPath("data.languages[].grade").type(JsonFieldType.STRING).description("등급"),
                                        fieldWithPath("data.languages[].certificationType").type(JsonFieldType.STRING).description("어학 유형"),
                                        fieldWithPath("data.languages[].certificationId").type(JsonFieldType.NUMBER).description("자격증 ID"),
                                        fieldWithPath("data.tech.resumeId").type(JsonFieldType.NUMBER).description("이력서 ID"),
                                        fieldWithPath("data.tech.techId").type(JsonFieldType.NUMBER).description("기술 스택 ID"),
                                        fieldWithPath("data.tech.techSkills[].skillId").type(JsonFieldType.NUMBER).description("기술 ID"),
                                        fieldWithPath("data.tech.techSkills[].skillName").type(JsonFieldType.STRING).description("기술 이름"),
                                        fieldWithPath("data.tech.techSkills[].skillIcon").type(JsonFieldType.STRING).description("기술 아이콘 URL"),
                                        fieldWithPath("data.tech.referenceUrls[].referenceUrlId").type(JsonFieldType.NUMBER).description("참조 URL ID"),
                                        fieldWithPath("data.tech.referenceUrls[].url").type(JsonFieldType.STRING).description("참조 URL")
                                )).build()
                )));
    }

    @Test
    @DisplayName("이력서_생성_테스트")
    void 이력서_생성_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        long memberId = 1L;

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(post("/api/v1/resume")
                .contentType("application/json")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isCreated())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서 생성")
                                .summary("이력서 생성 API")
                                .description("로그인된 사용자의 새로운 이력서를 생성합니다.")
                                .responseFields(empty())
                                .build()
                )));

        verify(resumeService).resumeInit(memberId);
    }

    @Test
    @DisplayName("이력서_기본정보_조회_테스트")
    @CustomMockUser
    void 이력서_기본정보_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long userId = 1L;
        ResumeBasicResponse mockResponse = ResumeBasicResponse.of(
                1L,
                10L,
                "이력서 제목",
                "https://portfolio.example.com/profile.png",
                "바밤바",
                "ablej@example.com",
                LocalDate.of(1999, 10, 29),
                "010-1234-5678",
                "백엔드 개발자",
                "같이하는 가치",
                "https://portfolio.example.com"
        );

        when(resumeService.getBasicInfo(userId))
                .thenReturn(mockResponse);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(
                get("/api/v1/resume/basic")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서 기본 정보 조회")
                                .summary("로그인 사용자 기반 이력서 기본 정보 조회 API")
                                .description("로그인한 사용자의 이력서 기본 정보를 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data.resumeId").type(JsonFieldType.NUMBER).description("이력서 PK"),
                                        fieldWithPath("data.resumeBasicId").type(JsonFieldType.NUMBER).description("기본 이력서 정보 PK"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("이력서 제목"),
                                        fieldWithPath("data.profile").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("사용자 이름"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                        fieldWithPath("data.birth").type(JsonFieldType.STRING).description("생년월일"),
                                        fieldWithPath("data.phone").type(JsonFieldType.STRING).description("전화번호"),
                                        fieldWithPath("data.job").type(JsonFieldType.STRING).description("직업"),
                                        fieldWithPath("data.introduce").type(JsonFieldType.STRING).description("자기소개"),
                                        fieldWithPath("data.portfolioUrl").type(JsonFieldType.STRING).description("포트폴리오 URL")
                                )).build()
                )));
    }

    @Test
    @DisplayName("이력서_기본_정보_업데이트_테스트")
    void 이력서_기본_정보_업데이트_테스트() throws Exception {
        // * GIVEN: 테스트 요청 데이터 생성
        ResumeBasicRequest request = new ResumeBasicRequest();
        request.setResumeId(1L);
        request.setJobId(2L);
        request.setProfile("https://example.com/profile.png");
        request.setTitle("Senior Developer");
        request.setName("John Doe");
        request.setEmail("johndoe@example.com");
        request.setBirth(LocalDate.of(1990, 5, 20));
        request.setPhone("010-1234-5678");
        request.setIntroduce("Experienced software developer with a background in AI.");
        request.setPortfolioUrl("https://example.com/portfolio");
        request.setResumeBasicId(null);

        // * WHEN: API 호출
        ResultActions actions = mockMvc.perform(post("/api/v1/resume/basic")
                .contentType("application/json")
                .content(toJson(request))
        );

        // * THEN: 기대 결과 검증
        actions.andExpect(status().isCreated())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서 기본 정보 생성")
                                .summary("이력서 기본 정보 업데이트 API")
                                .description("이력서 기본 정보를 업데이트합니다.")
                                .requestFields(
                                        fieldWithPath("resumeId").type(JsonFieldType.NUMBER).description("이력서 PK"),
                                        fieldWithPath("jobId").type(JsonFieldType.NUMBER).description("직무 PK"),
                                        fieldWithPath("profile").type(JsonFieldType.STRING).optional().description("프로필 이미지 URL"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("이력서 제목"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일 주소"),
                                        fieldWithPath("birth").type(JsonFieldType.STRING).description("생년월일"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("핸드폰 번호"),
                                        fieldWithPath("introduce").type(JsonFieldType.STRING).description("한 줄 소개"),
                                        fieldWithPath("portfolioUrl").type(JsonFieldType.STRING).optional().description("포트폴리오 URL"),
                                        fieldWithPath("resumeBasicId").type(JsonFieldType.NUMBER).optional().description("이력서 기본 PK (새로 추가 시 null)")
                                ).responseFields(empty())
                                .build()
                )));

        // 서비스 메서드 호출 검증
        verify(resumeService).saveResumeBasic(
                request.getResumeId(),
                request.getJobId(),
                request.getProfile(),
                request.getTitle(),
                request.getName(),
                request.getEmail(),
                request.getBirth(),
                request.getPhone(),
                request.getIntroduce(),
                request.getPortfolioUrl(),
                request.getResumeBasicId()
        );
    }

    @Test
    @DisplayName("이력서_PDF_문자열_변환_테스트")
    void 이력서_PDF_문자열_변환_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        String fileName = "resume.pdf";
        Resource source = loader.getResource("classpath:/media/" + fileName);
        MockMultipartFile file = new MockMultipartFile("file", fileName, "application/pdf", source.getInputStream());

        when(resumeService.getText(file)).thenReturn("추출한 텍스트");

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(multipart("/api/v1/resume/convert")
                        .file(file)
                        .contentType("multipart/form-data")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서 관리")
                                .summary("이력서 PDF 문자열 변환 API")
                                .description("PDF 파일을 텍스트로 변환합니다.")
                                .responseFields(response(
                                        fieldWithPath("data").type(JsonFieldType.STRING).description("변환된 텍스트")
                                )).build()
                )));
    }

//    @Test
//    @DisplayName("이력서_PDF_다운로드_링크_테스트")
//    void 이력서_PDF_다운로드_링크_테스트() throws Exception {
//        // * GIVEN: 이런게 주어졌을 때
//        String fileName = "resume.pdf";
//        String fileUrl = "https://url/to/download/file/" + fileName;
//
//        // TODO: 추후 수정해야합니다. To. 민준수
//        Long memberId = 1L;
//        when(storageService.getFileUrl(memberId, fileName)).thenReturn(fileUrl);
//
//        // * WHEN: 이걸 실행하면
//        ResultActions actions = this.mockMvc.perform(
//                get("/api/v1/resume/pdf/{fileName}", fileName)
//        );
//
//        // * THEN: 이런 결과가 나와야 한다
//        actions.andExpect(status().isOk())
//                .andDo(restDocs.document(resource(
//                        ResourceSnippetParameters.builder()
//                                .tag("이력서 관리")
//                                .summary("이력서 PDF 다운로드 API")
//                                .description("PDF 파일을 다운로드할 수 있는 URL 반환한다.")
//                                .pathParameters(
//                                        parameterWithName("fileName").description("파일 이름")
//                                )
//                                .responseFields(response(
//                                        fieldWithPath("data").type(JsonFieldType.STRING).description("파일 URL")
//                                )).build()
//                )));
//    }
}