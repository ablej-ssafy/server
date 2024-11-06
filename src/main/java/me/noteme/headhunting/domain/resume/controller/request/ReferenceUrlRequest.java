package me.noteme.headhunting.domain.resume.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class ReferenceUrlRequest {
    private Long id;
    private String url;
}
