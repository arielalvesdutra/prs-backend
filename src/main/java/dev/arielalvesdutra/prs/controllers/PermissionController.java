package dev.arielalvesdutra.prs.controllers;

import dev.arielalvesdutra.prs.controllers.dto.CreatePermissionDTO;
import dev.arielalvesdutra.prs.controllers.dto.RetrievePermissionDTO;
import dev.arielalvesdutra.prs.controllers.dto.UpdatePermissionDTO;
import dev.arielalvesdutra.prs.entities.Permission;
import dev.arielalvesdutra.prs.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RequestMapping("/permissions")
@RestController
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RetrievePermissionDTO> create(
            @Valid @RequestBody CreatePermissionDTO createDto, UriComponentsBuilder uriBuilder) {

        Permission createdPermission = permissionService.create(createDto.toPermission());
        URI uri = uriBuilder.path("/permissions/{id}")
                .buildAndExpand(createdPermission.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new RetrievePermissionDTO(createdPermission));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<RetrievePermissionDTO>> retrieveAll(
            @PageableDefault(sort="name", page = 0, size = 10) Pageable pagination) {

        Page<Permission> permissionsPage = permissionService.findAll(pagination);

        return ResponseEntity.ok().body(RetrievePermissionDTO.toPage(permissionsPage));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{permissionId}")
    public ResponseEntity<RetrievePermissionDTO> retrieveById(@PathVariable Long permissionId) {

        Permission permission = permissionService.findById(permissionId);

        return ResponseEntity.ok().body(new RetrievePermissionDTO(permission));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{permissionId}")
    public ResponseEntity<?> deleteById(@PathVariable Long permissionId) {

        permissionService.deleteById(permissionId);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{permissionId}")
    public ResponseEntity<RetrievePermissionDTO> updateById(
            @PathVariable Long permissionId,
            @Valid @RequestBody UpdatePermissionDTO updateDto) {

        Permission updatedPermission = permissionService.update(permissionId, updateDto.toPermission());

        return ResponseEntity.ok().body(new RetrievePermissionDTO(updatedPermission));
    }
}
