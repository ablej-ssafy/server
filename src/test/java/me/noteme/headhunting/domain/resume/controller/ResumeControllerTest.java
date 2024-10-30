package me.noteme.headhunting.domain.resume.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.annotation.CustomMockUser;
import me.noteme.headhunting.core.support.RestDocsSupport;
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

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
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