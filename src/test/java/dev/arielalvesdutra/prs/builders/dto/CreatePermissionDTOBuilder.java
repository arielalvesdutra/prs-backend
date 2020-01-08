package dev.arielalvesdutra.prs.builders.dto;

import dev.arielalvesdutra.prs.builders.CategoryBuilder;
import dev.arielalvesdutra.prs.controllers.dto.CreatePermissionDTO;

public class CreatePermissionDTOBuilder {
    CreatePermissionDTO createDto = new CreatePermissionDTO();

    public CreatePermissionDTOBuilder withName(String name) {
        createDto.setName(name);
        return this;
    }

    public CreatePermissionDTOBuilder withDescription(String description) {
        createDto.setDescription(description);
        return this;
    }

    public CreatePermissionDTO build() {
        return createDto;
    }
}
