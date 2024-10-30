package me.noteme.headhunting.domain.resume.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.resume.controller.request.ExperienceForm;
import me.noteme.headhunting.domain.resume.controller.request.ExperienceRequest;
import me.noteme.headhunting.domain.resume.entity.ExperienceType;
import me.noteme.headhunting.domain.resume.service.ExperienceService;
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

@DisplayName("이력서 활동 관련 컨트롤러 테스트")
@WebMvcTest(value = ExperienceController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTFilter.class),
        }
)
public class ExperienceControllerTest extends RestDocsSupport {

    @MockBean
    private ExperienceService experienceService;

    @Autowired
    private ResourceLoader loader;

    @Test
    @DisplayName("경험_업데이트_테스트")
    void 경험_업데이트_테스트() throws Exception {
        // * GIVEN: 테스트 요청 데이터 생성
        ExperienceRequest request = new ExperienceRequest();
        List<ExperienceForm> experienceForms = List.of(
                new ExperienceForm(1L, ExperienceType.PROJECT, "프로젝트 경험", "회사명", LocalDate.of(2021, 3, 1), LocalDate.of(2022, 3, 1), "설명", "https://example.com", 1L),
                new ExperienceForm(1L, ExperienceType.ACTIVITY, "봉사 활동", "봉사단체", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 6, 1), "설명", "https://volunteer.com", 2L)
        );
        request.setExperiences(experienceForms);

        // * WHEN: API 호출
        ResultActions actions = mockMvc.perform(post("/api/v1/experience")
                .contentType("application/json")
                .content(toJson(request))
        );

        // * THEN: 기대 결과 검증
        actions.andExpect(status().isCreated())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("경험 필드 생성")
                                .summary("경험 업데이트 API")
                                .description("경험 정보를 업데이트합니다.")
                                .requestFields(
                                        fieldWithPath("experiences[].resumeId").type(JsonFieldType.NUMBER).description("이력서 ID"),
                                        fieldWithPath("experiences[].experienceType").type(JsonFieldType.STRING).description("경험 유형 (PROJECT, VOLUNTEER 등)"),
                                        fieldWithPath("experiences[].title").type(JsonFieldType.STRING).description("경험 제목"),
                                        fieldWithPath("experiences[].affiliation").type(JsonFieldType.STRING).description("회사/기관 이름"),
                                        fieldWithPath("experiences[].startAt").type(JsonFieldType.STRING).description("경험 시작일"),
                                        fieldWithPath("experiences[].endAt").type(JsonFieldType.STRING).optional().description("경험 종료일"),
                                        fieldWithPath("experiences[].description").type(JsonFieldType.STRING).optional().description("경험 설명"),
                                        fieldWithPath("experiences[].referenceUrl").type(JsonFieldType.STRING).optional().description("참조 URL"),
                                        fieldWithPath("experiences[].experienceId").type(JsonFieldType.NUMBER).optional().description("경험 ID (새로 추가 시 null)")
                                ).responseFields(empty())
                                .build()
                )));

        verify(experienceService).saveAllExperience(request.getExperiences());
    }
}
