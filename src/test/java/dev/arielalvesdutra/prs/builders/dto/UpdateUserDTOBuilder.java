package dev.arielalvesdutra.prs.builders.dto;

import dev.arielalvesdutra.prs.controllers.dto.UpdateUserDTO;

public class UpdateUserDTOBuilder {
    UpdateUserDTO user = new UpdateUserDTO();

    public UpdateUserDTOBuilder withName(String name) {
        user.setName(name);
        return this;
    }

    public UpdateUserDTOBuilder withEmail(String email) {
        user.setEmail(email);
        return this;
    }

    public UpdateUserDTOBuilder withPassword(String password) {
        user.setPassword(password);
        return this;
    }

    public UpdateUserDTO build() {
        return user;
    }
}
