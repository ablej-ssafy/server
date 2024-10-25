package me.noteme.headhunting.domain.resume.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EducationalType {
    ASSOCIATE_DEGREE("전문대"),
    BACHELOR("대학"),
    MASTER("석사"),
    DOCTOR("박사");

    private final String name;
}
