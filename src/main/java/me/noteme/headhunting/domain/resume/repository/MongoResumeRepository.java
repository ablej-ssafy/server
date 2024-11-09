package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.mongo.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MongoResumeRepository extends MongoRepository<MongoResume, String> {
    Optional<MongoResume> findByMemberId(@Param("memberId") Long memberId);

    @Query("{'memberId': ?0}")
    @Update("{'$set': {'basic': ?1}}")
    void updateBasic(Long memberId, MongoResumeBasic resumeBasic);

    @Query("{'memberId': ?0}")
    @Update("{'$set': {'companies': ?1}}")
    void updateCompanies(Long memberId, List<MongoExperience> companies);

    @Query("{'memberId': ?0}")
    @Update("{'$set': {'activities': ?1}}")
    void updateActivities(Long memberId, List<MongoExperience> activities);

    @Query("{'memberId': ?0}")
    @Update("{'$set': {'projects': ?1}}")
    void updateProjects(Long memberId, List<MongoExperience> projects);

    @Query("{'memberId': ?0}")
    @Update("{'$set': {'educations': ?1}}")
    void updateEducations(Long memberId, List<MongoEducation> educations);

    @Query("{'memberId': ?0}")
    @Update("{'$set': {'qualifications': ?1}}")
    void updateQualifications(Long memberId, List<MongoCertification> qualifications);

    @Query("{'memberId': ?0}")
    @Update("{'$set': {'languages': ?1}}")
    void updateLanguages(Long memberId, List<MongoCertification> languages);

}
