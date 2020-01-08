package dev.arielalvesdutra.prs.controllers.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter @Setter @NoArgsConstructor @ToString
public class LoginFormDTO {

    private String password;

    private String email;

    public LoginFormDTO(String email, String password) {
        setEmail(email);
        setPassword(password);
    }

    public UsernamePasswordAuthenticationToken toUsernamePasswordAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(
                this.getEmail(), this.getPassword());
    }
}
