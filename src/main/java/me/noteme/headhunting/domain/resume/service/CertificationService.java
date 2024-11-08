package me.noteme.headhunting.domain.resume.service;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.resume.controller.request.CertificationForm;
import me.noteme.headhunting.domain.resume.dto.CertificationResponse;
import me.noteme.headhunting.domain.resume.entity.Certification;
import me.noteme.headhunting.domain.resume.entity.CertificationType;
import me.noteme.headhunting.domain.resume.entity.Resume;
import me.noteme.headhunting.domain.resume.repository.CertificationRepository;
import me.noteme.headhunting.domain.resume.repository.ResumeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CertificationService {
    private final CertificationRepository certificationRepository;
    private final ResumeRepository resumeRepository;

    @Transactional
    public void saveAllCertifications(Long memberId, List<CertificationForm> certificationForms) {
        // TODO: 자격 정보 저장에 대한 최대 값 검증 로직
        List<Certification> certifications = certificationForms.stream()
                .map(form -> form.toEntity(getResumeByMemberId(memberId)))
                .toList();

        certificationRepository.saveAll(certifications);
    }

    public CertificationResponse getCertifications(Long memberId, String type) {
        List<Certification> certifications;
        if (StringUtils.isEmpty(type)) {
            certifications = certificationRepository.findAllByMemberId(memberId);
            return CertificationResponse.of(getCertificationForms(certifications));
        }

        CertificationType certificationType = CertificationType.from(type);
        certifications = certificationRepository.findAllByMemberIdAndType(memberId, certificationType);
        return CertificationResponse.of(getCertificationForms(certifications));
    }

    @Transactional
    public CertificationForm createEmptyCertification(Long memberId, String type) {
        if (StringUtils.isEmpty(type)) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "요청 타입이 틀렸습니다.");
        }

        CertificationType certificationType = CertificationType.from(type);
        return CertificationForm.fromEntity(certificationRepository.save(
                Certification.of(
                        certificationType,
                        getResumeByMemberId(memberId)
                )
        ));
    }

    @Transactional
    public void deleteById(Long certificationId) {
        certificationRepository.deleteById(certificationId);
    }

    private List<CertificationForm> getCertificationForms(List<Certification> certifications) {
        return certifications.stream()
                .map(CertificationForm::fromEntity)
                .toList();
    }

    private Resume getResumeByMemberId(Long memberId) {
        return resumeRepository.findByMemberId(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 Member가 지니고 있는 Resume가 없습니다."));
    }
}
