package me.noteme.headhunting.domain.resume.controller;

import com.epages.restdocs.apispec.FieldDescriptors;
import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.common.service.StorageService;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.member.controller.AuthController;
import me.noteme.headhunting.domain.resume.controller.request.ResumeBasicRequest;
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

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
    @DisplayName("이력서_생성_테스트")
    void 이력서_생성_테스트() throws Exception {
        // * GIVEN: 테스트 준비
        long memberId = 1L;

        // * WHEN: API 호출
        ResultActions actions = mockMvc.perform(post("/api/v1/resume")
                .contentType("application/json")
        );

        // * THEN: 기대 결과 검증
        actions.andExpect(status().isCreated())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서 생성")
                                .summary("이력서 생성 API")
                                .description("로그인된 사용자의 새로운 이력서를 생성합니다.")
                                .responseFields(empty())
                                .build()
                )));

        // 서비스 메서드 호출 검증
        verify(resumeService).resumeInit(memberId);
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
                                        fieldWithPath("resumeId").type(JsonFieldType.NUMBER).description("이력서 ID"),
                                        fieldWithPath("jobId").type(JsonFieldType.NUMBER).description("직무 ID"),
                                        fieldWithPath("profile").type(JsonFieldType.STRING).optional().description("프로필 이미지 URL"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("이력서 제목"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일 주소"),
                                        fieldWithPath("birth").type(JsonFieldType.STRING).description("생년월일"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("핸드폰 번호"),
                                        fieldWithPath("introduce").type(JsonFieldType.STRING).description("한 줄 소개"),
                                        fieldWithPath("portfolioUrl").type(JsonFieldType.STRING).optional().description("포트폴리오 URL"),
                                        fieldWithPath("resumeBasicId").type(JsonFieldType.NUMBER).optional().description("이력서 기본 ID (새로 추가 시 null)")
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
    @DisplayName("이력서_PDF_업로드_테스트")
    void 이력서_PDF_업로드_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        String fileName = "resume.pdf";
        Resource source = loader.getResource("classpath:/media/" + fileName);
        MockMultipartFile file = new MockMultipartFile("file", fileName, "application/pdf", source.getInputStream());

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(multipart("/api/v1/resume/pdf")
                        .file(file)
                        .contentType("multipart/form-data")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서 관리")
                                .summary("이력서 PDF 업로드 API")
                                .description("PDF 파일을 업로드합니다.")
                                .responseFields(empty())
                                .build()
                )));
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