package me.noteme.headhunting.domain.member.repository;

import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.member.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("""
            SELECT q
            FROM Question q
            WHERE q.member = :member
            ORDER BY q.id
    """)
    List<Question> findAllByMemberId(Member member);
}
