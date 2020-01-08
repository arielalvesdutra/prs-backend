package dev.arielalvesdutra.prs.controllers.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class UpdateUserRolesDTO {

    private List<Long> rolesIds = new ArrayList<>();

    public UpdateUserRolesDTO(List<Long> rolesIds) {
        setRolesIds(rolesIds);
    }
}
