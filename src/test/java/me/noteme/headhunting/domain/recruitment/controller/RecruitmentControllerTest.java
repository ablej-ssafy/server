package me.noteme.headhunting.domain.recruitment.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.support.RestDocsSupport;
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
import org.springframework.data.web.PagedModel;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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
    void 채용공고_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Recruitment recruitment = MockRecruitment.create(1L);
        RecruitmentResponse response = RecruitmentResponse.fromEntity(recruitment);
        when(recruitmentService.getRecruitmentById(1L)).thenReturn(response);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
            get("/api/v1/recruitment/{recruitmentId}", 1L)
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
                                        fieldWithPath("data.category.categoryId").type(JsonFieldType.NUMBER).description("직업 카테고리 ID"),
                                        fieldWithPath("data.category.name").type(JsonFieldType.STRING).description("직업 카테고리 이름"),
                                        fieldWithPath("data.childCategories[].categoryId").type(JsonFieldType.NUMBER).description("하위 직업 카테고리 ID"),
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
                                        fieldWithPath("data.annualFrom").type(JsonFieldType.NUMBER).description("연차 하한")
                                ))
                                .build()
                )));
    }

    @Test
    @DisplayName("채용공고_이름_검색_조회_테스트")
    void 채용공고_이름_검색_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        String query = "에이";
        int size = 20;
        Pageable pageable = Pageable.ofSize(size);
        AtomicLong id = new AtomicLong(1);
        List<RecruitmentSummaryResponse> recruitments = Stream.of("에이블제이 백엔드", "호두에이아이랩", "[인텔리전스랩스] 넥슨크리에이터즈팀 백엔드 개발자 (Java)", "카펜스트리트(에이콘3D)").map(name -> {
            Recruitment recruitment = MockRecruitment.create(id.getAndIncrement(), name);
            return RecruitmentSummaryResponse.fromEntity(recruitment);
        }).toList();

        Page<RecruitmentSummaryResponse> response = new PageImpl<>(recruitments, pageable, size);

        when(recruitmentService.searchRecruitments(anyString(), any(Pageable.class))).thenReturn(response);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
            get("/api/v1/recruitment")
                .queryParam("q", query)
                .queryParam("page", "0")
                .queryParam("size", "20")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("채용 공고")
                                .summary("채용 공고 검색 조회 API")
                                .description("검색어를 기반으로 채용 공고를 검색합니다.")
                                .queryParameters(
                                        parameterWithName("q").type(SimpleType.STRING).description("검색어"),
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
                                        fieldWithPath("data.content[].strict").type(JsonFieldType.STRING).description("기업 위치 구역 (서초구)")
                                )))
                                .build()
                )));
    }

    @Test
    @DisplayName("카테고리_ID_기반_조회_테스트")
    void 카테고리_ID_기반_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
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
        when(recruitmentService.getRecruitmentsByCategoryId(categoryId, pageable)).thenReturn(response);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
            get("/api/v1/recruitment/category/{categoryId}", categoryId)
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
                                        fieldWithPath("data.content[].strict").type(JsonFieldType.STRING).description("기업 위치 구역 (서초구)")
                                )))
                                .build()
                )));
    }
}