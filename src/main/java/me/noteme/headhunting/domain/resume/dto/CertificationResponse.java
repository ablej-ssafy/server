package me.noteme.headhunting.domain.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.resume.controller.request.CertificationForm;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class CertificationResponse {
    List<CertificationForm> certifications;
}
