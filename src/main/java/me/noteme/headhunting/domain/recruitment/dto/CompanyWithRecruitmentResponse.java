package me.noteme.headhunting.domain.recruitment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.recruitment.entity.Company;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class CompanyWithRecruitmentResponse {
    private Long companyId;
    private String name;
    private String thumbnail;
    private String address;
    private String roadAddress;
    private double latitude;
    private double longitude;
    private String location;
    private String strict;
    private List<CompanyRecruitmentResponse> recruitments;

    public static CompanyWithRecruitmentResponse fromEntity(Company company) {
        return CompanyWithRecruitmentResponse.of(
                company.getId(),
                company.getName(),
                company.getThumbnailImage(),
                company.getAddress(),
                company.getRoadAddress(),
                company.getLatitude(),
                company.getLongitude(),
                company.getLocation(),
                company.getStrict(),
                company.getRecruitments().stream()
                        .map(CompanyRecruitmentResponse::fromEntity)
                        .toList()
        );
    }
}
