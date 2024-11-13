package me.noteme.headhunting.domain.recruitment.repository;

import me.noteme.headhunting.domain.recruitment.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface CompanyRepository extends JpaRepository<Company, Long>, CompanyQueryRepository {
    @Query("SELECT c.name FROM Company c")
    List<String> findCompanyNames();
}
