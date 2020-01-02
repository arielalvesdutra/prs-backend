package dev.arielalvesdutra.prs.controllers.dto;

import dev.arielalvesdutra.prs.entities.Permission;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class CreatePermissionDTO {

    @NotNull
    @Size(min = 2)
    private String name;

    private String description;

    public Permission toPermission() {
        return new Permission()
                .setName(getName())
                .setDescription(getDescription());
    }
}
