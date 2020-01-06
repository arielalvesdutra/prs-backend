package dev.arielalvesdutra.prs.builders.dto;

import dev.arielalvesdutra.prs.controllers.dto.CreateRoleDTO;

public class CreateRoleDTOBuilder {
    CreateRoleDTO createDto = new CreateRoleDTO();

    public CreateRoleDTOBuilder withName(String name) {
        createDto.setName(name);
        return this;
    }

    public CreateRoleDTOBuilder withDescription(String description) {
        createDto.setDescription(description);
        return this;
    }

    public CreateRoleDTO build() {
        return createDto;
    }
}
