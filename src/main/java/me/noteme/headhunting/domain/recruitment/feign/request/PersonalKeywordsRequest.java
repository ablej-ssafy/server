package me.noteme.headhunting.domain.recruitment.feign.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PersonalKeywordsRequest {
    @JsonProperty("job_id")
    private int jobId;

    @JsonProperty("job_sub_id")
    private int jobSubId;

    private String resume;
}
