package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reference_url")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ReferenceUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reference_url_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teck_stack_id")
    private TechStack techStack;

    private String url;
}
