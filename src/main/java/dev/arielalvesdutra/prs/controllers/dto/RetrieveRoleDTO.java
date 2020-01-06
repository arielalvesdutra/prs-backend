package dev.arielalvesdutra.prs.controllers.dto;

import dev.arielalvesdutra.prs.entities.Permission;
import dev.arielalvesdutra.prs.entities.Role;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class RetrieveRoleDTO {

    private Long id;

    private String name;

    private String description;

    private Instant createdAt;

    public RetrieveRoleDTO(Role role) {
        setId(role.getId());
        setName(role.getName());
        setDescription(role.getDescription());
        setCreatedAt(role.getCreatedAt());
    }

    public static Page<RetrieveRoleDTO> toPage(Page<Role> categoriesPage) {
        return categoriesPage.map(RetrieveRoleDTO::new);
    }

    public static List<RetrieveRoleDTO> fromSetToDTOList(Set<Role> roles) {
        List<RetrieveRoleDTO> retrieveRoleDtoList = new ArrayList<>();

        for (Role role : roles) {
            retrieveRoleDtoList.add(new RetrieveRoleDTO(role));
        }

        return retrieveRoleDtoList;
    }
}