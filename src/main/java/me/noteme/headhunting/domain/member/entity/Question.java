package me.noteme.headhunting.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "question"
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String question;

    public void updateQuestion(String question) {
        this.question = question;
    }
}
