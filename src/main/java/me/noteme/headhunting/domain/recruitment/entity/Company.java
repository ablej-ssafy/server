package me.noteme.headhunting.domain.recruitment.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String name;

    @Column(name = "original_image")
    private String originalImage;

    @Column(name = "thumbnail_image")
    private String thumbnailImage;

    private String address;

    @Column(name = "road_address")
    private String roadAddress;

    private String location;

    private String strict;

    @Builder.Default
    private double latitude = 0;

    @Builder.Default
    private double longitude = 0;
}
