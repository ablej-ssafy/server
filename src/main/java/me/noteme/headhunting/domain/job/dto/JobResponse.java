package me.noteme.headhunting.domain.job.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class JobResponse {
    private Long id;
    private String title;
}
