package me.noteme.headhunting.domain.resume.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.domain.resume.controller.request.CertificationForm;
import me.noteme.headhunting.domain.resume.dto.CertificationResponse;
import me.noteme.headhunting.domain.resume.entity.Certification;
import me.noteme.headhunting.domain.resume.entity.CertificationType;
import me.noteme.headhunting.domain.resume.entity.Resume;
import me.noteme.headhunting.domain.resume.repository.CertificationRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CertificationService {
    private final CertificationRepository certificationRepository;
    private final EntityManager em;

    @Transactional
    public void saveAllCertifications(List<CertificationForm> certificationForms) {
        // TODO: 자격 정보 저장에 대한 최대 값 검증 로직
        List<Certification> certifications = certificationForms.stream()
                .map(form -> form.toEntity(getResumeById(form.getResumeId())))
                .toList();

        certificationRepository.saveAll(certifications);
    }

    public CertificationResponse getCertifications(Long userId, String type) {
        List<Certification> certifications;
        if (StringUtils.isEmpty(type)) {
            certifications = certificationRepository.findAllByMemberId(userId);
            return CertificationResponse.of(getCertificationForms(certifications));
        }

        CertificationType certificationType = CertificationType.from(type);
        certifications = certificationRepository.findAllByMemberIdAndType(userId, certificationType);
        return CertificationResponse.of(getCertificationForms(certifications));
    }

    private List<CertificationForm> getCertificationForms(List<Certification> certifications) {
        return certifications.stream()
                .map(CertificationForm::fromEntity)
                .toList();
    }

    private Resume getResumeById(Long resumeId) {
        return em.getReference(Resume.class, resumeId);
    }
}
