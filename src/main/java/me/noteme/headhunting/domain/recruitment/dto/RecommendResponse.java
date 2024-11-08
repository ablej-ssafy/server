package me.noteme.headhunting.domain.recruitment.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.noteme.headhunting.domain.recruitment.entity.Recruitment;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecommendResponse implements Comparable<RecommendResponse> {
    private Long id;
    private String title;
    private String companyName;
    private String thumbnail;
    private boolean isScrapped;
    private double similarity;

    public static RecommendResponse create(Recruitment recruitment, boolean isScrapped, double similarity) {
        return new RecommendResponse(
                recruitment.getId(),
                recruitment.getName(),
                recruitment.getCompany().getName(),
                recruitment.getThumbnail(),
                isScrapped,
                similarity
        );
    }

    public static RecommendResponse create(Recruitment recruitment, boolean isScrapped) {
        return RecommendResponse.create(recruitment, isScrapped, 0.0);
    }


    @Override
    public int compareTo(RecommendResponse recommendResponse) {
        return Double.compare(recommendResponse.similarity, this.similarity);
    }
}
