package me.noteme.headhunting.domain.recruitment.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

public class MockRecruitment {
    public static Recruitment create(Long id) {
        return create(id, "채용 공고명", 1L);
    }

    public static Recruitment create(Long id, String name) {
        return create(id, name, 1L);
    }

        public static Recruitment create(Long id, Long categoryId) {
        return create(id, "채용 공고명", categoryId);
    }

    public static Recruitment create(Long id, String name, Long categoryId) {
        Company mockCompany = MockCompany.create(id);
        JobCategory mockCategory = category(categoryId);
        List<RecruitmentImage> images = Stream.of(1L, 2L, 3L).map(MockRecruitment::image).toList();
        List<RecruitmentCategory> categories = Stream.of(1L, 2L, 3L).map(MockRecruitment::recruitmentCategory).toList();

        Recruitment recruitment = mock(Recruitment.class);
        lenient().when(recruitment.getId()).thenReturn(id);
        lenient().when(recruitment.getCategory()).thenReturn(mockCategory);
        lenient().when(recruitment.getChildCategories()).thenReturn(Set.copyOf(categories));
        lenient().when(recruitment.getImages()).thenReturn(Set.copyOf(images));
        lenient().when(recruitment.getName()).thenReturn(name);
        lenient().when(recruitment.getCompany()).thenReturn(mockCompany);
        lenient().when(recruitment.getThumbnail()).thenReturn("채용 공고 썸네일 이미지");
        lenient().when(recruitment.getIntro()).thenReturn("기업 소개");
        lenient().when(recruitment.getTask()).thenReturn("업무 내용");
        lenient().when(recruitment.getRequirement()).thenReturn("자격 요건");
        lenient().when(recruitment.getPreference()).thenReturn("기업 우대 조건");
        lenient().when(recruitment.getBenefit()).thenReturn("기업 복지/혜택");
        lenient().when(recruitment.getHireRound()).thenReturn("채용 프로세스");
        lenient().when(recruitment.getWantedId()).thenReturn(id);
        lenient().when(recruitment.getAnnualTo()).thenReturn(0);
        lenient().when(recruitment.getAnnualFrom()).thenReturn(10);
        lenient().when(recruitment.getDueTime()).thenReturn(id % 2 == 0 ? LocalDate.of(2024, 11, 4) : null);
        return recruitment;
    }

    public static JobCategory category(Long id) {
        JobCategory category = mock(JobCategory.class);
        lenient().when(category.getId()).thenReturn(id);
        lenient().when(category.getName()).thenReturn("직업 카테고리");
        return category;
    }

    public static RecruitmentImage image(Long id) {
        RecruitmentImage image = mock(RecruitmentImage.class);
        lenient().when(image.getId()).thenReturn(id);
        lenient().when(image.getImageUrl()).thenReturn("채용 공고 이미지 URL");
        return image;
    }

    public static RecruitmentCategory recruitmentCategory(Long id) {
        JobCategory category = category(id);
        RecruitmentCategory recruitmentCategory = mock(RecruitmentCategory.class);
        lenient().when(recruitmentCategory.getId()).thenReturn(id);
        lenient().when(recruitmentCategory.getCategory()).thenReturn(category);
        return recruitmentCategory;
    }
}
