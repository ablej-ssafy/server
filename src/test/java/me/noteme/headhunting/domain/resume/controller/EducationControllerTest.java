package me.noteme.headhunting.domain.resume.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.annotation.CustomMockUser;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.resume.controller.request.EducationForm;
import me.noteme.headhunting.domain.resume.controller.request.EducationRequest;
import me.noteme.headhunting.domain.resume.dto.EducationResponse;
import me.noteme.headhunting.domain.resume.dto.EnumTypeResponse;
import me.noteme.headhunting.domain.resume.entity.EducationType;
import me.noteme.headhunting.domain.resume.entity.GradeType;
import me.noteme.headhunting.domain.resume.service.EducationService;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("이력서 교육 컨트롤러 테스트")
@WebMvcTest(value = EducationController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTFilter.class),
        }
)
public class EducationControllerTest extends RestDocsSupport {

    @MockBean
    private EducationService educationService;

    @Test
    @DisplayName("교육_업데이트_테스트")
    @CustomMockUser
    void 교육_업데이트_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        EducationRequest request = new EducationRequest();
        List<EducationForm> educationForms = List.of(
                EducationForm.of("교육 이름", "전공", EducationType.BACHELOR, "A", GradeType.FOUR_POINT_FIVE, "설명", LocalDate.of(2021, 3, 1), LocalDate.of(2021, 8, 31), 1L),
                EducationForm.of("학위 이름", "세부 전공", EducationType.MASTER, "B", GradeType.FOUR_POINT_ZERO, "설명", LocalDate.of(2019, 3, 1), LocalDate.of(2020, 8, 31), 2L)
        );
        request.setEducations(educationForms);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(post("/api/v1/education")
                .contentType("application/json")
                .content(toJson(request))
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isCreated())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-교육")
                                .summary("학교 정보 업데이트 API")
                                .description("교육 정보를 업데이트합니다.")
                                .requestFields(
                                        fieldWithPath("educations[].name").type(JsonFieldType.STRING).description("교육 이름"),
                                        fieldWithPath("educations[].major").type(JsonFieldType.STRING).description("전공"),
                                        fieldWithPath("educations[].category").type(JsonFieldType.STRING).description("교육 유형 (BACHELOR, MASTERS 등)"),
                                        fieldWithPath("educations[].grade").type(JsonFieldType.STRING).optional().description("성적"),
                                        fieldWithPath("educations[].gradeType").type(JsonFieldType.STRING).description("성적 유형 (FOUR_POINT_FIVE, FOUR_POINT_ZERO 등)"),
                                        fieldWithPath("educations[].description").type(JsonFieldType.STRING).optional().description("설명"),
                                        fieldWithPath("educations[].startAt").type(JsonFieldType.STRING).description("교육 시작일"),
                                        fieldWithPath("educations[].endAt").type(JsonFieldType.STRING).optional().description("교육 종료일"),
                                        fieldWithPath("educations[].educationId").type(JsonFieldType.NUMBER).optional().description("교육 PK (새로 추가 시 null)")
                                ).responseFields(empty())
                                .build()
                )));

        verify(educationService).saveAllEducations(memberId, request.getEducations());
    }

