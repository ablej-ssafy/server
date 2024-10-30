package me.noteme.headhunting.domain.recruitment.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.recruitment.controller.request.CompanyAnalyzeRequest;
import me.noteme.headhunting.domain.recruitment.controller.request.ResumeKeywordsRequest;
import me.noteme.headhunting.domain.recruitment.service.RecruitmentService;
import me.noteme.headhunting.domain.resume.controller.ResumeController;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("AI 채용 추천 컨트롤러 테스트")
@WebMvcTest(value = RecruitmentController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTFilter.class),
        }
)
class RecruitmentControllerTest extends RestDocsSupport {
    @MockBean
    private RecruitmentService recruitmentService;

    @Test
    @DisplayName("에이블제이_이력서_분석_테스트")
    void 에이블제이_이력서_분석_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        ResumeKeywordsRequest request = new ResumeKeywordsRequest();
        request.setJobId(1);
        request.setJobSubId(2);
        request.setResume("저장된 이력서 내용");

        List<String> keywords = List.of("키워드1", "키워드2", "키워드3");

        when(recruitmentService.getResumeKeywords(request.getJobId(), request.getJobSubId(), request.getResume()))
                .thenReturn(keywords);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
            post("/api/v1/recruitment/resume/keywords")
                    .contentType("application/json")
                    .content(toJson(request))
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("AI 채용 추천")
                                .summary("에이블제이 이력서 분석 API")
                                .description("에이블제이 이력서에서 핵심 키워드를 분석하여 반환합니다.")
                                .requestFields(
                                        fieldWithPath("jobId").type(JsonFieldType.NUMBER).description("직무 ID"),
                                        fieldWithPath("jobSubId").type(JsonFieldType.NUMBER).description("직무 세부 ID"),
                                        fieldWithPath("resume").type(JsonFieldType.STRING).description("이력서 내용")
                                ).responseFields(response(
                                        fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("이력서에서 추출된 키워드")
                                ))
                                .build()
                )));
    }

    @Test
    @DisplayName("에이블제이측_제공_기업_분석_테스트")
    void 에이블제이측_제공_기업_분석_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        String companyName = "에이블제이";

        CompanyAnalyzeRequest request = new CompanyAnalyzeRequest();
        request.setCompanyName(companyName);

        when(recruitmentService.getCompanyAnalyze(companyName))
                .thenReturn("기업 분석 결과");

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(post("/api/v1/recruitment/company/analyze")
                    .contentType("application/json")
                    .content(toJson(request)
        ));

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("AI 채용 추천")
                                .summary("에이블제이측 제공 기업 분석 API")
                                .description("에이블제이측에서 제공하는 기업 분석 결과를 반환합니다.")
                                .requestFields(
                                        fieldWithPath("companyName").type(JsonFieldType.STRING).description("기업명")
                                ).responseFields(response(
                                        fieldWithPath("data").type(JsonFieldType.STRING).description("기업 분석 결과")
                                ))
                                .build()
                )));
    }
}