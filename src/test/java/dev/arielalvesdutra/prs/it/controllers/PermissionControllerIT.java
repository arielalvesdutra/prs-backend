package dev.arielalvesdutra.prs.it.controllers;

import dev.arielalvesdutra.prs.builders.dto.CreatePermissionDTOBuilder;
import dev.arielalvesdutra.prs.builders.dto.UpdatePermissionDTOBuilder;
import dev.arielalvesdutra.prs.controllers.dto.CreatePermissionDTO;
import dev.arielalvesdutra.prs.controllers.dto.RetrievePermissionDTO;
import dev.arielalvesdutra.prs.controllers.dto.UpdatePermissionDTO;
import dev.arielalvesdutra.prs.entities.Permission;
import dev.arielalvesdutra.prs.repositories.PermissionRepository;
import dev.arielalvesdutra.prs.services.PermissionService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.arielalvesdutra.prs.factories.PermissionFactory.newPermission;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("it")
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class PermissionControllerIT {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private TestRestTemplate restTemplate;

    @After
    public void tearDown() {
        permissionRepository.deleteAll();
    }

    @Test
    public void create_shouldReturnPermissionWithStatus201() {
        CreatePermissionDTO createDto = new CreatePermissionDTOBuilder()
                .withName("post.create")
                .withDescription("Criar Post")
                .build();
        HttpEntity<CreatePermissionDTO> httpEntity = new HttpEntity<>(createDto, null);

        ResponseEntity<RetrievePermissionDTO> response = restTemplate.exchange(
                "/permissions",
                HttpMethod.POST,
                httpEntity,
                RetrievePermissionDTO.class);
        RetrievePermissionDTO retrieveDto = response.getBody();

        assertThat(response)
                .withFailMessage("Retorno da requisição não pode ser nulo").isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(retrieveDto).isNotNull();
        assertThat(retrieveDto.getId()).isNotNull();
        assertThat(retrieveDto.getName()).isEqualTo(createDto.getName());
        assertThat(retrieveDto.getDescription()).isEqualTo(createDto.getDescription());
    }

    @Test
    public void retrieveAll_withPagination_shouldReturnPermissionPage() {
        Permission createdPermission = permissionService.create(newPermission());
        RetrievePermissionDTO expectedPermission = new RetrievePermissionDTO(createdPermission);

        ResponseEntity<PagedModel<RetrievePermissionDTO>> response = restTemplate.exchange(
                "/permissions",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedModel<RetrievePermissionDTO>>() {});
        PagedModel<RetrievePermissionDTO> resources = response.getBody();
        List<RetrievePermissionDTO> permissionsList = new ArrayList<>(resources.getContent());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(permissionsList).size().isEqualTo(1);
        assertThat(permissionsList).contains(expectedPermission);
    }

    @Test
    public void retrieveById_shouldReturnPermission() {
        Permission createdPermission = permissionService.create(newPermission());
        RetrievePermissionDTO expectedPermission = new RetrievePermissionDTO(createdPermission);
        String url = "/permissions/" + expectedPermission.getId();

        ResponseEntity<RetrievePermissionDTO> response =
                restTemplate.getForEntity(url, RetrievePermissionDTO.class);
        RetrievePermissionDTO retrievedPermissionDto = response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(retrievedPermissionDto).isEqualTo(expectedPermission);
    }

    @Test
    public void deletePermissionById_shouldWork() {
        Permission createdPermission = permissionService.create(newPermission());
        String url = "/permissions/" + createdPermission.getId();

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                String.class);
        Optional<Permission> fetchedPermission = permissionRepository.findById(createdPermission.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(fetchedPermission.isPresent()).isFalse();
    }

    @Test
    public void updateById_shouldWork() {
        Permission createdPermission = permissionService.create(newPermission());
        String url = "/permissions/" + createdPermission.getId();
        HttpHeaders headers = new HttpHeaders();
        UpdatePermissionDTO updateDto = new UpdatePermissionDTOBuilder()
                .withName("post.delete")
                .withDescription("Deletar Post")
                .build();
        HttpEntity<UpdatePermissionDTO> httpEntity = new HttpEntity<>(updateDto, headers);

        ResponseEntity<RetrievePermissionDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpEntity,
                RetrievePermissionDTO.class);
        RetrievePermissionDTO responsePermission = response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(responsePermission).isNotNull();
        assertThat(responsePermission.getId()).isEqualTo(createdPermission.getId());
        assertThat(responsePermission.getName()).isEqualTo(updateDto.getName());
        assertThat(responsePermission.getDescription()).isEqualTo(updateDto.getDescription());
        assertThat(responsePermission.getCreatedAt().equals(createdPermission.getCreatedAt())).isTrue();
    }
}
