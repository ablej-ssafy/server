package me.noteme.headhunting.domain.member.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobCategoryRequest {
    @NotNull(message = "직무 번호를 입력해주세요.")
    private Long id;
}
