package me.noteme.headhunting.domain.member.repository;

import me.noteme.headhunting.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query(""" 
            SELECT m
            FROM Member m
            WHERE m.username = :username
    """)
    Optional<Member> findByUsername(@Param("username") String username);

    @Query("""
            SELECT m.nickname
            FROM Member m
            WHERE m.username = :username
            AND m.emailVerified = FALSE
    """)
    Optional<String> findNicknameByUsername(String username);

    @Modifying
    @Query("""
            UPDATE Member m
            SET m.emailVerified = true
            WHERE m.username = :username
    """)
    void verify(@Param("username") String email);

    @Query("""
            SELECT m FROM Member m
            LEFT JOIN FETCH m.interestJobs ij
            LEFT JOIN FETCH ij.job
            WHERE m.id = :memberId
    """)
    Optional<Member> findFetchById(@Param("memberId") long memberId);
}
