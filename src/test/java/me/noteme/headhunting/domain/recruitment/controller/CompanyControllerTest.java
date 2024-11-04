package me.noteme.headhunting.domain.recruitment.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.recruitment.dto.CompanyRecruitmentResponse;
import me.noteme.headhunting.domain.recruitment.dto.CompanyResponse;
import me.noteme.headhunting.domain.recruitment.dto.CompanyWithRecruitmentResponse;
import me.noteme.headhunting.domain.recruitment.entity.Company;
import me.noteme.headhunting.domain.recruitment.entity.MockCompany;
import me.noteme.headhunting.domain.recruitment.entity.MockRecruitment;
import me.noteme.headhunting.domain.recruitment.entity.Recruitment;
import me.noteme.headhunting.domain.recruitment.service.CompanyService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("기업 컨트롤러 테스트")
@WebMvcTest(value = CompanyController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTFilter.class),
        }
)
class CompanyControllerTest extends RestDocsSupport {
    @MockBean
    private CompanyService companyService;

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

        when(companyService.searchCompanies(
                argThat(s -> s.equals("name")),
                argThat(s -> s.equals("회사")),
                any(Pageable.class)
        )).thenReturn(page);

        // * WHEN: 이걸 실행하면
        var actions = this.mockMvc.perform(
                get("/api/v1/company")
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
                                        parameterWithName("type").description("검색 타입 [name: 회사명, location: 지역, strict: 구역, address: 주소]"),
                                        parameterWithName("q").description("검색어"),
                                        parameterWithName("page").description("페이지 번호"),
                                        parameterWithName("size").description("페이지 크기")
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
    @DisplayName("회사_ID_기반으로_회사_정보_조회_테스트")
    void 회사_ID_기반으로_회사_정보_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long companyId = 1L;
        Company company = MockCompany.create(companyId, "회사1");
        AtomicLong id = new AtomicLong(1);
        List<Recruitment> recruitments = Stream.of("채용1", "채용2", "채용3").map(name -> MockRecruitment.create(id.getAndIncrement(), name)).toList();
        when(company.getRecruitments()).thenReturn(recruitments);
        CompanyWithRecruitmentResponse response = CompanyWithRecruitmentResponse.fromEntity(company);
        when(companyService.getCompanyById(companyId)).thenReturn(response);

        // * WHEN: 이걸 실행하면
        var actions = this.mockMvc.perform(
                get("/api/v1/company/{companyId}", companyId)
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("회사")
                                .summary("회사 ID 기반 회사 정보 조회 API")
                                .description("회사 ID를 기반으로 회사 정보와 채용 정보를 조회합니다.")
                                .pathParameters(
                                        parameterWithName("companyId").description("회사 ID")
                                ).responseFields(response(
                                        fieldWithPath("data.companyId").type(JsonFieldType.NUMBER).description("회사 ID"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("회사명"),
                                        fieldWithPath("data.thumbnail").type(JsonFieldType.STRING).description("썸네일 이미지"),
                                        fieldWithPath("data.address").type(JsonFieldType.STRING).description("주소"),
                                        fieldWithPath("data.roadAddress").type(JsonFieldType.STRING).description("도로명 주소"),
                                        fieldWithPath("data.latitude").type(JsonFieldType.NUMBER).description("위도"),
                                        fieldWithPath("data.longitude").type(JsonFieldType.NUMBER).description("경도"),
                                        fieldWithPath("data.location").type(JsonFieldType.STRING).description("지역"),
                                        fieldWithPath("data.strict").type(JsonFieldType.STRING).description("구역"),
                                        fieldWithPath("data.recruitments[].recruitmentId").type(JsonFieldType.NUMBER).description("채용 ID"),
                                        fieldWithPath("data.recruitments[].name").type(JsonFieldType.STRING).description("채용명"),
                                        fieldWithPath("data.recruitments[].thumbnail").type(JsonFieldType.STRING).description("썸네일 이미지"),
                                        fieldWithPath("data.recruitments[].annualTo").type(JsonFieldType.NUMBER).description("연차 상한"),
                                        fieldWithPath("data.recruitments[].annualFrom").type(JsonFieldType.NUMBER).description("연차 하한"),
                                        fieldWithPath("data.recruitments[].dueTime").type(JsonFieldType.STRING).optional().description("마감일")
                                )).build()
                )));
    }
}