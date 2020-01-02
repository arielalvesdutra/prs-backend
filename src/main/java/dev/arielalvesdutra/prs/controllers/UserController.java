package dev.arielalvesdutra.prs.controllers;

import dev.arielalvesdutra.prs.controllers.dto.*;
import dev.arielalvesdutra.prs.entities.Role;
import dev.arielalvesdutra.prs.entities.User;
import dev.arielalvesdutra.prs.services.RoleService;
import dev.arielalvesdutra.prs.services.UserService;
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

@RequestMapping("/users")
@RestController
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RetrieveUserDTO> create(
            @Valid @RequestBody CreateUserDTO createDto,
            UriComponentsBuilder uriBuilder) {

        User createdUser = userService.create(createDto.toUser());
        URI uri = uriBuilder.path("/users/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new RetrieveUserDTO(createdUser));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<RetrieveUserDTO>> retrieveAll(
            @PageableDefault(sort="name", page = 0, size = 10) Pageable pagination) {

        Page<User> usersPage = userService.findAll(pagination);

        return ResponseEntity.ok().body(RetrieveUserDTO.toPage(usersPage));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{userId}")
    public ResponseEntity<RetrieveUserDTO> retrieveById(@PathVariable Long userId) {

        User user = userService.findById(userId);

        return ResponseEntity.ok().body(new RetrieveUserDTO(user));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{userId}")
    public ResponseEntity<?> deleteById(@PathVariable Long userId) {

        userService.deleteById(userId);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{userId}")
    public ResponseEntity<RetrieveUserDTO> updateById(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserDTO updateDto) {

        User updatedUser = userService.update(userId, updateDto.toUser());

        return ResponseEntity.ok().body(new RetrieveUserDTO(updatedUser));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{userId}/roles")
    public ResponseEntity<List<RetrieveRoleDTO>> updateUserRoles(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRolesDTO updateDto) {

        List<Role> foundRoles = roleService.findAllByIds(updateDto.getRolesIds());
        Set<Role> userRoles = userService.updateRoles(userId, new HashSet<>(foundRoles));
        List<RetrieveRoleDTO> retrieveDtoList = RetrieveRoleDTO.fromSetToDTOList(userRoles);

        return ResponseEntity.ok().body(retrieveDtoList);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{userId}/roles")
    public Page<RetrieveRoleDTO> retrieveUserRoles(
            @PathVariable Long userId,
            @PageableDefault(sort="name", page = 0, size = 10) Pageable pagination) {

        Page<Role> rolePage = userService.findAllRoles(userId, pagination);

        return RetrieveRoleDTO.toPage(rolePage);
    }
}
