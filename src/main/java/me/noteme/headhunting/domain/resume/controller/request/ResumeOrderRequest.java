package me.noteme.headhunting.domain.resume.controller.request;

import lombok.Data;

@Data
public class ResumeOrderRequest {
    private int education;
    private int company;
    private int project;
    private int activity;
    private int qualification;
    private int language;
    private int tech;
}
