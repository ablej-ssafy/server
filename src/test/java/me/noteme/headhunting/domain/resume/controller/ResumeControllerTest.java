package me.noteme.headhunting.domain.resume.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.common.service.StorageService;
import me.noteme.headhunting.core.annotation.CustomMockUser;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.recruitment.dto.RecommendResponse;
import me.noteme.headhunting.domain.resume.controller.request.*;
import me.noteme.headhunting.domain.resume.dto.*;
import me.noteme.headhunting.domain.resume.entity.*;
import me.noteme.headhunting.domain.resume.dto.ResumeBasicResponse;
import me.noteme.headhunting.domain.resume.dto.ResumePdfResponse;
import me.noteme.headhunting.domain.resume.service.ResumeService;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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

    @MockBean
    private StorageService storageService;

    @Test
    @DisplayName("이력서 PDF 파일 업로드 테스트")
    @CustomMockUser
    void 이력서_PDF_업로드_테스트() throws Exception {
        // * GIVEN: 테스트용 PDF 파일 준비
        MockMultipartFile resumePdf = new MockMultipartFile(
                "file", // @RequestPart("file")와 일치하게 설정
                "test-resume.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "sample pdf content".getBytes()
        );

        // List<RecommendResponse> 타입의 가짜 응답 데이터 설정
        List<RecommendResponse> response = List.of(
                RecommendResponse.of(1L, "title1", "companyName1", "thumbnail1", false, 0.42)
        );

        when(resumeService.upload(anyLong(), eq(resumePdf))).thenReturn(response);

        // * WHEN: API 호출
        ResultActions actions = mockMvc.perform(
                multipart("/api/v1/resume/pdf")
                        .file(resumePdf)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        // * THEN: 기대 결과 검증 및 RestDocs 문서화
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서")
                                .summary("이력서 PDF 파일 업로드 API")
                                .description("PDF 형식의 이력서 파일을 업로드합니다. 'file' 파라미터를 통해 파일을 전달합니다.")
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("채용공고 ID"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("채용공고 제목"),
                                        fieldWithPath("data[].companyName").type(JsonFieldType.STRING).description("회사명"),
                                        fieldWithPath("data[].thumbnail").type(JsonFieldType.STRING).description("썸네일 이미지"),
                                        fieldWithPath("data[].scrapped").type(JsonFieldType.BOOLEAN).description("스크랩 여부"),
                                        fieldWithPath("data[].similarity").type(JsonFieldType.NUMBER).description("유사도 점수")
                                )
                                .build()
                )));
        // upload 메서드 호출 여부 및 파일 전달 여부 확인
        verify(resumeService).upload(anyLong(), eq(resumePdf));
    }

    @Test
    @DisplayName("이력서_정보_조회_테스트")
    @CustomMockUser
    void 이력서_정보_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        ResumeResponse response = ResumeResponse.of(
                "hashKey",
                true,
                ResumeTemplateType.BASIC_LIGHT
        );

        when(resumeService.getResume(memberId)).thenReturn(response);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(
                get("/api/v1/resume")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서")
                                .summary("이력서 정보 조회 API")
                                .description("로그인한 사용자의 이력서 정보를 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data.hashKey").type(JsonFieldType.STRING).description("해시키"),
                                        fieldWithPath("data.private").type(JsonFieldType.BOOLEAN).description("비공개 여부"),
                                        fieldWithPath("data.templateType").type(JsonFieldType.STRING).description("템플릿 타입")
                                )).build()
                )));
    }

    @Test
    @DisplayName("이력서_생성_테스트")
    @CustomMockUser
    void 이력서_생성_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(post("/api/v1/resume/basic/test")
                .contentType("application/json")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isCreated())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서")
                                .summary("이력서 초기 생성 API")
                                .description("로그인된 사용자의 새로운 이력서를 생성합니다.")
                                .responseFields(empty())
                                .build()
                )));

        verify(resumeService).resumeInit(memberId);
    }

    @Test
    @DisplayName("이력서_PDF_목록_조회_테스트")
    @CustomMockUser
    void 이력서_PDF_목록_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        List<ResumePdfResponse> response = Stream.of(1, 2, 3).map(index ->
                ResumePdfResponse.of((long) index, "파일 이름", "파일 주소", LocalDate.of(2024, 10, 1))
        ).toList();

        when(resumeService.getPdfList(memberId)).thenReturn(
                response
        );

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(
                get("/api/v1/resume/pdf")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서")
                                .summary("이력서 PDF 목록 반환 API")
                                .description("PDF List 정보를 반환합니다.")
                                .responseFields(response(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("이력서 PDF 목록"),
                                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("이력서 PDF ID"),
                                        fieldWithPath("data[].fileName").type(JsonFieldType.STRING).description("이력서 PDF 파일 이름"),
                                        fieldWithPath("data[].url").type(JsonFieldType.STRING).description("이력서 PDF 파일 주소"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("생성 날짜(yyy-MM-dd 형식)")
                                )).build()
                )));
    }

    @Test
    @DisplayName("이력서_기본정보_조회_테스트")
    @CustomMockUser
    void 이력서_기본정보_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        ResumeBasicResponse mockResponse = ResumeBasicResponse.of(
                1L,
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

        when(resumeService.getBasicInfo(memberId))
                .thenReturn(mockResponse);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(
                get("/api/v1/resume/basic")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-기본")
                                .summary("기본 정보 조회 API")
                                .description("로그인한 사용자의 이력서 기본 정보를 조회합니다.")
                                .responseFields(response(
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
    @CustomMockUser
    void 이력서_기본_정보_업데이트_테스트() throws Exception {
        // * GIVEN: 테스트 요청 데이터 생성
        Long memberId = 1L;
        ResumeBasicRequest request = new ResumeBasicRequest();
        request.setJob("백엔드 개발자");
        request.setProfile("https://example.com/profile.png");
        request.setTitle("Senior Developer");
        request.setName("John Doe");
        request.setEmail("johndoe@example.com");
        request.setBirth(LocalDate.of(1990, 5, 20));
        request.setPhone("010-1234-5678");
        request.setIntroduce("Experienced software developer with a background in AI.");
        request.setPortfolioUrl("https://example.com/portfolio");

        // * WHEN: API 호출
        ResultActions actions = mockMvc.perform(post("/api/v1/resume/basic")
                .contentType("application/json")
                .content(toJson(request))
        );

        // * THEN: 기대 결과 검증
        actions.andExpect(status().isCreated())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-기본")
                                .summary("기본 정보 업데이트 API")
                                .description("이력서 기본 정보를 업데이트합니다.")
                                .requestFields(
                                        fieldWithPath("job").type(JsonFieldType.STRING).description("직무"),
                                        fieldWithPath("profile").type(JsonFieldType.STRING).optional().description("프로필 이미지 URL"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("이력서 제목"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일 주소"),
                                        fieldWithPath("birth").type(JsonFieldType.STRING).description("생년월일"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("핸드폰 번호"),
                                        fieldWithPath("introduce").type(JsonFieldType.STRING).description("한 줄 소개"),
                                        fieldWithPath("portfolioUrl").type(JsonFieldType.STRING).optional().description("포트폴리오 URL")
                                ).responseFields(empty())
                                .build()
                )));

        // 서비스 메서드 호출 검증
        verify(resumeService).saveResumeBasic(
                memberId,
                request.getJob(),
                request.getProfile(),
                request.getTitle(),
                request.getName(),
                request.getEmail(),
                request.getBirth(),
                request.getPhone(),
                request.getIntroduce(),
                request.getPortfolioUrl()
        );
    }

    @Test
    @DisplayName("이력서_PDF_다운로드_링크_테스트")
    @CustomMockUser
    void 이력서_PDF_다운로드_링크_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        Long resumePdfId = 1L;

        String response = "다운로드 링크";

        when(resumeService.download(memberId, resumePdfId))
                .thenReturn(response);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                get("/api/v1/resume/download/{resumePdfId}", resumePdfId)
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서")
                                .summary("이력서 PDF 다운로드 API")
                                .description("PDF 파일을 다운로드할 수 있는 URL 반환한다.")
                                .pathParameters(
                                        parameterWithName("resumePdfId").description("이력서 파일 번호")
                                )
                                .responseFields(response(
                                        fieldWithPath("data").type(JsonFieldType.STRING).description("파일 URL")
                                )).build()
                )));
    }

    @Test
    @DisplayName("이력서_PDF_삭제_테스트")
    @CustomMockUser
    void 이력서_PDF_삭제_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        Long resumePdfId = 1L;

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                delete("/api/v1/resume/pdf/{resumePdfId}", resumePdfId)
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isNoContent())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서")
                                .summary("이력서 PDF 삭제 API")
                                .description("사용자의 특정 PDF 파일을 삭제합니다.")
                                .pathParameters(
                                        parameterWithName("resumePdfId").description("삭제할 이력서 PDF의 ID")
                                )
                                .build()
                )));

        // 서비스 메서드 호출 검증
        verify(resumeService).delete(memberId, resumePdfId);
    }

    @Test
    @DisplayName("이력서_순서_조회_테스트")
    @CustomMockUser
    void 이력서_순서_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;

        ResumeOrderResponse response = ResumeOrderResponse.of(0, 1, 2,3 ,4, 5, 6, 7);
        when(resumeService.getResumeOrder(memberId)).thenReturn(response);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                get("/api/v1/resume/order")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서")
                                .summary("이력서 순서 조회 API")
                                .description("이력서의 순서를 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data.basic").type(JsonFieldType.NUMBER).description("기본 정보 순서"),
                                        fieldWithPath("data.education").type(JsonFieldType.NUMBER).description("학력 순서"),
                                        fieldWithPath("data.company").type(JsonFieldType.NUMBER).description("경력 순서"),
                                        fieldWithPath("data.project").type(JsonFieldType.NUMBER).description("프로젝트 순서"),
                                        fieldWithPath("data.activity").type(JsonFieldType.NUMBER).description("활동 순서"),
                                        fieldWithPath("data.qualification").type(JsonFieldType.NUMBER).description("자격증 순서"),
                                        fieldWithPath("data.language").type(JsonFieldType.NUMBER).description("어학 순서"),
                                        fieldWithPath("data.tech").type(JsonFieldType.NUMBER).description("기술 스택 순서")
                                )).build()
                )));
    }

    @Test
    @DisplayName("이력서_순서_업데이트_테스트")
    @CustomMockUser
    void 이력서_순서_업데이트_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;

        ResumeOrderRequest request = new ResumeOrderRequest();
        request.setEducation(1);
        request.setCompany(2);
        request.setProject(3);
        request.setActivity(4);
        request.setQualification(5);
        request.setLanguage(6);
        request.setTech(7);


        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                put("/api/v1/resume/order")
                .contentType("application/json")
                .content(toJson(request))
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서")
                                .summary("이력서 순서 업데이트 API")
                                .description("이력서의 순서를 업데이트합니다.")
                                .requestFields(
                                        fieldWithPath("education").type(JsonFieldType.NUMBER).description("학력 순서"),
                                        fieldWithPath("company").type(JsonFieldType.NUMBER).description("경력 순서"),
                                        fieldWithPath("project").type(JsonFieldType.NUMBER).description("프로젝트 순서"),
                                        fieldWithPath("activity").type(JsonFieldType.NUMBER).description("활동 순서"),
                                        fieldWithPath("qualification").type(JsonFieldType.NUMBER).description("자격증 순서"),
                                        fieldWithPath("language").type(JsonFieldType.NUMBER).description("어학 순서"),
                                        fieldWithPath("tech").type(JsonFieldType.NUMBER).description("기술 스택 순서")
                                ).responseFields(empty())
                                .build()
                )));
        verify(resumeService).updateResumeOrder(memberId, 1, 2, 3, 4, 5, 6, 7);
    }

    @Test
    @DisplayName("이력서_템플릿_수정_요청_테스트")
    @CustomMockUser
    void 이력서_템플릿_수정_요청_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        TemplateRequest request = new TemplateRequest();
        request.setTemplateType(ResumeTemplateType.BASIC_LIGHT);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                patch("/api/v1/resume/template")
                        .contentType("application/json")
                        .content(toJson(request))
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서")
                                .summary("이력서 템플릿 수정 요청 API")
                                .description("이력서의 템플릿을 수정합니다.")
                                .requestFields(
                                        fieldWithPath("templateType").type(JsonFieldType.STRING).description("템플릿 타입")
                                ).responseFields(empty())
                                .build()
                )));
        verify(resumeService).changeTemplate(memberId, request.getTemplateType());
    }

    @Test
    @DisplayName("이력서_공개_테스트")
    @CustomMockUser
    void 이력서_공개_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                post("/api/v1/resume/visible")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서")
                                .summary("이력서 공개 API")
                                .description("이력서를 공개합니다.")
                                .responseFields(empty())
                                .build()
                )));
        verify(resumeService).updateVisible(memberId, false);
    }

    @Test
    @DisplayName("이력서_비공개_테스트")
    @CustomMockUser
    void 이력서_비공개_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                delete("/api/v1/resume/visible")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isNoContent())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서")
                                .summary("이력서 비공개 API")
                                .description("이력서를 비공개합니다.")
                                .responseFields()
                                .build()
                )));
        verify(resumeService).updateVisible(memberId, true);
    }
}