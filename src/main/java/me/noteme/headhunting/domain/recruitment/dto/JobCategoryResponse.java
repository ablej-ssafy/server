package me.noteme.headhunting.domain.recruitment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.recruitment.entity.JobCategory;

@Data
@AllArgsConstructor(staticName = "of")
public class JobCategoryResponse {
    private Long id;
    private String title;

    public static JobCategoryResponse fromEntity(JobCategory jobCategory) {
        return JobCategoryResponse.of(jobCategory.getId(), jobCategory.getName());
    }
}
