package me.noteme.headhunting.domain.github.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.member.controller.GithubController;
import me.noteme.headhunting.domain.member.feign.request.RepoInfoRequest;
import me.noteme.headhunting.domain.member.feign.response.RepoAnalysisResponse;
import me.noteme.headhunting.domain.member.service.GithubService;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("깃허브 API 컨트롤러")
@WebMvcTest(value = GithubController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTFilter.class),
        }
)
public class GithubControllerTest extends RestDocsSupport {
    @MockBean
    private GithubService githubService;

    @Test
    @DisplayName("깃허브_분석_API_테스트")
    void 깃허브_분석_API_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        RepoInfoRequest request = RepoInfoRequest.of(
                null,
                "owner",
                "repo-name",
                "master",
                "ghp_ddd"
        );

        RepoAnalysisResponse mockResponse = new RepoAnalysisResponse();
        mockResponse.setRequestId("123");
        mockResponse.setState("initialized");
        mockResponse.setStep("pending");
        mockResponse.setResult(null);
        when(githubService.repoAnalysis(request.getOwner(), request.getRepo(), request.getBranch(), request.getToken()))
                .thenReturn(mockResponse);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(post("/api/v1/github/analysis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("프로젝트 분석")
                                .summary("Github 프로젝트 분석 API")
                                .description("Github 프로젝트 분석 요청 API 입니다. 처리된 결과는 메일로 전송합니다.")
                                .requestFields(
                                        fieldWithPath("owner").type(JsonFieldType.STRING).description("깃허브 리포지토리 소유자"),
                                        fieldWithPath("repo").type(JsonFieldType.STRING).description("깃허브 리포지토리 이름"),
                                        fieldWithPath("branch").type(JsonFieldType.STRING).description("깃허브 리포지토리 브랜치(default_branch)"),
                                        fieldWithPath("token").type(JsonFieldType.STRING).description("깃허브 인증 토큰(private 시 필요)")
                                ).responseFields(response(
                                        fieldWithPath("data.request_id").type(JsonFieldType.STRING).description("깃 분석 요청 ID"),
                                        fieldWithPath("data.state").type(JsonFieldType.STRING).description("요청 처리 상태"),
                                        fieldWithPath("data.step").type(JsonFieldType.STRING).description("요청 현재 진행 상황"),
                                        fieldWithPath("data.result").type(JsonFieldType.NULL).description("요청 처리 결과")
                                ))
                                .build()
                )));

        verify(githubService)
                .repoAnalysis(request.getOwner(), request.getRepo(), request.getBranch(), request.getToken());
    }

    @Test
    @DisplayName("깃허브_분석_상태_조회_API_테스트")
    void 깃허브_분석_상태_조회_API_테스트() throws Exception {
        // * GIVEN: 테스트에 필요한 데이터 설정
        String requestId = "123";
        RepoAnalysisResponse mockResponse = new RepoAnalysisResponse();
        mockResponse.setRequestId(requestId);
        mockResponse.setState("in_progress");
        mockResponse.setStep("pending");
        mockResponse.setResult(null);

        when(githubService.getRepoAnalysisStatus(requestId)).thenReturn(mockResponse);

        // * WHEN: API 호출 수행
        ResultActions actions = this.mockMvc.perform(get("/api/v1/github/analysis/{requestId}", requestId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // * THEN: 기대하는 응답 상태 및 문서 검증
        actions.andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("프로젝트 분석")
                                .summary("Github 분석 결과 확인 API")
                                .description("Github 프로젝트 분석 결과 확인 API 입니다.")
                                .pathParameters(
                                        parameterWithName("requestId").type(SimpleType.NUMBER).description("깃 분석 요청 ID")
                                ).responseFields(response(
                                        fieldWithPath("data.request_id").type(JsonFieldType.STRING).description("깃 분석 요청 ID"),
                                        fieldWithPath("data.state").type(JsonFieldType.STRING).description("요청 처리 상태"),
                                        fieldWithPath("data.step").type(JsonFieldType.STRING).description("요청 현재 진행 상황"),
                                        fieldWithPath("data.result").type(JsonFieldType.NULL).description("요청 처리 결과")
                                ))
                                .build()
                )));
    }
}
