package dev.arielalvesdutra.prs.controllers.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@ToString
@Setter @Getter
public class UpdateRolePermissionsDTO {

    @NotNull
    private List<Long> permissionsIds = new ArrayList<>();

    public UpdateRolePermissionsDTO addPermissionId(Long permissionId) {
        permissionsIds.add(permissionId);
        return this;
    }
}
