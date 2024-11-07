package me.noteme.headhunting.domain.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.noteme.headhunting.domain.resume.entity.ReferenceUrl;
import me.noteme.headhunting.domain.resume.entity.TechStack;

import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TechResponse {
    Long resumeId;
    Long techId;
    List<TechSkillResponse> techSkills;
    List<ReferenceUrlResponse> referenceUrls;

    public static TechResponse fromEntity(TechStack techStack) {
        // TODO: TechSkill, ReferenceUrl 조회 시 JOIN FETCH에서 발생되는 이슈(ex. 카타시안곱, MultipleBagFetchException) 개선점 생각하기
        List<TechSkillResponse> techSkillResponses = techStack.getStackSkills().stream()
                .sorted(Comparator.comparing(ss -> ss.getTechSkill().getId()))
                .map(ss -> TechSkillResponse.of(
                        ss.getTechSkill().getId(), ss.getTechSkill().getName(), ss.getTechSkill().getIconUrl()
                ))
                .toList();

        List<ReferenceUrlResponse> referenceUrlResponses = techStack.getReferenceUrls().stream()
                .sorted(Comparator.comparing(ReferenceUrl::getId))
                .map(url -> ReferenceUrlResponse.of(
                        url.getId(), url.getUrl()
                ))
                .toList();

        return TechResponse.of(
                techStack.getResume().getId(),
                techStack.getId(),
                techSkillResponses,
                referenceUrlResponses
        );
    }
}