//    @Test
//    @DisplayName("교육_컬럼_추가_테스트")
//    @CustomMockUser
//    void 교육_컬럼_추가_테스트() throws Exception {
//        // * GIVEN: 이런게 주어졌을 때
//        Long memberId = 1L;
//        Long educationalId = 1L;
//
//        EducationalForm mockResponse = EducationalForm.of(null, null, null, null, null, null, null, null, educationalId);
//
//        when(educationalService.createEmptyEducational(memberId)).thenReturn(mockResponse);
//
//        // * WHEN: 이걸 실행하면
//        ResultActions actions = mockMvc.perform(post("/api/v1/educational/add"));
//
//        // * THEN: 이런 결과가 나와야 한다
//        actions.andExpect(status().isCreated())
//                .andDo(restDocs.document(resource(
//                        ResourceSnippetParameters.builder()
//                                .tag("이력서-교육")
//                                .summary("학교 정보 컬럼 추가 API")
//                                .description("교육 정보를 레코드를 추가합니다.")
//                                .responseFields(response(
//                                        fieldWithPath("data.name").type(JsonFieldType.NULL).description("null"),
//                                        fieldWithPath("data.organization").type(JsonFieldType.NULL).description("null"),
//                                        fieldWithPath("data.credential").type(JsonFieldType.NULL).optional().description("null"),
//                                        fieldWithPath("data.acquisitionAt").type(JsonFieldType.NULL).description("null"),
//                                        fieldWithPath("data.grade").type(JsonFieldType.NULL).optional().description("null"),
//                                        fieldWithPath("data.certificationType").type(JsonFieldType.STRING).description("자격증 유형 (QUALIFICATION, LANGUAGE)"),
//                                        fieldWithPath("data.certificationId").type(JsonFieldType.NUMBER).optional().description("자격증 ID")))
//                                .build()
//                )));
//
//        verify(educationalService).createEmptyEducational(memberId);
//    }

    @Test
    @DisplayName("교육_정보_조회_테스트")
    @CustomMockUser
    void 교육_정보_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        EducationForm educationForm = EducationForm.of(
                "바밤대학교",
                "바밤과",
                EducationType.BACHELOR,
                "4.3",
                GradeType.FOUR_POINT_FIVE,
                "학부재밌었다.",
                LocalDate.of(2018, 3, 2),
                LocalDate.of(2024, 2, 15),
                1L
        );

        EducationResponse mockResponse = EducationResponse.of(List.of(educationForm));
        when(educationService.getAllEducations(memberId))
                .thenReturn(mockResponse);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(
                get("/api/v1/education")
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-교육")
                                .summary("학교 정보 조회 API")
                                .description("로그인 한 사용자가 작성한 학교 정보를 조회합니다.")
                                .responseFields(response(
                                        fieldWithPath("data.educations[].name").type(JsonFieldType.STRING).description("학교 이름"),
                                        fieldWithPath("data.educations[].major").type(JsonFieldType.STRING).description("전공"),
                                        fieldWithPath("data.educations[].category").type(JsonFieldType.STRING).description("학교 타입(ASSOCIATE_DEGREE, BACHELOR, MASTER, DOCTOR)"),
                                        fieldWithPath("data.educations[].grade").type(JsonFieldType.STRING).description("학점"),
                                        fieldWithPath("data.educations[].gradeType").type(JsonFieldType.STRING).description("최대 학점"),
                                        fieldWithPath("data.educations[].description").type(JsonFieldType.STRING).description("활동 내용 설명"),
                                        fieldWithPath("data.educations[].startAt").type(JsonFieldType.STRING).description("입학 날짜"),
                                        fieldWithPath("data.educations[].endAt").type(JsonFieldType.STRING).description("졸업 날짜"),
                                        fieldWithPath("data.educations[].educationId").type(JsonFieldType.NUMBER).description("학력 PK")
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

        when(educationService.getEducationTypes()).thenReturn(mockResponse);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(get("/api/v1/education/type"));

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

    @Test
    @DisplayName("교육_정보_삭제_테스트")
    void 교육_정보_삭제_테스트() throws Exception {
        // * GIVEN: 교육 타입 목록이 주어졌을 때
        Long educationId = 1L;
        doNothing().when(educationService).deleteById(educationId);

        // * WHEN: 이걸 실행하면
        ResultActions actions = mockMvc.perform(
                delete("/api/v1/education/{educationId}", educationId)
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isNoContent())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("이력서-교육")
                                .summary("교육 삭제 API")
                                .description("교육 정보를 삭제합니다.")
                                .build()
                )));
        verify(educationService).deleteById(educationId);
    }
}
