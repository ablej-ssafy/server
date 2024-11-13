package me.noteme.headhunting.domain.recruitment.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.domain.member.repository.ScrapRepository;
import me.noteme.headhunting.domain.recruitment.dto.CompanyResponse;
import me.noteme.headhunting.domain.recruitment.dto.KeywordResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
import me.noteme.headhunting.domain.recruitment.dto.SearchResponse;
import me.noteme.headhunting.domain.recruitment.entity.Company;
import me.noteme.headhunting.domain.recruitment.entity.Recruitment;
import me.noteme.headhunting.domain.recruitment.repository.CompanyRepository;
import me.noteme.headhunting.domain.recruitment.repository.RecruitmentRepository;
import me.noteme.headhunting.domain.recruitment.repository.SearchCacheRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final CompanyRepository companyRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final ScrapRepository scrapRepository;
    private final SearchCacheRepository searchCacheRepository;

    @PostConstruct
    @Transactional
    public void init() {
        searchCacheRepository.initSuggestions(recruitmentRepository.findRecruitmentNames());
        searchCacheRepository.initSuggestions(companyRepository.findCompanyNames());
    }

    public Page<CompanyResponse> searchCompanies(Long memberId, String type, String query, Pageable pageable) {
        if (type.equals("name")) {
            searchCacheRepository.addKeyword(memberId, query);
        }
        Page<Company> companies = companyRepository.searchCompanies(type, query, pageable);

        return companies.map(CompanyResponse::fromEntity);
    }

    public Page<RecruitmentSummaryResponse> searchRecruitments(Long memberId, String query, Pageable pageable) {
        searchCacheRepository.addKeyword(memberId, query);
        Page<Recruitment> recruitments = recruitmentRepository.searchRecruitments(query, pageable);
        Set<Long> scrapped = scrapRepository.isScrapped(memberId, recruitments.stream().map(Recruitment::getId).toList());

        return recruitments.map(
                recruitment -> RecruitmentSummaryResponse.fromEntity(recruitment, scrapped.contains(recruitment.getId()))
        );
    }

    public SearchResponse rankKeywords(Long memberId) {
        List<KeywordResponse> topKeywordResponses = getTopKeywordResponses();
        List<KeywordResponse> recentKeywords =
                Objects.isNull(memberId) ? List.of() : getRecentKeywords(memberId);

        return SearchResponse.of(topKeywordResponses, recentKeywords);
    }

    private List<KeywordResponse> getRecentKeywords(Long memberId) {
        List<String> keywords = searchCacheRepository.getKeywords(memberId);

        List<KeywordResponse> recentKeywords = new ArrayList<>();
        int recent = 1;
        for (String keyword : keywords) {
            recentKeywords.add(KeywordResponse.of(recent++, keyword));
        }
        return recentKeywords;
    }

    private List<KeywordResponse> getTopKeywordResponses() {
        Set<String> topKeywords = searchCacheRepository.getTopKeywords();

        List<KeywordResponse> keywordResponses = new ArrayList<>();
        int rank = 1;
        for (String keyword : topKeywords) {
            keywordResponses.add(KeywordResponse.of(rank++, keyword));
        }
        return keywordResponses;
    }

    public List<String> getSuggestions(String keyword) {
        return searchCacheRepository.getSuggestions(keyword);
    }

    @Transactional
    public void removeKeyword(Long userId, String keyword) {
        searchCacheRepository.removeKeyword(userId, keyword);
    }
}
