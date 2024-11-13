package me.noteme.headhunting.domain.member.feign.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class GitAccessRequest {
    @JsonProperty("client_id")
    @JsonAlias("clientId")
    private String clientId;
    @JsonProperty("client_secret")
    @JsonAlias("clientSecret")
    private String clientSecret;
    private String code;
    @JsonProperty("redirect_uri")
    @JsonAlias("redirectUri")
    private String redirectUri;
}
