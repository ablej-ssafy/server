package me.noteme.headhunting.domain.resume.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GradeType {
    FOUR_POINT_ZERO("4.0"),
    FOUR_POINT_THREE("4.3"),
    FOUR_POINT_FIVE("4.5");

    private final String value;
}
