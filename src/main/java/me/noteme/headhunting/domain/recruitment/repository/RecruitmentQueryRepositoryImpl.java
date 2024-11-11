package me.noteme.headhunting.domain.recruitment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.domain.recruitment.entity.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static me.noteme.headhunting.domain.recruitment.entity.QRecruitment.recruitment;

@Repository
@RequiredArgsConstructor
public class RecruitmentQueryRepositoryImpl implements RecruitmentQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Recruitment> findRecruitmentsByCategoryId(Long categoryId, Pageable pageable) {
        Long count = countRecruitmentsByCategoryId(categoryId);
        List<Recruitment> contents = getContentsByCategoryId(categoryId, pageable);
        return new PageImpl<>(contents, pageable, count);
    }

    @Override
    public Page<Recruitment> findRecruitments(Pageable pageable) {
        Long count = countRecruitment();
        List<Long> ids = getRecruitmentIds(pageable);
        List<Recruitment> contents = getRecruitmentContents(ids);
        return new PageImpl<>(contents, pageable, count);
    }

    @Override
    public Page<Recruitment> searchRecruitments(String query, Pageable pageable) {
        Long count = countRecruitmentsByQuery(query);
        List<Recruitment> contents = getContentsByQuery(query, pageable);
        return new PageImpl<>(contents, pageable, count);
    }

    private List<Long> getRecruitmentIds(Pageable pageable) {
        return queryFactory.select(recruitment.id)
                .from(recruitment)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private List<Recruitment> getContentsByCategoryId(Long categoryId, Pageable pageable) {
        return queryFactory.selectFrom(recruitment)
                .leftJoin(recruitment.category).fetchJoin()
                .leftJoin(recruitment.company).fetchJoin()
                .where(recruitment.category.id.eq(categoryId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private List<Recruitment> getRecruitmentContents(List<Long> ids) {
        return queryFactory.selectFrom(recruitment)
                .leftJoin(recruitment.category).fetchJoin()
                .leftJoin(recruitment.childCategories).fetchJoin()
                .leftJoin(recruitment.company).fetchJoin()
                .leftJoin(recruitment.images).fetchJoin()
                .where(recruitment.id.in(ids))
                .fetch();
    }

    private List<Recruitment> getContentsByQuery(String query, Pageable pageable) {
        return queryFactory.selectFrom(recruitment)
                .leftJoin(recruitment.category).fetchJoin()
                .leftJoin(recruitment.company).fetchJoin()
                .where(recruitment.name.contains(query).or(
                        recruitment.company.name.contains(query)
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private Long countRecruitment() {
        return queryFactory.select(recruitment.count())
                .from(recruitment)
                .fetchOne();
    }

    private Long countRecruitmentsByCategoryId(Long categoryId) {
        return queryFactory.select(recruitment.count())
                .from(recruitment)
                .where(recruitment.category.id.eq(categoryId))
                .fetchOne();
    }

    private Long countRecruitmentsByQuery(String query) {
        return queryFactory.select(recruitment.count())
                .from(recruitment)
                .where(recruitment.name.contains(query))
                .fetchOne();
    }
}
