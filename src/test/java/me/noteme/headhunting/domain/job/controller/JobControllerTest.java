package me.noteme.headhunting.domain.job.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import me.noteme.headhunting.common.advice.CustomControllerAdvice;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.job.dto.JobResponse;
import me.noteme.headhunting.domain.job.service.JobService;
import me.noteme.headhunting.domain.recruitment.controller.RecruitmentController;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("직무 컨트롤러 테스트")
@WebMvcTest(value = JobController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CustomControllerAdvice.class)
        }
)
class JobControllerTest extends RestDocsSupport {
    @MockBean
    private JobService jobService;

    @Test
    @DisplayName("직무_목록_전체_조회_테스트")
    void 직무_목록_전체_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        AtomicLong id = new AtomicLong(1);
        List<JobResponse> response = Stream.of("백엔드 개발자", "프론트엔드 개발자", "풀스택 개발자").map(
                title -> JobResponse.of(id.getAndIncrement(), title)
        ).toList();

        when(jobService.getJobs()).thenReturn(response);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                get("/api/v1/jobs")
                    .contentType("application/json")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("직무")
                                .summary("관심 직무 전체 조회 API")
                                .description("관심 직무 전체 목록을 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("직무 ID"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("직무 제목")
                                ))
                                .build()
                )));
    }
}