package me.noteme.headhunting.domain.resume.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.resume.controller.request.CertificationForm;
import me.noteme.headhunting.domain.resume.controller.request.CertificationRequest;
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

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("이력서 자격증 컨트롤러 테스트")
@WebMvcTest(value = CertificationController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTFilter.class),
        }
)
public class CertificationControllerTest extends RestDocsSupport {
    @MockBean
    private CertificationService certificationService;

    @Autowired
    private ResourceLoader loader;

    @Test
    @DisplayName("자격증_업데이트_테스트")
    void 자격증_업데이트_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        CertificationRequest request = new CertificationRequest();
        List<CertificationForm> certificationForms = List.of(
                new CertificationForm(1L, "자격증 이름", "발행 기관", "인증 번호", LocalDate.of(2020, 5, 20), "A", CertificationType.QUALIFICATION, 1L),
                new CertificationForm(1L, "어학 이름", "발행 기관", "인증 번호", LocalDate.of(2019, 8, 15), "B", CertificationType.LANGUAGE, 2L)
        );
        request.setCertifications(certificationForms);

        System.out.println(toJson(request));

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(post("/api/v1/certification")
                .contentType("application/json")
                .content(toJson(request))
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isCreated())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("자격증 필드 생성")
                                .summary("자격증 업데이트 API")
                                .description("자격증 정보를 업데이트합니다.")
                                .requestFields(
                                        fieldWithPath("certifications[].resumeId").type(JsonFieldType.NUMBER).description("이력서 PK"),
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

        verify(certificationService).saveAllCertifications(request.getCertifications());
    }
}
