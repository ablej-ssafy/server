package me.noteme.headhunting.domain.recruitment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class SearchResponse {
    private List<KeywordResponse> ranks;
    private List<KeywordResponse> recentKeywords;
}
