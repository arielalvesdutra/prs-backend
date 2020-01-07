package dev.arielalvesdutra.prs.controllers.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class TokenDTO {

    private String token;

    public TokenDTO(String token) {
        this.token = token;
    }
}
