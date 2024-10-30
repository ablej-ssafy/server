package me.noteme.headhunting.domain.resume.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.domain.resume.controller.request.CertificationForm;
import me.noteme.headhunting.domain.resume.entity.Certification;
import me.noteme.headhunting.domain.resume.entity.CertificationType;
import me.noteme.headhunting.domain.resume.entity.Resume;
import me.noteme.headhunting.domain.resume.repository.CertificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificationService {
    private final CertificationRepository certificationRepository;
    private final EntityManager em;

    @Transactional
    public void saveCertification(Long resumeId, String name, String organization, String credential, LocalDate acquisitionAt, String grade, CertificationType certificationType, Long certificationId) {
        Resume resume = getResumeById(resumeId);

        Certification certification = generateCertification(certificationId, name, organization, credential, acquisitionAt, grade, certificationType, resume);

        certificationRepository.save(certification);
    }

    @Transactional
    public void saveAllCertifications(List<CertificationForm> certificationForms) {
        // TODO: 자격 정보 저장에 대한 최대 값 검증 로직
        List<Certification> certifications = certificationForms.stream()
                .map(form -> generateCertification(
                        form.getCertificationId(),
                        form.getName(),
                        form.getOrganization(),
                        form.getCredential(),
                        form.getAcquisitionAt(),
                        form.getGrade(),
                        form.getCertificationType(),
                        getResumeById(form.getResumeId())
                ))
                .toList();

        certificationRepository.saveAll(certifications);
    }

    private Resume getResumeById(Long resumeId) {
        return em.getReference(Resume.class, resumeId);
    }

    private static Certification generateCertification(Long id, String name, String organization, String credential, LocalDate acquisitionAt, String grade, CertificationType certificationType, Resume resume) {
        return Certification.builder()
                .id(id)
                .resume(resume)
                .name(name)
                .organization(organization)
                .credential(credential)
                .acquisitionAt(acquisitionAt)
                .grade(grade)
                .certificationType(certificationType)
                .build();
    }
}
