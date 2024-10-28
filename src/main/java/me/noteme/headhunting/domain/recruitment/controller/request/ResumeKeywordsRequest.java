package me.noteme.headhunting.domain.recruitment.controller.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ResumeKeywordsRequest {
    @Min(value = 1, message = "최솟값은 1 이상입니다.")
    @JsonProperty("job_id")
    @JsonAlias({"jobId"})
    private int jobId;

    @Min(value = 1, message = "최솟값은 1 이상입니다.")
    @JsonProperty("job_sub_id")
    @JsonAlias({"jobSubId"})
    private int jobSubId;

    @NotEmpty(message = "유효하지 않은 입력값입니다.")
    private String resume;
}
