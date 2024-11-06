package me.noteme.headhunting.domain.member.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.annotation.CustomMockUser;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.member.dto.LoginMemberResponse;
import me.noteme.headhunting.domain.member.service.MemberService;
import me.noteme.headhunting.domain.recruitment.dto.JobCategoryResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
import me.noteme.headhunting.domain.recruitment.entity.MockRecruitment;
import me.noteme.headhunting.domain.recruitment.entity.Recruitment;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("멤버 컨트롤러 테스트")
@WebMvcTest(value = MemberController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTFilter.class),
        }
)
public class MemberControllerTest extends RestDocsSupport {
    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("현재_로그인_사용자_조회_테스트")
    @CustomMockUser
    void 현재_로그인_사용자_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        long memberId = 1L;

        List<JobCategoryResponse> jobCategoryResponses = List.of(
                JobCategoryResponse.of(1L, "이름1"),
                JobCategoryResponse.of(2L, "이름2"),
                JobCategoryResponse.of(3L, "이름2")
        );
        LoginMemberResponse response = LoginMemberResponse.of(1L, "로그인 중인 사용자의 이름", "로그인 중인 사용자의 이메일", 0, jobCategoryResponses);

        when(memberService.info(memberId)).thenReturn(response);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                get("/api/v1/member/me")
                        .contentType("application/json")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("멤버")
                                .summary("로그인 멤버 조회 API")
                                .description("로그인 중인 멤버의 정보를 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("사용자 ID"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("사용자 이름"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                        fieldWithPath("data.career").type(JsonFieldType.NUMBER).description("사용자 커리어"),
                                        fieldWithPath("data.jobCategoryResponses").type(JsonFieldType.ARRAY).description("사용자 선호 직무 목록"),
                                        fieldWithPath("data.jobCategoryResponses[].id").type(JsonFieldType.NUMBER).description("직무 ID"),
                                        fieldWithPath("data.jobCategoryResponses[].title").type(JsonFieldType.STRING).description("직무 이름")
                                ))
                                .build()
                )));
    }

    @Test
    @DisplayName("현재_로그인_사용자_스크랩_목록_조회_테스트")
    @CustomMockUser
    void 현재_로그인_사용자_스크랩_목록_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        AtomicLong id = new AtomicLong(1);
        List<RecruitmentSummaryResponse> recruitments = Stream.of("에이블제이 백엔드", "호두에이아이랩", "[인텔리전스랩스] 넥슨크리에이터즈팀 백엔드 개발자 (Java)", "카펜스트리트(에이콘3D)").map(name -> {
            Long recruitmentId = id.getAndIncrement();
            Recruitment recruitment = MockRecruitment.create(recruitmentId, name, recruitmentId);
            return RecruitmentSummaryResponse.fromEntity(recruitment);
        }).toList();
        when(memberService.scrapList(memberId)).thenReturn(recruitments);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                get("/api/v1/member/scrap")
                        .contentType("application/json")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("멤버")
                                .summary("로그인 멤버 스크랩 목록 조회 API")
                                .description("로그인 중인 멤버가 스크랩한 채용 공고 목록을 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data[].recruitmentId").type(JsonFieldType.NUMBER).description("채용 공고 ID"),
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("채용 공고 제목"),
                                        fieldWithPath("data[].thumbnail").type(JsonFieldType.STRING).description("채용 공고 썸네일"),
                                        fieldWithPath("data[].companyId").type(JsonFieldType.NUMBER).description("채용 공고 회사 ID"),
                                        fieldWithPath("data[].companyName").type(JsonFieldType.STRING).description("채용 공고 회사명"),
                                        fieldWithPath("data[].location").type(JsonFieldType.STRING).description("채용 공고 공고 회사 지역"),
                                        fieldWithPath("data[].strict").type(JsonFieldType.STRING).description("채용 공고 공고 회사 구역"),
                                        fieldWithPath("data[].category").type(JsonFieldType.STRING).description("채용 공고 카테고리")
                                ))
                                .build()
                )));
    }
}
