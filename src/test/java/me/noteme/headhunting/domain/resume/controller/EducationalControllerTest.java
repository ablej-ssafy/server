package me.noteme.headhunting.domain.resume.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.annotation.CustomMockUser;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.resume.controller.request.EducationalForm;
import me.noteme.headhunting.domain.resume.controller.request.EducationalRequest;
import me.noteme.headhunting.domain.resume.dto.EducationalResponse;
import me.noteme.headhunting.domain.resume.dto.EnumTypeResponse;
import me.noteme.headhunting.domain.resume.entity.EducationalType;
import me.noteme.headhunting.domain.resume.entity.GradeType;
import me.noteme.headhunting.domain.resume.service.EducationalService;
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

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("이력서 교육 컨트롤러 테스트")
@WebMvcTest(value = EducationalController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTFilter.class),
        }
)
public class EducationalControllerTest extends RestDocsSupport {

    @MockBean
    private EducationalService educationalService;

    @Autowired
    private ResourceLoader loader;

    @Test
    @DisplayName("교육_업데이트_테스트")
    void 교육_업데이트_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        EducationalRequest request = new EducationalRequest();
        List<EducationalForm> educationalForms = List.of(
                EducationalForm.of(1L, "교육 이름", "전공", EducationalType.BACHELOR, "A", GradeType.FOUR_POINT_FIVE, "설명", LocalDate.of(2021, 3, 1), LocalDate.of(2021, 8, 31), 1L),
                EducationalForm.of(1L, "학위 이름", "세부 전공", EducationalType.MASTER, "B", GradeType.FOUR_POINT_ZERO, "설명", LocalDate.of(2019, 3, 1), LocalDate.of(2020, 8, 31), 2L)
        );
        request.setEducationals(educationalForms);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(post("/api/v1/educational")
                .contentType("application/json")
                .content(toJson(request))
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isCreated())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-교육")
                                .summary("학교 정보 추가 API")
                                .description("교육 정보를 업데이트합니다.")
                                .requestFields(
                                        fieldWithPath("educationals[].resumeId").type(JsonFieldType.NUMBER).description("이력서 PK"),
                                        fieldWithPath("educationals[].name").type(JsonFieldType.STRING).description("교육 이름"),
                                        fieldWithPath("educationals[].major").type(JsonFieldType.STRING).description("전공"),
                                        fieldWithPath("educationals[].category").type(JsonFieldType.STRING).description("교육 유형 (BACHELOR, MASTERS 등)"),
                                        fieldWithPath("educationals[].grade").type(JsonFieldType.STRING).optional().description("성적"),
                                        fieldWithPath("educationals[].gradeType").type(JsonFieldType.STRING).description("성적 유형 (FOUR_POINT_FIVE, FOUR_POINT_ZERO 등)"),
                                        fieldWithPath("educationals[].description").type(JsonFieldType.STRING).optional().description("설명"),
                                        fieldWithPath("educationals[].startAt").type(JsonFieldType.STRING).description("교육 시작일"),
                                        fieldWithPath("educationals[].endAt").type(JsonFieldType.STRING).optional().description("교육 종료일"),
                                        fieldWithPath("educationals[].educationalId").type(JsonFieldType.NUMBER).optional().description("교육 PK (새로 추가 시 null)")
                                ).responseFields(empty())
                                .build()
                )));

        verify(educationalService).saveAllEducationals(request.getEducationals());
    }

    @Test
    @DisplayName("교육_정보_조회_테스트")
    @CustomMockUser
    void 교육_정보_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        EducationalForm educationalForm = EducationalForm.of(
                1L,
                "바밤대학교",
                "바밤과",
                EducationalType.BACHELOR,
                "4.3",
                GradeType.FOUR_POINT_FIVE,
                "학부재밌었다.",
                LocalDate.of(2018, 3, 2),
                LocalDate.of(2024, 2, 15),
                1L
        );

        EducationalResponse mockResponse = EducationalResponse.of(List.of(educationalForm));
        when(educationalService.getAllEducationals(memberId))
                .thenReturn(mockResponse);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(
                get("/api/v1/educational")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-교육")
                                .summary("학교 정보 조회 API")
                                .description("로그인 한 사용자가 작성한 학교 정보를 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data.educationals[].resumeId").type(JsonFieldType.NUMBER).description("이력서 PK"),
                                        fieldWithPath("data.educationals[].name").type(JsonFieldType.STRING).description("학교 이름"),
                                        fieldWithPath("data.educationals[].major").type(JsonFieldType.STRING).description("전공"),
                                        fieldWithPath("data.educationals[].category").type(JsonFieldType.STRING).description("학교 타입(ASSOCIATE_DEGREE, BACHELOR, MASTER, DOCTOR)"),
                                        fieldWithPath("data.educationals[].grade").type(JsonFieldType.STRING).description("학점"),
                                        fieldWithPath("data.educationals[].gradeType").type(JsonFieldType.STRING).description("최대 학점"),
                                        fieldWithPath("data.educationals[].description").type(JsonFieldType.STRING).description("활동 내용 설명"),
                                        fieldWithPath("data.educationals[].startAt").type(JsonFieldType.STRING).description("입학 날짜"),
                                        fieldWithPath("data.educationals[].endAt").type(JsonFieldType.STRING).description("졸업 날짜"),
                                        fieldWithPath("data.educationals[].educationalId").type(JsonFieldType.NUMBER).description("학력 PK")
                                        )).build()
                )));
    }

    @Test
    @DisplayName("교육_타입_정보_조회_테스트")
    void 교육_타입_정보_조회_테스트() throws Exception {
        // * GIVEN: 교육 타입 목록이 주어졌을 때
        List<EnumTypeResponse> mockResponse = List.of(
                EnumTypeResponse.of("ASSOCIATE_DEGREE", "전문대"),
                EnumTypeResponse.of("BACHELOR", "대학"),
                EnumTypeResponse.of("MASTER", "석사"),
                EnumTypeResponse.of("DOCTOR", "박사")
        );

        when(educationalService.getEducationTypes()).thenReturn(mockResponse);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(get("/api/v1/educational/type"));

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-교육")
                                .summary("교육 타입 확인 API")
                                .description("사용 가능한 교육 타입 정보를 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data[].code").type(JsonFieldType.STRING).description("교육 타입 코드 (예: ASSOCIATE_DEGREE, BACHELOR, MASTER, DOCTOR)"),
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("교육 타입 이름 (예: 전문대, 대학, 석사, 박사)")
                                )).build()
                )));
    }
}
