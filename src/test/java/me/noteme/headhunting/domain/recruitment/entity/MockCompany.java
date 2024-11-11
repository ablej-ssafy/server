package me.noteme.headhunting.domain.recruitment.entity;

import java.util.Set;

import static org.mockito.Mockito.*;

public class MockCompany {
    public static Company create(Long id) {
        return create(id, "기업 명");
    }

    public static Company create(Long id, String name) {
        Company company = mock(Company.class);
        CompanyImage image1 = createImage(1L, "이미지 URL");
        CompanyImage image2 = createImage(2L, "이미지 URL");
        lenient().when(company.getId()).thenReturn(id);
        lenient().when(company.getName()).thenReturn(name);
        lenient().when(company.getThumbnailImage()).thenReturn("기업 소개용 썸네일 이미지");
        lenient().when(company.getLongitude()).thenReturn(37.1234);
        lenient().when(company.getLatitude()).thenReturn(127.1234);
        lenient().when(company.getAddress()).thenReturn("기업 주소");
        lenient().when(company.getDescription()).thenReturn("기업 소개");
        lenient().when(company.getLink()).thenReturn("기업 링크");
        lenient().when(company.getIndustryName()).thenReturn("기업 업종");
        lenient().when(company.getFoundedYear()).thenReturn(2024);
        lenient().when(company.getAge()).thenReturn(5);
        lenient().when(company.getRoadAddress()).thenReturn("기업 도로명 주소");
        lenient().when(company.getLocation()).thenReturn("기업 지역 (서울)");
        lenient().when(company.getStrict()).thenReturn("기업 구역 (서초구)");
        lenient().when(company.getCompanyImages()).thenReturn(Set.of(image1, image2));
        return company;
    }

    public static CompanyImage createImage(Long id, String url) {
        CompanyImage companyImage = mock(CompanyImage.class);
        lenient().when(companyImage.getId()).thenReturn(id);
        lenient().when(companyImage.getImageUrl()).thenReturn(url);
        return companyImage;
    }
}
