package dev.arielalvesdutra.prs.controllers.dto;

import dev.arielalvesdutra.prs.entities.Permission;
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
public class RetrievePermissionDTO {

    private Long id;

    private String name;

    private String description;

    private Instant createdAt;

    public RetrievePermissionDTO(Permission permission) {
        setId(permission.getId());
        setName(permission.getName());
        setDescription(permission.getDescription());
        setCreatedAt(permission.getCreatedAt());
    }

    public static Page<RetrievePermissionDTO> toPage(Page<Permission> categoriesPage) {
        return categoriesPage.map(RetrievePermissionDTO::new);
    }

    /**
     * Receive a Permission Set and converts to a RetrievePermissionDTO List
     *
     * @param permissions
     * @return
     */
    public static List<RetrievePermissionDTO> fromSetToDTOList(Set<Permission> permissions) {

        List<RetrievePermissionDTO> retrievePermissionDto = new ArrayList<>();

        for (Permission permission : permissions) {
            retrievePermissionDto.add(new RetrievePermissionDTO(permission));
        }

        return retrievePermissionDto;
    }
}
