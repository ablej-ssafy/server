package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tech_skill")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TechSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_skill_id")
    private Long id;

    @Column(name = "icon_url")
    private String iconUrl;

    private String name;

    @OneToMany(mappedBy = "techSkill", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StackSkill> stackSkills;
}
