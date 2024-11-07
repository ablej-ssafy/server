package me.noteme.headhunting.domain.recruitment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.recruitment.entity.JobCategory;
import me.noteme.headhunting.domain.recruitment.entity.RecruitmentCategory;

@Data
@AllArgsConstructor(staticName = "of")
public class CategoryResponse {
    private Long id;
    private String name;

    public static CategoryResponse fromEntity(JobCategory category) {
        return CategoryResponse.of(
                category.getId(),
                category.getName()
        );
    }

    public static CategoryResponse fromEntity(RecruitmentCategory category) {
        return CategoryResponse.of(
                category.getCategory().getId(),
                category.getCategory().getName()
        );
    }
}
