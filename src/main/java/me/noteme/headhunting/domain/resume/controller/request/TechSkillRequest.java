package me.noteme.headhunting.domain.resume.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TechSkillRequest {
    @NotEmpty(message = "이름은 필수값입니다.")
    String name;

    @NotEmpty(message = "이미지는 필수값입니다.")
    String iconUrl;
}
