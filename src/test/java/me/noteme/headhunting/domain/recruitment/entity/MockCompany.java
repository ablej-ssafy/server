package me.noteme.headhunting.domain.recruitment.entity;

import static org.mockito.Mockito.*;

public class MockCompany {
    public static Company create(Long id) {
        return create(id, "기업 명");
    }

    public static Company create(Long id, String name) {
        Company company = mock(Company.class);
        lenient().when(company.getId()).thenReturn(id);
        lenient().when(company.getName()).thenReturn(name);
        lenient().when(company.getThumbnailImage()).thenReturn("기업 소개용 썸네일 이미지");
        lenient().when(company.getLongitude()).thenReturn(37.1234);
        lenient().when(company.getLatitude()).thenReturn(127.1234);
        lenient().when(company.getAddress()).thenReturn("기업 주소");
        lenient().when(company.getRoadAddress()).thenReturn("기업 도로명 주소");
        lenient().when(company.getLocation()).thenReturn("기업 지역 (서울)");
        lenient().when(company.getStrict()).thenReturn("기업 구역 (서초구)");
        return company;
    }
}
