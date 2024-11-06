package me.noteme.headhunting.domain.resume.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.annotation.CustomMockUser;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.resume.controller.request.ExperienceForm;
import me.noteme.headhunting.domain.resume.controller.request.ExperienceRequest;
import me.noteme.headhunting.domain.resume.dto.EnumTypeResponse;
import me.noteme.headhunting.domain.resume.dto.ExperienceResponse;
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

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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
        // * GIVEN: 이런게 주어졌을 때
        ExperienceRequest request = new ExperienceRequest();
        List<ExperienceForm> experienceForms = List.of(
                ExperienceForm.of(1L, ExperienceType.PROJECT, "프로젝트 경험", "회사명", LocalDate.of(2021, 3, 1), LocalDate.of(2022, 3, 1), "설명", "https://example.com", 1L),
                ExperienceForm.of(1L, ExperienceType.ACTIVITY, "봉사 활동", "봉사단체", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 6, 1), "설명", "https://volunteer.com", 2L)
        );
        request.setExperiences(experienceForms);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(post("/api/v1/experience")
                .contentType("application/json")
                .content(toJson(request))
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isCreated())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-경험")
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

    @Test
    @DisplayName("경험_정보_전체_조회_테스트")
    @CustomMockUser
    void 경험_정보_전체_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        ExperienceForm experienceForm = ExperienceForm.of(
                1L,
                ExperienceType.COMPANY,
                "활동 이름",
                "소속",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2021, 1, 1),
                "활동 설명",
                "http://example.com",
                100L
        );

        ExperienceResponse mockResponse = ExperienceResponse.of(List.of(experienceForm));
        when(experienceService.getExperiences(memberId, null))
                .thenReturn(mockResponse);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(
                get("/api/v1/experience")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-경험")
                                .summary("경험 전체 조회 API")
                                .description("로그인 한 사용자가 작성한 경험 정보를 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data.experiences[].resumeId").type(JsonFieldType.NUMBER).description("이력서 PK"),
                                        fieldWithPath("data.experiences[].experienceType").type(JsonFieldType.STRING).description("경험 타입 (COMPANY, PROJECT, ACTIVITY)"),
                                        fieldWithPath("data.experiences[].title").type(JsonFieldType.STRING).description("활동 이름"),
                                        fieldWithPath("data.experiences[].affiliation").type(JsonFieldType.STRING).description("활동 소속"),
                                        fieldWithPath("data.experiences[].startAt").type(JsonFieldType.STRING).description("활동 시작 날짜"),
                                        fieldWithPath("data.experiences[].endAt").type(JsonFieldType.STRING).description("활동 종료 날짜"),
                                        fieldWithPath("data.experiences[].description").type(JsonFieldType.STRING).description("활동 내용 설명"),
                                        fieldWithPath("data.experiences[].referenceUrl").type(JsonFieldType.STRING).description("참조 URL"),
                                        fieldWithPath("data.experiences[].experienceId").type(JsonFieldType.NUMBER).description("경험 ID")
                                )).build()
                )));
    }

    @Test
    @DisplayName("경험_타입지정_정보_전체_조회_테스트")
    @CustomMockUser
    void 경험_타입지정_정보_전체_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        ExperienceForm experienceForm = ExperienceForm.of(
                1L,
                ExperienceType.COMPANY,
                "활동 이름",
                "소속",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2021, 1, 1),
                "활동 설명",
                "http://example.com",
                100L
        );

        ExperienceResponse mockResponse = ExperienceResponse.of(List.of(experienceForm));
        when(experienceService.getExperiences(memberId, ExperienceType.PROJECT.toString()))
                .thenReturn(mockResponse);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(
                get("/api/v1/experience").queryParam("type", ExperienceType.PROJECT.toString())
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-경험")
                                .summary("경험 타입 지정 조회 API")
                                .description("로그인 한 사용자가 작성한 경험 정보(회사, 프로젝트, 대내외활동)를 조회합니다.")
                                .queryParameters(
                                        parameterWithName("type").description("조회할 경험을 작성합니다. (예: company, activity, project")
                                )
                                .responseFields(response(
                                        fieldWithPath("data.experiences[].resumeId").type(JsonFieldType.NUMBER).description("이력서 PK"),
                                        fieldWithPath("data.experiences[].experienceType").type(JsonFieldType.STRING).description("경험 타입 (COMPANY, PROJECT, ACTIVITY)"),
                                        fieldWithPath("data.experiences[].title").type(JsonFieldType.STRING).description("활동 이름"),
                                        fieldWithPath("data.experiences[].affiliation").type(JsonFieldType.STRING).description("활동 소속"),
                                        fieldWithPath("data.experiences[].startAt").type(JsonFieldType.STRING).description("활동 시작 날짜"),
                                        fieldWithPath("data.experiences[].endAt").type(JsonFieldType.STRING).description("활동 종료 날짜"),
                                        fieldWithPath("data.experiences[].description").type(JsonFieldType.STRING).description("활동 내용 설명"),
                                        fieldWithPath("data.experiences[].referenceUrl").type(JsonFieldType.STRING).description("참조 URL"),
                                        fieldWithPath("data.experiences[].experienceId").type(JsonFieldType.NUMBER).description("경험 ID")
                                )).build()
                )));
    }

    @Test
    @DisplayName("경험_타입_목록_조회_테스트")
    void 경험_타입_목록_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        List<EnumTypeResponse> mockResponse = List.of(
                EnumTypeResponse.of("COMPANY", "회사"),
                EnumTypeResponse.of("PROJECT", "프로젝트"),
                EnumTypeResponse.of("ACTIVITY", "대내외활동")
        );

        when(experienceService.getExperienceTypes()).thenReturn(mockResponse);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(get("/api/v1/experience/type"));

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-경험")
                                .summary("경험 타입 목록 조회 API")
                                .description("사용 가능한 경험 타입 목록을 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data[].code").type(JsonFieldType.STRING).description("경험 타입 코드 (예: COMPANY, PROJECT, ACTIVITY)"),
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("경험 타입 이름 (예: 회사, 프로젝트, 대내외활동)")
                                )).build()
                )));
    }

    @Test
    @DisplayName("경험_정보_삭제_테스트")
    void 경험_정보_삭제_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long experienceId = 1L;
        doNothing().when(experienceService).deleteById(experienceId);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(
                delete("/api/v1/experience/{experienceId}", experienceId)
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isNoContent())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-경험")
                                .summary("경험 삭제 API")
                                .description("경험 정보를 삭제합니다.")
                                .build()
                )));
        verify(experienceService).deleteById(experienceId);
    }
}
