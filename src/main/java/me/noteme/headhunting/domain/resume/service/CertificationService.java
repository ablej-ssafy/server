package me.noteme.headhunting.domain.resume.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.domain.resume.entity.Certification;
import me.noteme.headhunting.domain.resume.repository.CertificationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificationService {
    private final CertificationRepository certificationRepository;
    private final EntityManager em;

    @Transactional
    public void saveCertification() {

    }
}
