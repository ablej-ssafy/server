package me.noteme.headhunting.domain.recruitment.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ResumeKeywordsRequest {
    @JsonProperty("job_id")
    private int jobId;
    @JsonProperty("job_sub_id")
    private int jobSubId;
    private String resume;
}
