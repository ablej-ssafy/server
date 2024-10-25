package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_stack_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

}
