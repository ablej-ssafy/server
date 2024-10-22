package me.noteme.headhunting.domain.recruitment.feign.response;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class PersonalKeywordsResponse {
    private String message;

    public List<String> getMessageAsList() {
        return Arrays.asList(message.split(","));
    }
}
