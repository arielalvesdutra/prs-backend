package dev.arielalvesdutra.prs.builders.dto;

import dev.arielalvesdutra.prs.controllers.dto.UpdatePermissionDTO;

public class UpdatePermissionDTOBuilder {
    UpdatePermissionDTO updateDto = new UpdatePermissionDTO();

    public  UpdatePermissionDTOBuilder withName(String name) {
        updateDto.setName(name);
        return this;
    }

    public  UpdatePermissionDTOBuilder withDescription(String description) {
        updateDto.setDescription(description);
        return this;
    }

    public UpdatePermissionDTO build() {
        return updateDto;
    }
}
