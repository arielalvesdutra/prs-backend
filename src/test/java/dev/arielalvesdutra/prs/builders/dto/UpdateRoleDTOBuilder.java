package dev.arielalvesdutra.prs.builders.dto;

import dev.arielalvesdutra.prs.controllers.dto.UpdateRoleDTO;

public class UpdateRoleDTOBuilder {
    UpdateRoleDTO updateDto = new UpdateRoleDTO();

    public  UpdateRoleDTOBuilder withName(String name) {
        updateDto.setName(name);
        return this;
    }

    public  UpdateRoleDTOBuilder withDescription(String description) {
        updateDto.setDescription(description);
        return this;
    }

    public UpdateRoleDTO build() {
        return updateDto;
    }
}
