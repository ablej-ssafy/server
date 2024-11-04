package me.noteme.headhunting.domain.recruitment.repository;

import me.noteme.headhunting.domain.recruitment.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CompanyQueryRepository {
    Page<Company> searchCompanies(String type, String query, Pageable pageable);

    Optional<Company> findCompanyById(Long companyId);
}
