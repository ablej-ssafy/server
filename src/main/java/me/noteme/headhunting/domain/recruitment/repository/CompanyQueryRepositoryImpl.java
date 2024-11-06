package me.noteme.headhunting.domain.recruitment.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.domain.recruitment.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static me.noteme.headhunting.domain.member.entity.QScrap.scrap;
import static me.noteme.headhunting.domain.recruitment.entity.QCompany.company;
import static me.noteme.headhunting.domain.recruitment.entity.QRecruitment.recruitment;

@Repository
@RequiredArgsConstructor
public class CompanyQueryRepositoryImpl implements CompanyQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Company> searchCompanies(String type, String query, Pageable pageable) {
        Long count = countSearchCompany(type, query);
        List<Company> companies = getContentsCompany(type, query, pageable);
        return new PageImpl<>(companies, pageable, count);
    }

    @Override
    public Optional<Company> findCompanyById(Long companyId) {
        return Optional.ofNullable(
                queryFactory.select(company)
                    .from(company)
                    .leftJoin(company.recruitments, recruitment).fetchJoin()
                    .where(company.id.eq(companyId))
                    .fetchOne()
        );
    }

    private List<Company> getContentsCompany(String type, String query, Pageable pageable) {
        return queryFactory.selectFrom(company)
                .where(searchByType(type, query))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private Long countSearchCompany(String type, String query) {
        return queryFactory.select(company.id.count())
                .from(company)
                .where(searchByType(type, query))
                .fetchOne();
    }

    private BooleanExpression searchByType(String type, String query) {
        if (!StringUtils.hasText(query)) {
            return null;
        }

        return switch (type) {
            case "name" -> company.name.contains(query);
            case "location" -> company.location.contains(query);
            case "strict" -> company.strict.contains(query);
            case "address" -> company.address.contains(query).or(
                    company.roadAddress.contains(query)
            );
            default -> null;
        };
    }
}
