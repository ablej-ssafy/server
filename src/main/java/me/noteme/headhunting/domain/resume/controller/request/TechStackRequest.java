package me.noteme.headhunting.domain.resume.controller.request;

import lombok.Data;

import java.util.List;

@Data
public class TechStackRequest {
    List<String> referenceUrls;
    List<TechStackRequest> techSkills;
}
