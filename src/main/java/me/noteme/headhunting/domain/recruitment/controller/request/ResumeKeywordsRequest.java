package me.noteme.headhunting.domain.recruitment.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ResumeKeywordsRequest {
    @Min(1)
    @JsonProperty("job_id")
    private int jobId;
    @Min(1)
    @JsonProperty("job_sub_id")
    private int jobSubId;
    @NotEmpty
    private String resume;
}
