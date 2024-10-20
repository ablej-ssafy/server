package me.noteme.headhunting.member.repository;

import me.noteme.headhunting.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member m WHERE m.username = :username")
    Optional<Member> findByUsername(@Param("username") String username);
}
