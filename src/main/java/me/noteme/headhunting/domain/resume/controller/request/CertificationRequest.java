package me.noteme.headhunting.domain.resume.controller.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import me.noteme.headhunting.domain.resume.entity.CertificationType;

import java.time.LocalDate;
import java.util.List;

@Data
public class CertificationRequest {
    @Valid
    List<CertificationForm> certifications;
}
