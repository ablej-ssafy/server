package me.noteme.headhunting.domain.recruitment.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.recruitment.dto.CompanyResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
import me.noteme.headhunting.domain.recruitment.dto.SearchResponse;
import me.noteme.headhunting.domain.recruitment.service.SearchService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public SuccessResponse<SearchResponse> search(@LoginUser Long userId) {
        return SuccessResponse.of(
                searchService.rankKeywords(userId)
        );
    }

    @GetMapping("/suggestions")
    public SuccessResponse<List<String>> getSuggestions(@RequestParam(name = "q") String keyword) {
        List<String> suggestions = searchService.getSuggestions(keyword);
        return SuccessResponse.of(suggestions);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSearchKeyword(
            @LoginUser Long userId,
            @RequestParam String keyword
    ) {
        searchService.removeKeyword(userId, keyword);
    }

    @GetMapping("/company")
    public SuccessResponse<PagedModel<CompanyResponse>> searchCompanies(
            @LoginUser Long memberId,
            @RequestParam(name = "type", required = false, defaultValue = "all") String type,
            @RequestParam(name = "q", required = false) String query,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return SuccessResponse.of(
                new PagedModel<>(searchService.searchCompanies(memberId, type, query, pageable))
        );
    }

    @GetMapping("/recruitment")
    public SuccessResponse<PagedModel<RecruitmentSummaryResponse>> searchRecruitments(
            @LoginUser Long memberId,
            @RequestParam(value = "q", required = false) String query,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return SuccessResponse.of(
                new PagedModel<>(searchService.searchRecruitments(memberId, query, pageable))
        );
    }
}
