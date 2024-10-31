package me.noteme.headhunting.domain.recruitment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recruitment_image")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @Column(name = "image_url", length = 500)
    private String imageUrl;
}
