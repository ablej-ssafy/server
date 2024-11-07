package me.noteme.headhunting.domain.resume.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.annotation.CustomMockUser;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.resume.controller.request.CertificationForm;
import me.noteme.headhunting.domain.resume.controller.request.CertificationRequest;
import me.noteme.headhunting.domain.resume.dto.CertificationResponse;
import me.noteme.headhunting.domain.resume.entity.CertificationType;
import me.noteme.headhunting.domain.resume.service.CertificationService;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.ResourceLoader;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("이력서 자격증 컨트롤러 테스트")
@WebMvcTest(value = CertificationController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTFilter.class),
        }
)
public class CertificationControllerTest extends RestDocsSupport {
    @MockBean
    private CertificationService certificationService;

    @Test
    @DisplayName("자격증_업데이트_테스트")
    @CustomMockUser
    void 자격증_업데이트_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;

        CertificationRequest request = new CertificationRequest();
        List<CertificationForm> certificationForms = List.of(
                CertificationForm.of("자격증 이름", "발행 기관", "인증 번호", LocalDate.of(2020, 5, 20), "A", CertificationType.QUALIFICATION, 1L),
                CertificationForm.of("어학 이름", "발행 기관", "인증 번호", LocalDate.of(2019, 8, 15), "B", CertificationType.LANGUAGE, 1L)
        );
        request.setCertifications(certificationForms);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(post("/api/v1/certification")
                .contentType("application/json")
                .content(toJson(request))
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isCreated())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-자격증")
                                .summary("자격 정보 업데이트 API")
                                .description("자격증 정보를 업데이트합니다.")
                                .requestFields(
                                        fieldWithPath("certifications[].name").type(JsonFieldType.STRING).description("자격증 이름"),
                                        fieldWithPath("certifications[].organization").type(JsonFieldType.STRING).description("발행 기관"),
                                        fieldWithPath("certifications[].credential").type(JsonFieldType.STRING).optional().description("인증 번호"),
                                        fieldWithPath("certifications[].acquisitionAt").type(JsonFieldType.STRING).description("취득 날짜"),
                                        fieldWithPath("certifications[].grade").type(JsonFieldType.STRING).optional().description("점수 또는 등급"),
                                        fieldWithPath("certifications[].certificationType").type(JsonFieldType.STRING).description("자격증 유형 (QUALIFICATION, LANGUAGE)"),
                                        fieldWithPath("certifications[].certificationId").type(JsonFieldType.NUMBER).optional().description("자격증 ID (새로 추가 시 null)")
                                ).responseFields(empty())
                                .build()
                )));

        verify(certificationService).saveAllCertifications(memberId, request.getCertifications());
    }

    @Test
    @DisplayName("전체_자격증_조회_테스트")
    @CustomMockUser
    void 전체_자격증_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        CertificationForm certificationForm = CertificationForm.of(
                "정보처리기사",
                "한국산업인력공단",
                "12345678",
                LocalDate.of(2022, 5, 1),
                "1급",
                CertificationType.QUALIFICATION,
                10L
        );

        CertificationResponse mockResponse = CertificationResponse.of(List.of(certificationForm));
        when(certificationService.getCertifications(memberId, null))
                .thenReturn(mockResponse);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(
                get("/api/v1/certification")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-자격증")
                                .summary("자격 전체 조회 API")
                                .description("로그인 한 사용자가 작성한 자격 정보를 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data.certifications[].name").type(JsonFieldType.STRING).description("자격증 이름"),
                                        fieldWithPath("data.certifications[].organization").type(JsonFieldType.STRING).description("발급 기관"),
                                        fieldWithPath("data.certifications[].credential").type(JsonFieldType.STRING).description("자격 번호"),
                                        fieldWithPath("data.certifications[].acquisitionAt").type(JsonFieldType.STRING).description("취득 날짜"),
                                        fieldWithPath("data.certifications[].grade").type(JsonFieldType.STRING).description("등급"),
                                        fieldWithPath("data.certifications[].certificationType").type(JsonFieldType.STRING).description("자격증 유형"),
                                        fieldWithPath("data.certifications[].certificationId").type(JsonFieldType.NUMBER).description("자격증 PK")
                                )).build()
                )));
    }

    @Test
    @DisplayName("자격증_타입지정_정보_전체_조회")
    @CustomMockUser
    void 자격증_타입지정_정보_전체_조회() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        CertificationForm certificationForm = CertificationForm.of(
                "정보처리기사",
                "한국산업인력공단",
                "12345678",
                LocalDate.of(2022, 5, 1),
                "1급",
                CertificationType.LANGUAGE,
                10L
        );

        CertificationResponse mockResponse = CertificationResponse.of(List.of(certificationForm));
        when(certificationService.getCertifications(memberId, CertificationType.LANGUAGE.toString()))
                .thenReturn(mockResponse);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(
                get("/api/v1/certification").queryParam("type", CertificationType.LANGUAGE.toString())
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-자격증")
                                .summary("자격증 타입 조회 API")
                                .description("로그인 한 사용자가 작성한 자격 정보(어학, 자격증)를 조회합니다.")
                                .queryParameters(
                                        parameterWithName("type").description("조회할 자격 유형을 작성합니다. (예: language, qualification")
                                )
                                .responseFields(response(
                                        fieldWithPath("data.certifications[].name").type(JsonFieldType.STRING).description("자격증 이름"),
                                        fieldWithPath("data.certifications[].organization").type(JsonFieldType.STRING).description("발급 기관"),
                                        fieldWithPath("data.certifications[].credential").type(JsonFieldType.STRING).description("자격 번호"),
                                        fieldWithPath("data.certifications[].acquisitionAt").type(JsonFieldType.STRING).description("취득 날짜"),
                                        fieldWithPath("data.certifications[].grade").type(JsonFieldType.STRING).description("등급"),
                                        fieldWithPath("data.certifications[].certificationType").type(JsonFieldType.STRING).description("자격증 유형(어학, 자격증)"),
                                        fieldWithPath("data.certifications[].certificationId").type(JsonFieldType.NUMBER).description("자격증 PK")
                                )).build()
                )));
    }

    @Test
    @DisplayName("자격_정보_삭제_테스트")
    void 자격_정보_삭제_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long certificationId = 1L;
        doNothing().when(certificationService).deleteById(certificationId);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(
                delete("/api/v1/certification/{certificationId}", certificationId)
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isNoContent())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-자격")
                                .summary("자격 삭제 API")
                                .description("자격 정보를 삭제합니다.")
                                .build()
                )));
        verify(certificationService).deleteById(certificationId);
    }
}
