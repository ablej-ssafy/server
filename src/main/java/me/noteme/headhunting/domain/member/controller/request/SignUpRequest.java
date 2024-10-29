package me.noteme.headhunting.domain.member.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.List;

@Data
public class SignUpRequest {
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    @NotEmpty(message = "이름은 필수 입력 값입니다.")
    private String name;

    @Range(min = 0, max = 30, message = "경력은 0년 이상 30년 이하로 입력해주세요.")
    private int experience;

    private List<Long> jobIds = new ArrayList<>();
}
