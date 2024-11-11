package me.noteme.headhunting.domain.recruitment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "company")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    @Column(name = "wanted_id")
    private Long wantedId;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Recruitment> recruitments = new ArrayList<>();

    private String name;

    private String link;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "industry_name")
    private String industryName;

    @Column(name = "original_image")
    private String originalImage;

    @Column(name = "thumbnail_image")
    private String thumbnailImage;

    private String address;

    @Column(name = "road_address")
    private String roadAddress;

    private String location;

    private String strict;

    @Column(name = "age")
    private int age;

    @Column(name = "founded_year")
    private int foundedYear;

    @Builder.Default
    private double latitude = 0;

    @Builder.Default
    private double longitude = 0;
}
