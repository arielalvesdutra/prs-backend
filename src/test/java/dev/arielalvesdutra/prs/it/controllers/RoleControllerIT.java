package dev.arielalvesdutra.prs.it.controllers;

import dev.arielalvesdutra.prs.builders.dto.CreateRoleDTOBuilder;
import dev.arielalvesdutra.prs.builders.dto.UpdateRoleDTOBuilder;
import dev.arielalvesdutra.prs.builders.dto.UpdateRolePermissionsDTOBuilder;
import dev.arielalvesdutra.prs.controllers.dto.*;
import dev.arielalvesdutra.prs.entities.Permission;
import dev.arielalvesdutra.prs.entities.Role;
import dev.arielalvesdutra.prs.repositories.PermissionRepository;
import dev.arielalvesdutra.prs.repositories.RoleRepository;
import dev.arielalvesdutra.prs.services.PermissionService;
import dev.arielalvesdutra.prs.services.RoleService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static dev.arielalvesdutra.prs.factories.PermissionFactory.newPermission;
import static dev.arielalvesdutra.prs.factories.RoleFactory.newRole;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("it")
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class RoleControllerIT {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private TestRestTemplate restTemplate;

    @After
    public void tearDown() {
        roleRepository.deleteAll();
        permissionRepository.deleteAll();
    }

    @Test
    public void create_shouldReturnRoleWithStatus201() {
        CreateRoleDTO createDto = new CreateRoleDTOBuilder()
                .withName("Sales")
                .withDescription("Sales Role")
                .build();
        HttpEntity<CreateRoleDTO> httpEntity = new HttpEntity<>(createDto, null);

        ResponseEntity<RetrieveRoleDTO> response = this.restTemplate.exchange(
                "/roles",
                HttpMethod.POST,
                httpEntity,
                RetrieveRoleDTO.class);
        RetrieveRoleDTO retrieveDto = response.getBody();

        assertThat(response)
                .withFailMessage("Retorno da requisição não pode ser nulo").isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(retrieveDto).isNotNull();
        assertThat(retrieveDto.getId()).isNotNull();
        assertThat(retrieveDto.getName()).isEqualTo(createDto.getName());
        assertThat(retrieveDto.getDescription()).isEqualTo(createDto.getDescription());
    }

    @Test
    public void retrieveAll_withPagination_shouldReturnRolePage() {
        Role createdRole = roleService.create(newRole());
        RetrieveRoleDTO expectedRole = new RetrieveRoleDTO(createdRole);

        ResponseEntity<PagedModel<RetrieveRoleDTO>> response = restTemplate.exchange(
                "/roles",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedModel<RetrieveRoleDTO>>() {});
        PagedModel<RetrieveRoleDTO> resources = response.getBody();
        List<RetrieveRoleDTO> roles = new ArrayList<>(resources.getContent());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(roles).size().isEqualTo(1);
        assertThat(roles).contains(expectedRole);
    }

    @Test
    public void retrieveById_shouldReturnRole() {
        Role createdRole = roleService.create(newRole());
        RetrieveRoleDTO expectedRole = new RetrieveRoleDTO(createdRole);
        String url = "/roles/" + expectedRole.getId();

        ResponseEntity<RetrieveRoleDTO> response =
                restTemplate.getForEntity(url, RetrieveRoleDTO.class);
        RetrieveRoleDTO retrievedRoleDto = response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(retrievedRoleDto).isEqualTo(expectedRole);
    }

    @Test
    public void deleteRoleById_shouldWork() {
        Role createdRole = roleService.create(newRole());
        String url = "/roles/" + createdRole.getId();

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                String.class);
        Optional<Role> fetchedRole = roleRepository.findById(createdRole.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(fetchedRole.isPresent()).isFalse();
    }

    @Test
    public void updateById_shouldWork() {
        Role createdRole = roleService.create(newRole());
        String url = "/roles/" + createdRole.getId();
        HttpHeaders headers = new HttpHeaders();
        UpdateRoleDTO updateDto = new UpdateRoleDTOBuilder()
                .withName("Assistant Regional Manager")
                .withDescription("Assistant Regional Manager Role")
                .build();
        HttpEntity<UpdateRoleDTO> httpEntity = new HttpEntity<>(updateDto, headers);

        ResponseEntity<RetrieveRoleDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpEntity,
                RetrieveRoleDTO.class);
        RetrieveRoleDTO responseRole = response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseRole).isNotNull();
        assertThat(responseRole.getId()).isEqualTo(createdRole.getId());
        assertThat(responseRole.getName()).isEqualTo(updateDto.getName());
        assertThat(responseRole.getDescription()).isEqualTo(updateDto.getDescription());
        assertThat(responseRole.getCreatedAt().equals(createdRole.getCreatedAt())).isTrue();
    }

    @Test
    public void updateRolePermissions_shouldWork() {
        Role role = roleService.create(newRole());
        Permission permission = permissionService.create(newPermission());
        RetrievePermissionDTO expectedPermissionDto = new RetrievePermissionDTO(permission);
        UpdateRolePermissionsDTO updateDto = new UpdateRolePermissionsDTOBuilder()
                .withPermissionId(permission.getId())
                .build();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UpdateRolePermissionsDTO> httpEntity = new HttpEntity<>(updateDto, headers);
        String url = "/roles/" + role.getId() + "/permissions";

        ResponseEntity<List<RetrievePermissionDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpEntity,
                new ParameterizedTypeReference<List<RetrievePermissionDTO>>() {});
        List<RetrievePermissionDTO> permissionsDto = response.getBody();
        List<Permission> fetchedPermissions = roleService.findAllPermissions(role.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(permissionsDto).contains(expectedPermissionDto);
        assertThat(fetchedPermissions).contains(permission);
    }

    @Test
    public void retrieveRolePermissions_shouldReturnPermissionsPage() {
        Role role = roleService.create(newRole());
        Permission permission = permissionService.create(newPermission());
        roleService.updatePermissions(role.getId(), toPermissionSet(permission));
        RetrievePermissionDTO expectedPermissionDto = new RetrievePermissionDTO(permission);
        String url = "/roles/" + role.getId() + "/permissions";

        ResponseEntity<PagedModel<RetrievePermissionDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedModel<RetrievePermissionDTO>>() {});
        PagedModel<RetrievePermissionDTO> resources = response.getBody();
        List<RetrievePermissionDTO> retrievedPermissionsList = new ArrayList<>(resources.getContent());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(retrievedPermissionsList).contains(expectedPermissionDto);
    }

    private Set<Permission> toPermissionSet(Permission permission) {
        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(permission);
        return permissionSet;
    }
}

