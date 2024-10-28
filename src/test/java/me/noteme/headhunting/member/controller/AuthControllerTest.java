package me.noteme.headhunting.member.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import me.noteme.headhunting.core.support.RestDocsSupport;
import me.noteme.headhunting.domain.member.controller.AuthController;
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

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("인증 컨트롤러 테스트")
@WebMvcTest(value = AuthController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTFilter.class),
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
        SignUpRequest request = new SignUpRequest();
        request.setEmail("testuser@gmail.com");
        request.setPassword("testpassword");
        request.setName("테스트 유저");

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
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름")
                                ).responseFields()
                                .build()
                )));

        verify(authService).signUp(request.getEmail(), request.getPassword(), request.getName());
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
}