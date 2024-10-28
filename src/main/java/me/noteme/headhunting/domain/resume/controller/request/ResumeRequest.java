package me.noteme.headhunting.domain.resume.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResumeRequest {
    private String title;
    private String name;
    private String email;
    private LocalDate birth;
    private String phone;
    private String introduce;
    private String portfolioUrl;
}
