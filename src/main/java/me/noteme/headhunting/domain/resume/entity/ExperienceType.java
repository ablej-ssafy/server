package me.noteme.headhunting.domain.resume.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExperienceType {
    COMPANY("회사"),
    PROJECT("프로젝트"),
    ACTIVITY("대내외활동");

    private final String value;
}
