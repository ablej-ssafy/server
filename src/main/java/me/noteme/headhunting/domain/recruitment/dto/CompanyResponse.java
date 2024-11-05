package me.noteme.headhunting.domain.recruitment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.recruitment.entity.Company;

@Data
@AllArgsConstructor(staticName = "of")
public class CompanyResponse {
    private Long companyId;
    private String name;
    private String thumbnail;
    private String address;
    private String roadAddress;
    private double latitude;
    private double longitude;
    private String location;
    private String strict;

    public static CompanyResponse fromEntity(Company company) {
        return CompanyResponse.of(
                company.getId(),
                company.getName(),
                company.getThumbnailImage(),
                company.getAddress(),
                company.getRoadAddress(),
                company.getLatitude(),
                company.getLongitude(),
                company.getLocation(),
                company.getStrict()
        );
    }
}
