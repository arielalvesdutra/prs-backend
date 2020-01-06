package dev.arielalvesdutra.prs.controllers;

import dev.arielalvesdutra.prs.controllers.dto.*;
import dev.arielalvesdutra.prs.entities.Permission;
import dev.arielalvesdutra.prs.entities.Role;
import dev.arielalvesdutra.prs.services.PermissionService;
import dev.arielalvesdutra.prs.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/roles")
@RestController
public class RoleController {
    
    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RetrieveRoleDTO> create(
            @Valid @RequestBody CreateRoleDTO createDto,
            UriComponentsBuilder uriBuilder) {

        Role createdRole = roleService.create(createDto.toRole());
        URI uri = uriBuilder.path("/roles/{id}")
                .buildAndExpand(createdRole.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new RetrieveRoleDTO(createdRole));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<RetrieveRoleDTO>> retrieveAll(
            @PageableDefault(sort="name", page = 0, size = 10) Pageable pagination) {

        Page<Role> rolesPage = roleService.findAll(pagination);

        return ResponseEntity.ok().body(RetrieveRoleDTO.toPage(rolesPage));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{roleId}")
    public ResponseEntity<RetrieveRoleDTO> retrieveById(@PathVariable Long roleId) {

        Role role = roleService.findById(roleId);

        return ResponseEntity.ok().body(new RetrieveRoleDTO(role));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{roleId}")
    public ResponseEntity<?> deleteById(@PathVariable Long roleId) {

        roleService.deleteById(roleId);

        return ResponseEntity.ok().build();
    }
    
    @RequestMapping(method = RequestMethod.PUT, path = "/{roleId}")
    public ResponseEntity<RetrieveRoleDTO> updateById(
            @PathVariable Long roleId,
            @Valid @RequestBody UpdateRoleDTO updateDto) {

        Role updatedRole = roleService.update(roleId, updateDto.toRole());

        return ResponseEntity.ok().body(new RetrieveRoleDTO(updatedRole));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{roleId}/permissions")
    public ResponseEntity<List<RetrievePermissionDTO>> updatePermissions(
            @PathVariable Long roleId,
            @Valid @RequestBody UpdateRolePermissionsDTO updateDto) {

        List<Permission> foundPermissions = permissionService.findAllByIds(updateDto.getPermissionsIds());
        Set<Permission> rolePermissions = roleService.updatePermissions(roleId, new HashSet<>(foundPermissions));
        List<RetrievePermissionDTO> retrieveDtoList = RetrievePermissionDTO.fromSetToDTOList(rolePermissions);

        return ResponseEntity.ok().body(retrieveDtoList);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{roleId}/permissions")
    public Page<RetrievePermissionDTO> retrieveRolePermissions(
            @PathVariable Long roleId,
            @PageableDefault(sort="name", page = 0, size = 10) Pageable pagination) {

        Page<Permission> permissionsPage = roleService.findAllPermissions(roleId, pagination);

        return RetrievePermissionDTO.toPage(permissionsPage);
    }
}
