package me.noteme.headhunting.domain.resume.controller.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.noteme.headhunting.domain.resume.entity.CertificationType;

import java.time.LocalDate;

@Data
public class CertificationRequest {
    private String name;
    private String organization;
    private String credential;
    private LocalDate acquisitionDate;
    private String grade;
    private CertificationType certificationType;
}
