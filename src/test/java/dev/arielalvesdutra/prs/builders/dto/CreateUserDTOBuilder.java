package dev.arielalvesdutra.prs.builders.dto;

import dev.arielalvesdutra.prs.controllers.dto.CreateUserDTO;

public class CreateUserDTOBuilder {
    CreateUserDTO user = new CreateUserDTO();

    public CreateUserDTOBuilder withName(String name) {
        user.setName(name);
        return this;
    }

    public CreateUserDTOBuilder withEmail(String email) {
        user.setEmail(email);
        return this;
    }

    public CreateUserDTOBuilder withPassword(String password) {
        user.setPassword(password);
        return this;
    }

    public CreateUserDTO build() {
        return user;
    }
}
