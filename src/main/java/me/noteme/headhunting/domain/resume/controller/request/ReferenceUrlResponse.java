package me.noteme.headhunting.domain.resume.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReferenceUrlResponse {
    private Long id;

    private String url;
}
