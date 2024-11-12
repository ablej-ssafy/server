package me.noteme.headhunting.domain.member.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.core.annotation.CustomMockUser;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.member.controller.request.EmailRequest;
import me.noteme.headhunting.domain.member.controller.request.RefreshRequest;
import me.noteme.headhunting.domain.member.controller.request.SignInRequest;
import me.noteme.headhunting.domain.member.controller.request.SignUpRequest;
import me.noteme.headhunting.domain.member.dto.JwtToken;
import me.noteme.headhunting.domain.member.service.AuthService;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("인증 컨트롤러 테스트")
@WebMvcTest(value = AuthController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTFilter.class),
        }
)
@PropertySource("classpath:application.yml")
class AuthControllerTest extends RestDocsSupport {
    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("회원가입_정상_테스트")
    void 회원가입_정상_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        long jobId = 1L;
        SignUpRequest request = new SignUpRequest();
        request.setEmail("testuser@gmail.com");
        request.setPassword("testpassword");
        request.setName("테스트 유저");
        request.setCareerYear(1);
        request.setJobId(jobId);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(post("/api/v1/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isCreated())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("인증")
                                .summary("회원가입 API")
                                .description("입력받은 회원 정보로 회원가입을 합니다.")
                                .requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("회원 비밀번호"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                        fieldWithPath("careerYear").type(JsonFieldType.NUMBER).description("경력"),
                                        fieldWithPath("jobId").type(JsonFieldType.NUMBER).description("관심 직무 ID")
                                ).responseFields()
                                .build()
                )));

        verify(authService).signUp(request.getEmail(), request.getPassword(), request.getName(), 1, jobId);
    }

    @Test
    @DisplayName("회원가입_입력_값_에러_테스트")
    void 회원가입_입력_값_에러_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        SignUpRequest request = new SignUpRequest();
        request.setEmail("testuser@naver.com");
        request.setPassword("q4!!");
        request.setName("테스트 유저");
        request.setCareerYear(20);
        request.setJobId(1L);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(post("/api/v1/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isBadRequest())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("인증")
                                .summary("회원가입 API")
                                .description("입력받은 회원 정보로 회원가입을 합니다.")
                                .requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("회원 비밀번호"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                        fieldWithPath("careerYear").type(JsonFieldType.NUMBER).description("경력"),
                                        fieldWithPath("jobId").type(JsonFieldType.NUMBER).description("관심 직무 ID")
                                ).responseFields(errors(
                                        fieldWithPath("errors[].field").type(JsonFieldType.STRING).description("에러 필드"),
                                        fieldWithPath("errors[].code").type(JsonFieldType.STRING).description("애러 코드"),
                                        fieldWithPath("errors[].message").type(JsonFieldType.STRING).description("에러 메시지"),
                                        fieldWithPath("errors[].objectName").type(JsonFieldType.STRING).description("에러 발생한 객체명")
                                ))
                                .build()
                )));
    }

    @Test
    @DisplayName("로그인_정상_테스트")
    void 로그인_정상_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        SignInRequest request = new SignInRequest();
        request.setEmail("testuser@gmail.com");
        request.setPassword("testpassword");

        JwtToken token = new JwtToken("accessToken", "refreshToken");
        when(authService.signIn(request.getEmail(), request.getPassword())).thenReturn(token);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(post("/api/v1/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("인증")
                                .summary("로그인 API")
                                .description("입력받은 회원 정보로 로그인을 합니다.")
                                .requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("회원 비밀번호")
                                ).responseFields(response(
                                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                        fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                                ))
                                .build()
                )));
    }

    @Test
    @DisplayName("로그아웃_정상_테스트")
    @CustomMockUser
    void 로그아웃_정상_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        String refreshToken = "refreshToken";

        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken(refreshToken);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(post("/api/v1/auth/sign-out")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer accessToken")
                .content(toJson(request))
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("인증")
                                .summary("로그아웃 API")
                                .description("로그아웃을 합니다.")
                                .responseHeaders(
                                        headerWithName("Set-Cookie").type(SimpleType.STRING).description("쿠키에서 refresh Token 삭제 요청")
                                )
                                .requestFields(
                                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                                ).responseFields()
                                .build()
                )));

        verify(authService).signOut(memberId, refreshToken);
    }

    @Test
    @DisplayName("토큰_재발급_정상_테스트")
    @CustomMockUser
    void 토큰_재발급_정상_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long memberId = 1L;
        String refreshToken = "refreshToken";

        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken(refreshToken);

        JwtToken response = new JwtToken("newAccessToken", "newRefreshToken");
        when(authService.refresh(memberId, refreshToken)).thenReturn(response);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer accessToken")
                .content(toJson(request))
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("인증")
                                .summary("토큰 재발급 API")
                                .description("리프레시 토큰을 이용하여 새로운 액세스 토큰을 발급합니다.")
                                .requestFields(
                                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                                ).responseHeaders(
                                        headerWithName("Set-Cookie").type(SimpleType.STRING).description("쿠키에 새로운 refresh Token 저장 요청")
                                ).responseFields(response(
                                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                        fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                                ))
                                .build()
                )));
    }

    @Test
    @DisplayName("이메일_인증_요청_정상_테스트")
    void 이메일_인증_요청_정상_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        String key = "confirmKey";

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(get("/api/v1/auth/confirm/email/{key}", key)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(key))
        );

        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("인증")
                                .summary("이메일 인증 API")
                                .description("이메일 인증을 합니다.")
                                .pathParameters(
                                        parameterWithName("key").type(SimpleType.STRING).description("이메일 인증 키")
                                ).responseFields()
                                .build()
                )));
    }

    @Test
    @DisplayName("이메일_재전송_요청_정상_테스트")
    void 이메일_재전송_요청_정상_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        String email = "email@gmail.com";

        EmailRequest request = new EmailRequest();
        request.setEmail(email);

        // * WHEN: 이걸 실행하면
        ResultActions actions = this.mockMvc.perform(post("/api/v1/auth/resend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        );


        // * THEN: 이런 결과가 나와야 한다
        actions.andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("인증")
                                .summary("이메일 재전송 API")
                                .description("이메일 재전송을 합니다.")
                                .requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일")
                                ).responseFields()
                                .build()
                )));

    }
}