package me.noteme.headhunting.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.recruitment.dto.CategoryResponse;

@Data
@AllArgsConstructor(staticName = "of")
public class LoginMemberResponse {
    private Long id;
    private String name;
    private String email;
    private int career;
    private CategoryResponse jobCategory;
}
