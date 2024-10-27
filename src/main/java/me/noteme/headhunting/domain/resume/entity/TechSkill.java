package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_skill_id")
    private Long id;

    @Column(name = "icon_url")
    private String iconUrl;

    private String name;

    @OneToMany(mappedBy = "techSkill", fetch = FetchType.LAZY)
    private List<TechStackSkill> techStackSkills;

}
