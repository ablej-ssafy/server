package me.noteme.headhunting.domain.recruitment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.recruitment.entity.Company;
import me.noteme.headhunting.domain.recruitment.entity.CompanyImage;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor(staticName = "of")
public class CompanyWithRecruitmentResponse {
    private Long companyId;
    private String name;
    private String thumbnail;
    private String description;
    private String industryName;
    private String link;
    private int foundedYear;
    private int age;
    private String address;
    private String roadAddress;
    private double latitude;
    private double longitude;
    private String location;
    private String strict;
    private List<String> images;
    private List<CompanyRecruitmentResponse> recruitments;

    public static CompanyWithRecruitmentResponse fromEntity(Company company) {
        return CompanyWithRecruitmentResponse.of(
                company.getId(),
                company.getName(),
                company.getThumbnailImage(),
                company.getDescription(),
                company.getIndustryName(),
                company.getLink(),
                company.getFoundedYear(),
                company.getAge(),
                company.getAddress(),
                company.getRoadAddress(),
                company.getLatitude(),
                company.getLongitude(),
                company.getLocation(),
                company.getStrict(),
                company.getCompanyImages().stream().map(CompanyImage::getImageUrl).toList(),
                company.getRecruitments().stream()
                        .map(CompanyRecruitmentResponse::fromEntity)
                        .toList()
        );
    }

    public static CompanyWithRecruitmentResponse fromEntity(Company company, Set<Long> scrapped) {
        return CompanyWithRecruitmentResponse.of(
                company.getId(),
                company.getName(),
                company.getThumbnailImage(),
                company.getDescription(),
                company.getIndustryName(),
                company.getLink(),
                company.getFoundedYear(),
                company.getAge(),
                company.getAddress(),
                company.getRoadAddress(),
                company.getLatitude(),
                company.getLongitude(),
                company.getLocation(),
                company.getStrict(),
                company.getCompanyImages().stream().map(CompanyImage::getImageUrl).toList(),
                company.getRecruitments().stream()
                        .map(recruitment -> CompanyRecruitmentResponse.fromEntity(recruitment, scrapped.contains(recruitment.getId())))
                        .toList()
        );
    }
}
