package dev.arielalvesdutra.prs.controllers.dto;

import dev.arielalvesdutra.prs.entities.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CreateUserDTO {

    @NotNull
    @Size(min = 2)
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 6)
    private String password;

    public User toUser() {
        return new User()
                .setName(getName())
                .setEmail(getEmail())
                .setPassword(getPassword());
    }
}
