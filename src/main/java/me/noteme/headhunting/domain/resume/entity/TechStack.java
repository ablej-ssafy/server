package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tech_stack")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TechStack {
    @Id
    @Column(name = "tech_stack_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "notion_url")
    private String notionUrl;

    @OneToMany(mappedBy = "techStack", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<StackSkill> stackSkills = new HashSet<>();

    public void update(String githubUrl, String notionUrl) {
        this.githubUrl = githubUrl;
        this.notionUrl = notionUrl;
    }
}
