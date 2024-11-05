package me.noteme.headhunting.domain.recruitment.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchResponse {
    private List<KeywordResponse> ranks;
    private List<KeywordResponse> recentKeywords;
}
