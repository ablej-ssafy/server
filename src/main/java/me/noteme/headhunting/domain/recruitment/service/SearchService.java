package me.noteme.headhunting.domain.recruitment.service;

import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final CompanyRepository companyRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final ScrapRepository scrapRepository;
    private final SearchCacheRepository searchCacheRepository;

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
        AtomicInteger rank = new AtomicInteger(1);
        SearchResponse response = new SearchResponse();

        response.setRanks(searchCacheRepository.getTopKeywords().stream().map(
                keyword -> KeywordResponse.of(rank.getAndIncrement(), keyword)
        ).toList());

        if (Objects.isNull(memberId)) {
            response.setRecentKeywords(List.of());
            return response;
        }

        AtomicInteger recent = new AtomicInteger(1);
        response.setRecentKeywords(searchCacheRepository.getKeywords(memberId).stream().map(
                keyword -> KeywordResponse.of(recent.getAndIncrement(), keyword)
        ).toList());

        return response;
    }
}
