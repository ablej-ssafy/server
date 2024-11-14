package me.noteme.headhunting.domain.recruitment.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.annotation.CustomMockUser;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.recruitment.dto.CategoryResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
import me.noteme.headhunting.domain.recruitment.entity.MockRecruitment;
import me.noteme.headhunting.domain.recruitment.entity.Recruitment;
import me.noteme.headhunting.domain.recruitment.service.RecruitmentService;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("채용 공고 컨트롤러 테스트")
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
    @DisplayName("채용공고_조회_테스트")
    @CustomMockUser
    void 채용공고_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        Long recruitmentId = 1L;
        Recruitment recruitment = MockRecruitment.create(recruitmentId);
        RecruitmentResponse response = RecruitmentResponse.fromEntity(recruitment, false);
        when(recruitmentService.getRecruitmentById(memberId, recruitmentId)).thenReturn(response);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
            get("/api/v1/recruitments/{recruitmentId}", recruitmentId)
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("채용 공고")
                                .summary("채용 공고 조회 API")
                                .description("검색한 ID 기반으로 채용 공고를 조회합니다.")
                                .pathParameters(
                                        parameterWithName("recruitmentId").type(SimpleType.NUMBER).description("조회할 채용 공고 ID")
                                ).responseFields(response(
                                        fieldWithPath("data.recruitmentId").type(JsonFieldType.NUMBER).description("채용 공고 ID"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("채용 공고명"),
                                        fieldWithPath("data.category.id").type(JsonFieldType.NUMBER).description("직업 카테고리 ID"),
                                        fieldWithPath("data.category.name").type(JsonFieldType.STRING).description("직업 카테고리 이름"),
                                        fieldWithPath("data.childCategories[].id").type(JsonFieldType.NUMBER).description("하위 직업 카테고리 ID"),
                                        fieldWithPath("data.childCategories[].name").type(JsonFieldType.STRING).description("하위 직업 카테고리 이름"),
                                        fieldWithPath("data.images").type(JsonFieldType.ARRAY).description("채용 공고 이미지 URL 목록"),
                                        fieldWithPath("data.company.companyId").type(JsonFieldType.NUMBER).description("기업 ID"),
                                        fieldWithPath("data.company.name").type(JsonFieldType.STRING).description("기업명"),
                                        fieldWithPath("data.company.thumbnail").type(JsonFieldType.STRING).description("기업 소개용 썸네일"),
                                        fieldWithPath("data.company.address").type(JsonFieldType.STRING).description("기업 주소"),
                                        fieldWithPath("data.company.roadAddress").type(JsonFieldType.STRING).description("기업 도로명 주소"),
                                        fieldWithPath("data.company.latitude").type(JsonFieldType.NUMBER).description("기업 위치 위도"),
                                        fieldWithPath("data.company.longitude").type(JsonFieldType.NUMBER).description("기업 위치 경도"),
                                        fieldWithPath("data.company.location").type(JsonFieldType.STRING).description("기업 위치 지역 (서울)"),
                                        fieldWithPath("data.company.strict").type(JsonFieldType.STRING).description("기업 위치 구역 (서초구)"),
                                        fieldWithPath("data.intro").type(JsonFieldType.STRING).optional().description("기업 소개"),
                                        fieldWithPath("data.task").type(JsonFieldType.STRING).optional().description("담당 업무"),
                                        fieldWithPath("data.requirement").type(JsonFieldType.STRING).optional().description("자격 요건"),
                                        fieldWithPath("data.preference").type(JsonFieldType.STRING).optional().description("우대 조건"),
                                        fieldWithPath("data.benefit").type(JsonFieldType.STRING).optional().description("복지/혜택"),
                                        fieldWithPath("data.hireRound").type(JsonFieldType.STRING).optional().description("채용 프로세스"),
                                        fieldWithPath("data.dueTime").type(JsonFieldType.STRING).optional().description("마감일"),
                                        fieldWithPath("data.annualTo").type(JsonFieldType.NUMBER).description("연차 상한"),
                                        fieldWithPath("data.annualFrom").type(JsonFieldType.NUMBER).description("연차 하한"),
                                        fieldWithPath("data.scrapped").type(JsonFieldType.BOOLEAN).description("스크랩 여부 (비로그인 시 false)")
                                ))
                                .build()
                )));
    }

    @Test
    @DisplayName("카테고리_ID_기반_조회_테스트")
    @CustomMockUser
    void 카테고리_ID_기반_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        Long categoryId = 1L;
        int size = 20;
        Pageable pageable = Pageable.ofSize(size);
        AtomicLong id = new AtomicLong(1);
        List<RecruitmentSummaryResponse> recruitments = Stream.of("에이블제이 백엔드", "호두에이아이랩", "[인텔리전스랩스] 넥슨크리에이터즈팀 백엔드 개발자 (Java)", "카펜스트리트(에이콘3D)").map(name -> {
            Long recruitmentId = id.getAndIncrement();
            Recruitment recruitment = MockRecruitment.create(recruitmentId, name, recruitmentId);
            return RecruitmentSummaryResponse.fromEntity(recruitment);
        }).toList();
        Page<RecruitmentSummaryResponse> response = new PageImpl<>(recruitments, pageable, size);
        when(recruitmentService.getRecruitmentsByCategoryId(memberId, categoryId, pageable)).thenReturn(response);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
            get("/api/v1/recruitments/category/{categoryId}", categoryId)
                .queryParam("page", "0")
                .queryParam("size", "20")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("채용 공고")
                                .summary("카테고리 기반 채용 공고 조회 API")
                                .description("카테고리 ID를 기반으로 채용 공고를 조회합니다.")
                                .pathParameters(
                                        parameterWithName("categoryId").type(SimpleType.NUMBER).description("조회할 카테고리 ID")
                                ).queryParameters(
                                        parameterWithName("page").type(SimpleType.NUMBER).defaultValue(0).description("페이지 번호"),
                                        parameterWithName("size").type(SimpleType.NUMBER).defaultValue(20).description("페이지 크기")
                                ).responseFields(response(page(
                                        fieldWithPath("data.content[].recruitmentId").type(JsonFieldType.NUMBER).description("채용 공고 ID"),
                                        fieldWithPath("data.content[].name").type(JsonFieldType.STRING).description("채용 공고명"),
                                        fieldWithPath("data.content[].category").type(JsonFieldType.STRING).description("직업 카테고리 이름"),
                                        fieldWithPath("data.content[].companyId").type(JsonFieldType.NUMBER).description("기업 ID"),
                                        fieldWithPath("data.content[].companyName").type(JsonFieldType.STRING).description("기업명"),
                                        fieldWithPath("data.content[].thumbnail").type(JsonFieldType.STRING).description("기업 소개용 썸네일"),
                                        fieldWithPath("data.content[].location").type(JsonFieldType.STRING).description("기업 위치 지역 (서울)"),
                                        fieldWithPath("data.content[].strict").type(JsonFieldType.STRING).description("기업 위치 구역 (서초구)"),
                                        fieldWithPath("data.content[].scrapped").type(JsonFieldType.BOOLEAN).description("스크랩 여부 (비로그인 시 false)")
                                )))
                                .build()
                )));
    }

    @Test
    @DisplayName("직무_목록_전체_조회_테스트")
    void 직무_목록_전체_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        AtomicLong id = new AtomicLong(1);
        List<CategoryResponse> response = Stream.of("백엔드 개발자", "프론트엔드 개발자", "풀스택 개발자").map(
                title -> CategoryResponse.of(id.getAndIncrement(), title)
        ).toList();

        when(recruitmentService.getJobCategories()).thenReturn(response);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                get("/api/v1/recruitments/category")
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
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("직무 제목")
                                ))
                                .build()
                )));
    }

    @Test
    @DisplayName("채용공고_목록_전체_조회_테스트")
    void 채용공고_목록_전체_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        int size = 20;
        Pageable pageable = Pageable.ofSize(size);
        AtomicLong id = new AtomicLong(1);
        List<RecruitmentSummaryResponse> recruitments = Stream.of("에이블제이 백엔드", "호두에이아이랩", "[인텔리전스랩스] 넥슨크리에이터즈팀 백엔드 개발자 (Java)", "카펜스트리트(에이콘3D)").map(name -> {
            Long recruitmentId = id.getAndIncrement();
            Recruitment recruitment = MockRecruitment.create(recruitmentId, name, recruitmentId);
            return RecruitmentSummaryResponse.fromEntity(recruitment);
        }).toList();
        Page<RecruitmentSummaryResponse> response = new PageImpl<>(recruitments, pageable, size);
        when(recruitmentService.getRecruitments(null, pageable)).thenReturn(response);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                get("/api/v1/recruitments")
                        .queryParam("page", "0")
                        .queryParam("size", "20")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(
                        this.restDocs.document(resource(
                                ResourceSnippetParameters.builder()
                                        .tag("채용 공고")
                                        .summary("채용 공고 전체 조회 API")
                                        .description("전체 채용 공고 목록을 조회합니다.")
                                        .queryParameters(
                                                parameterWithName("page").type(SimpleType.NUMBER).defaultValue(0).description("페이지 번호"),
                                                parameterWithName("size").type(SimpleType.NUMBER).defaultValue(20).description("페이지 크기")
                                        ).responseFields(response(page(
                                                fieldWithPath("data.content[].recruitmentId").type(JsonFieldType.NUMBER).description("채용 공고 ID"),
                                                fieldWithPath("data.content[].name").type(JsonFieldType.STRING).description("채용 공고명"),
                                                fieldWithPath("data.content[].category").type(JsonFieldType.STRING).description("직업 카테고리 이름"),
                                                fieldWithPath("data.content[].companyId").type(JsonFieldType.NUMBER).description("기업 ID"),
                                                fieldWithPath("data.content[].companyName").type(JsonFieldType.STRING).description("기업명"),
                                                fieldWithPath("data.content[].thumbnail").type(JsonFieldType.STRING).description("기업 소개용 썸네일"),
                                                fieldWithPath("data.content[].location").type(JsonFieldType.STRING).description("기업 위치 지역 (서울)"),
                                                fieldWithPath("data.content[].strict").type(JsonFieldType.STRING).description("기업 위치 구역 (서초구)"),
                                                fieldWithPath("data.content[].scrapped").type(JsonFieldType.BOOLEAN).description("스크랩 여부 (비로그인 시 false)")
                                        )))
                                        .build()
                        )));

    }

    @Test
    @DisplayName("채용공고_스크랩_생성_테스트")
    @CustomMockUser
    void 채용공고_스크랩_생성_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        Long recruitmentId = 1L;

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                post("/api/v1/recruitments/{recruitmentId}/scrap", recruitmentId)
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("채용 공고")
                                .summary("채용 공고 스크랩 생성 API")
                                .description("로그인한 사용자가 채용 공고를 스크랩합니다.")
                                .pathParameters(
                                        parameterWithName("recruitmentId").type(SimpleType.NUMBER).description("스크랩할 채용 공고 ID")
                                ).responseFields(empty())
                                .build()
                )));

        verify(recruitmentService).scrapRecruitment(memberId, recruitmentId);
    }

    @Test
    @DisplayName("채용공고_스크랩_삭제_테스트")
    @CustomMockUser
    void 채용공고_스크랩_삭제_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        Long recruitmentId = 1L;

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                delete("/api/v1/recruitments/{recruitmentId}/scrap", recruitmentId)
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isNoContent())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("채용 공고")
                                .summary("채용 공고 스크랩 삭제 API")
                                .description("로그인한 사용자가 채용 공고 스크랩을 삭제합니다.")
                                .pathParameters(
                                        parameterWithName("recruitmentId").type(SimpleType.NUMBER).description("삭제할 채용 공고 ID")
                                )
                                .build()
                )));

        verify(recruitmentService).unScrapRecruitment(memberId, recruitmentId);
    }

    @Test
    @DisplayName("채용공고_스크랩_여부_조회_테스트")
    @CustomMockUser
    void 채용공고_스크랩_여부_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        Long recruitmentId = 1L;
        when(recruitmentService.isScrapped(memberId, recruitmentId)).thenReturn(true);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                get("/api/v1/recruitments/{recruitmentId}/scrap", recruitmentId)
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("채용 공고")
                                .summary("채용 공고 스크랩 여부 조회 API")
                                .description("로그인한 사용자가 채용 공고를 스크랩했는지 여부를 조회합니다.")
                                .pathParameters(
                                        parameterWithName("recruitmentId").type(SimpleType.NUMBER).description("조회할 채용 공고 ID")
                                ).responseFields(response(
                                        fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("스크랩 여부")
                                ))
                                .build()
                )));
    }

    @Test
    @DisplayName("채용공고_여러개_스크랩_여부_조회_테스트")
    @CustomMockUser
    void 채용공고_여러개_스크랩_여부_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        List<Long> recruitmentIds = List.of(1L, 2L, 3L);
        when(recruitmentService.isScrapped(memberId, recruitmentIds)).thenReturn(List.of(1L, 3L));

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                get("/api/v1/recruitments/scraps")
                        .queryParam("recruitmentIds", "1, 2, 3")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("채용 공고")
                                .summary("채용 공고 여러개 스크랩 여부 조회 API")
                                .description("로그인한 사용자가 여러개의 채용 공고를 스크랩했는지 여부를 조회합니다.")
                                .queryParameters(
                                        parameterWithName("recruitmentIds").description("조회할 채용 공고 ID 목록")
                                ).responseFields(response(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("스크랩 여부 목록")
                                ))
                                .build()
                )));
    }
}