package me.noteme.headhunting.domain.resume.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.noteme.headhunting.domain.resume.entity.Certification;
import me.noteme.headhunting.domain.resume.entity.CertificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    @Query("SELECT c FROM Certification c WHERE c.resume.id = :resumeId ORDER BY c.id ASC")
    List<Certification> findAllByResumeId(@Param("resumeId") Long resumeId);

    @Query("SELECT c FROM Certification c WHERE c.resume.id = :resumeId and c.certificationType = :type ORDER BY c.id ASC")
    List<Certification> findAllCategoryByResumeId(@Param("resumeId") Long resumeId, @Param("type") CertificationType type);
}
