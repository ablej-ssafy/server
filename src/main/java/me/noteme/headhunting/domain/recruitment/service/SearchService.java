package me.noteme.headhunting.domain.recruitment.service;

import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final CompanyRepository companyRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final SearchCacheRepository searchCacheRepository;

    public Page<CompanyResponse> searchCompanies(Long userId, String type, String query, Pageable pageable) {
        if (type.equals("name")) {
            searchCacheRepository.addKeyword(userId, query);
        }
        Page<Company> companies = companyRepository.searchCompanies(type, query, pageable);
        return companies.map(CompanyResponse::fromEntity);
    }

    public Page<RecruitmentSummaryResponse> searchRecruitments(Long userId, String query, Pageable pageable) {
        searchCacheRepository.addKeyword(userId, query);
        Page<Recruitment> recruitments = recruitmentRepository.searchRecruitments(query, pageable);
        return recruitments.map(RecruitmentSummaryResponse::fromEntity);
    }

    public SearchResponse rankKeywords(Long userId) {
        AtomicInteger rank = new AtomicInteger(1);
        SearchResponse response = new SearchResponse();

        response.setRanks(searchCacheRepository.getTopKeywords().stream().map(
                keyword -> KeywordResponse.of(rank.getAndIncrement(), keyword)
        ).toList());

        if (userId == null) {
            response.setRecentKeywords(List.of());
            return response;
        }

        AtomicInteger recent = new AtomicInteger(1);
        response.setRecentKeywords(searchCacheRepository.getKeywords(userId).stream().map(
                keyword -> KeywordResponse.of(recent.getAndIncrement(), keyword)
        ).toList());

        return response;
    }
}
