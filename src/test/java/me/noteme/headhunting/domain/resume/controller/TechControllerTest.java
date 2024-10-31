package me.noteme.headhunting.domain.resume.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.annotation.CustomMockUser;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.resume.controller.request.ReferenceUrlRequest;
import me.noteme.headhunting.domain.resume.controller.request.TechSkillRequest;
import me.noteme.headhunting.domain.resume.controller.request.TechStackRequest;
import me.noteme.headhunting.domain.resume.dto.ReferenceUrlResponse;
import me.noteme.headhunting.domain.resume.dto.TechResponse;
import me.noteme.headhunting.domain.resume.dto.TechSkillResponse;
import me.noteme.headhunting.domain.resume.service.TechService;
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

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("이력서 테크 컨트롤러 테스트")
@WebMvcTest(value = TechController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTFilter.class),
        }
)
public class TechControllerTest extends RestDocsSupport {
    @MockBean
    private TechService techService;

    @Autowired
    private ResourceLoader loader;

    @Test
    @DisplayName("기술_스택_업데이트_테스트")
    void 기술_스택_업데이트_테스트() throws Exception {
        // * GIVEN: 테스트 요청 데이터 생성
        TechStackRequest request = new TechStackRequest();
        request.setResumeId(1L);
        request.setReferenceUrls(List.of(
                ReferenceUrlRequest.of(1L, "https://example.com/project1"),
                ReferenceUrlRequest.of(2L, "https://example.com/project2")
        ));
        request.setTechSkills(List.of(1L, 2L));
        request.setTechStackId(1L);

        // * WHEN: API 호출
        ResultActions actions = this.mockMvc.perform(post("/api/v1/tech/stack")
                .contentType("application/json")
                .content(toJson(request))
        );

        // * THEN: 기대 결과 검증
        actions.andExpect(status().isCreated())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("기술 스택 생성")
                                .summary("기술 스택 업데이트 API")
                                .description("기술 스택 정보를 업데이트합니다.")
                                .requestFields(
                                        fieldWithPath("resumeId").type(JsonFieldType.NUMBER).description("이력서 PK"),
                                        fieldWithPath("referenceUrls").type(JsonFieldType.ARRAY).optional().description("참조 URL 목록"),
                                        fieldWithPath("referenceUrls[].id").type(JsonFieldType.NUMBER).description("참조 URL PK"),
                                        fieldWithPath("referenceUrls[].url").type(JsonFieldType.STRING).description("참조 URL"),
                                        fieldWithPath("techSkills").type(JsonFieldType.ARRAY).optional().description("기술 PK 목록"),
                                        fieldWithPath("techStackId").type(JsonFieldType.NUMBER).optional().description("기술 스택 PK (새로 추가 시 null)")
                                ).responseFields(empty())
                                .build()
                )));

        verify(techService).saveTechStack(
                request.getResumeId(),
                request.getReferenceUrls(),
                request.getTechSkills(),
                request.getTechStackId()
        );
    }

    @Test
    @DisplayName("기술_스킬_업데이트_테스트")
    void 기술_스킬_업데이트_테스트() throws Exception {
        // * GIVEN: 테스트 요청 데이터 생성
        TechSkillRequest request = new TechSkillRequest();
        request.setName("Java");
        request.setIconUrl("https://example.com/icon/java.png");

        // * WHEN: API 호출
        ResultActions actions = this.mockMvc.perform(post("/api/v1/tech/skill")
                .contentType("application/json")
                .content(toJson(request))
        );

        // * THEN: 기대 결과 검증
        actions.andExpect(status().isCreated())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("기술 스킬 생성")
                                .summary("기술 스킬 생성 API")
                                .description("기술 스킬 정보를 추가합니다.")
                                .requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("기술 스킬 이름"),
                                        fieldWithPath("iconUrl").type(JsonFieldType.STRING).description("기술 스킬 아이콘 URL")
                                ).responseFields(empty())
                                .build()
                )));

        verify(techService).saveTechSkill(
                request.getName(),
                request.getIconUrl()
        );
    }

    @Test
    @DisplayName("이력서_기술_스택_조회_테스트")
    @CustomMockUser
    void 이력서_기술_스택_조회_테스트() throws Exception {
        // * GIVEN: 테스트 요청 데이터 생성
        Long userId = 1L;
        TechResponse mockResponse = TechResponse.of(
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

        when(techService.getTechStack(userId))
                .thenReturn(mockResponse);

        // * WHEN: API 호출
        ResultActions actions = mockMvc.perform(
                get("/api/v1/tech/stack")
        );

        // * THEN: 기대 결과 검증
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("기술 스택 조회")
                                .summary("로그인 사용자 기반 기술 스택 조회 API")
                                .description("로그인한 사용자의 기술 스택 작성 정보를 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data.resumeId").type(JsonFieldType.NUMBER).description("이력서 PK"),
                                        fieldWithPath("data.techId").type(JsonFieldType.NUMBER).description("기술 스택 PK"),
                                        fieldWithPath("data.techSkills[].skillId").type(JsonFieldType.NUMBER).description("기술 PK"),
                                        fieldWithPath("data.techSkills[].skillName").type(JsonFieldType.STRING).description("기술 이름"),
                                        fieldWithPath("data.techSkills[].skillIcon").type(JsonFieldType.STRING).description("기술 아이콘 URL"),
                                        fieldWithPath("data.referenceUrls[].referenceUrlId").type(JsonFieldType.NUMBER).description("참조 URL PK"),
                                        fieldWithPath("data.referenceUrls[].url").type(JsonFieldType.STRING).description("참조 URL")
                                )).build()
                )));
    }

    @Test
    @DisplayName("전체_기술_목록_조회_테스트")
    void 전체_기술_목록_조회_테스트() throws Exception {
        // * GIVEN: 테스트 요청 데이터 생성
        List<TechSkillResponse> mockResponse = List.of(
                TechSkillResponse.of(1L, "Java", "java-icon.png"),
                TechSkillResponse.of(2L, "Spring", "spring-icon.png"),
                TechSkillResponse.of(3L, "Docker", "docker-icon.png")
        );

        when(techService.getAllTechSkills())
                .thenReturn(mockResponse);

        // * WHEN: API 호출
        ResultActions actions = mockMvc.perform(
                get("/api/v1/tech/skill")
        );

        // * THEN: 기대 결과 검증
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("기술 목록 조회")
                                .summary("모든 기술 목록 조회 API")
                                .description("전체 기술 목록을 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data[].skillId").type(JsonFieldType.NUMBER).description("기술 PK"),
                                        fieldWithPath("data[].skillName").type(JsonFieldType.STRING).description("기술 이름"),
                                        fieldWithPath("data[].skillIcon").type(JsonFieldType.STRING).description("기술 아이콘 URL")
                                )).build()
                )));
    }
}
