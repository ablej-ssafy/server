package me.noteme.headhunting.domain.recruitment.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.annotation.CustomMockUser;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.recruitment.dto.CompanyResponse;
import me.noteme.headhunting.domain.recruitment.dto.KeywordResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
import me.noteme.headhunting.domain.recruitment.dto.SearchResponse;
import me.noteme.headhunting.domain.recruitment.entity.Company;
import me.noteme.headhunting.domain.recruitment.entity.MockCompany;
import me.noteme.headhunting.domain.recruitment.entity.MockRecruitment;
import me.noteme.headhunting.domain.recruitment.entity.Recruitment;
import me.noteme.headhunting.domain.recruitment.service.SearchService;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("검색 컨트롤러 테스트")
@WebMvcTest(value = SearchController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTFilter.class),
        }
)
class SearchControllerTest extends RestDocsSupport {
    @MockBean
    private SearchService searchService;

    @Test
    @DisplayName("검색_순위_및_최근검색_조회_테스트")
    @CustomMockUser
    void 검색_순위_및_최근검색_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long userId = 1L;
        AtomicInteger rank = new AtomicInteger(1);
        List<KeywordResponse> ranks = Stream.of("쿠팡", "배민", "카카오", "네이버", "등등").map(
                keyword -> KeywordResponse.of(rank.getAndIncrement(), keyword)
        ).toList();
        AtomicInteger recent = new AtomicInteger(1);
        List<KeywordResponse> recentKeywords = Stream.of("최근", "검색", "기록", "확인", "등등").map(
                keyword -> KeywordResponse.of(recent.getAndIncrement(), keyword)
        ).toList();

        SearchResponse response = SearchResponse.of(ranks, recentKeywords);

        when(searchService.rankKeywords(userId)).thenReturn(response);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                get("/api/v1/search")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("검색")
                                .summary("검색 순위 및 최근 검색 조회 API")
                                .description("검색 순위 및 최근 검색어를 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data.ranks[].rank").type(JsonFieldType.NUMBER).description("순위"),
                                        fieldWithPath("data.ranks[].keyword").type(JsonFieldType.STRING).description("검색어"),
                                        fieldWithPath("data.recentKeywords[].rank").type(JsonFieldType.NUMBER).description("순위"),
                                        fieldWithPath("data.recentKeywords[].keyword").type(JsonFieldType.STRING).description("검색어")
                                ))
                                .build()
                )));

    }

    @Test
    @DisplayName("검색_순위_및_최근검색_비로그인_조회_테스트")
    void 검색_순위_및_최근검색_비로그인_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        AtomicInteger rank = new AtomicInteger(1);
        List<KeywordResponse> ranks = Stream.of("쿠팡", "배민", "카카오", "네이버", "등등").map(
                keyword -> KeywordResponse.of(rank.getAndIncrement(), keyword)
        ).toList();
        SearchResponse response = SearchResponse.of(ranks, List.of());

        when(searchService.rankKeywords(null)).thenReturn(response);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                get("/api/v1/search")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("검색")
                                .summary("검색 순위 및 최근 검색 조회 API")
                                .description("검색 순위 및 최근 검색어를 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data.ranks[].rank").type(JsonFieldType.NUMBER).description("순위"),
                                        fieldWithPath("data.ranks[].keyword").type(JsonFieldType.STRING).description("검색어"),
                                        fieldWithPath("data.recentKeywords").type(JsonFieldType.ARRAY).description("최근 검색어")
                                ))
                                .build()
                )));
    }

    @Test
    @DisplayName("회사_이름_기반_검색_테스트")
    void 회사_이름_기반_검색_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        AtomicLong id = new AtomicLong(1);
        int size = 20;
        Pageable pageable = Pageable.ofSize(size);
        List<CompanyResponse> companies = Stream.of("회사1", "회사2", "회사3").map(name -> {
            Company company = MockCompany.create(id.getAndIncrement(), name);
            return CompanyResponse.fromEntity(company);
        }).toList();
        Page<CompanyResponse> page = new PageImpl<>(companies, pageable, size);

        when(searchService.searchCompanies(
                nullable(Long.class),
                argThat(s -> s.equals("name")),
                argThat(s -> s.equals("회사")),
                any(Pageable.class)
        )).thenReturn(page);

        // * WHEN: 이걸 실행하면
        var actions = this.mockMvc.perform(
                get("/api/v1/search/company")
                        .param("type", "name")
                        .param("q", "회사")
                        .param("page", "0")
                        .param("size", String.valueOf(size))
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("회사")
                                .summary("회사 이름 기반 검색 API")
                                .description("회사 이름을 기반으로 검색하여 회사 목록을 반환합니다.")
                                .queryParameters(
                                        parameterWithName("type").type(SimpleType.STRING).description("검색 타입 [name: 회사명, location: 지역, strict: 구역, address: 주소]"),
                                        parameterWithName("q").type(SimpleType.STRING).description("검색어"),
                                        parameterWithName("page").type(SimpleType.NUMBER).defaultValue(0).description("페이지 번호"),
                                        parameterWithName("size").type(SimpleType.NUMBER).defaultValue(20).description("페이지 크기")
                                ).responseFields(response(page(
                                        fieldWithPath("data.content[].companyId").type(JsonFieldType.NUMBER).description("회사 ID"),
                                        fieldWithPath("data.content[].name").type(JsonFieldType.STRING).description("회사명"),
                                        fieldWithPath("data.content[].thumbnail").type(JsonFieldType.STRING).description("썸네일 이미지"),
                                        fieldWithPath("data.content[].address").type(JsonFieldType.STRING).description("주소"),
                                        fieldWithPath("data.content[].roadAddress").type(JsonFieldType.STRING).description("도로명 주소"),
                                        fieldWithPath("data.content[].latitude").type(JsonFieldType.NUMBER).description("위도"),
                                        fieldWithPath("data.content[].longitude").type(JsonFieldType.NUMBER).description("경도"),
                                        fieldWithPath("data.content[].location").type(JsonFieldType.STRING).description("지역"),
                                        fieldWithPath("data.content[].strict").type(JsonFieldType.STRING).description("구역")
                                )))
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

        when(searchService.searchRecruitments(nullable(Long.class), anyString(), any(Pageable.class))).thenReturn(response);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(
                get("/api/v1/search/recruitment")
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
                                        fieldWithPath("data.content[].strict").type(JsonFieldType.STRING).description("기업 위치 구역 (서초구)"),
                                        fieldWithPath("data.content[].scrapped").type(JsonFieldType.BOOLEAN).description("스크랩 여부 (비로그인 시 false)")
                                )))
                                .build()
                )));
    }
}