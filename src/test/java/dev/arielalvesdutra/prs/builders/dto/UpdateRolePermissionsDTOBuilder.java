package dev.arielalvesdutra.prs.builders.dto;

import dev.arielalvesdutra.prs.controllers.dto.UpdateRolePermissionsDTO;

public class UpdateRolePermissionsDTOBuilder {

    UpdateRolePermissionsDTO updateDto =  new UpdateRolePermissionsDTO();

    public UpdateRolePermissionsDTOBuilder withPermissionId(Long permissionId) {
        updateDto.addPermissionId(permissionId);
        return this;
    }

    public UpdateRolePermissionsDTO build() {
        return  updateDto;
    }
}
