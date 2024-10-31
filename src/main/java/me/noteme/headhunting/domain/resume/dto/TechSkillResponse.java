package me.noteme.headhunting.domain.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class TechSkillResponse {
    Long skillId;
    String skillName;
    String skillIcon;
}
