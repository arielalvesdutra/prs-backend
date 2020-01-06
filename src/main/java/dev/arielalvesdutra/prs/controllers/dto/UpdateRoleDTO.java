package dev.arielalvesdutra.prs.controllers.dto;

import dev.arielalvesdutra.prs.entities.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateRoleDTO {

    @NotNull
    @Size(min = 2, max = 255)
    private String name;

    private String description;

    public Role toRole() {
        return new Role()
                .setName(getName())
                .setDescription(getDescription());
    }
}