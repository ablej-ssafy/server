package me.noteme.headhunting.domain.resume.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ReferenceUrlRequest {
    private Long id;
    private String url;
}
