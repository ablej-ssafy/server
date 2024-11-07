package me.noteme.headhunting.domain.resume.controller.request;


import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class CertificationRequest {
    @Valid
    List<CertificationForm> certifications;
}
