package dev.arielalvesdutra.prs.it.controllers;

import dev.arielalvesdutra.prs.builders.dto.CreateUserDTOBuilder;
import dev.arielalvesdutra.prs.builders.dto.UpdateUserDTOBuilder;
import dev.arielalvesdutra.prs.controllers.dto.*;
import dev.arielalvesdutra.prs.entities.Role;
import dev.arielalvesdutra.prs.entities.User;
import dev.arielalvesdutra.prs.repositories.RoleRepository;
import dev.arielalvesdutra.prs.repositories.UserRepository;
import dev.arielalvesdutra.prs.services.RoleService;
import dev.arielalvesdutra.prs.services.UserService;
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

import static dev.arielalvesdutra.prs.factories.RoleFactory.newRole;
import static dev.arielalvesdutra.prs.factories.UserFactory.newUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("it")
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UserControllerIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @After
    public void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
    
    @Test
    public void create_shouldReturnUserWithStatus201() {
        CreateUserDTO createDto = new CreateUserDTOBuilder()
                .withName("Pam Beesly")
                .withEmail("pam@dundermifflin.com")
                .withPassword("password")
                .build();
        HttpEntity<CreateUserDTO> httpEntity = new HttpEntity<>(createDto, null);

        ResponseEntity<RetrieveUserDTO> response = this.restTemplate.exchange(
                "/users",
                HttpMethod.POST,
                httpEntity,
                RetrieveUserDTO.class);
        RetrieveUserDTO retrieveDto = response.getBody();

        assertThat(response)
                .withFailMessage("Retorno da requisição não pode ser nulo").isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(retrieveDto).isNotNull();
        assertThat(retrieveDto.getId()).isNotNull();
        assertThat(retrieveDto.getName()).isEqualTo(createDto.getName());
        assertThat(retrieveDto.getEmail()).isEqualTo(createDto.getEmail());
    }

    @Test
    public void retrieveAll_withPagination_shouldReturnUserPage() {
        User createdUser = userService.create(newUser());
        RetrieveUserDTO expectedUser = new RetrieveUserDTO(createdUser);

        ResponseEntity<PagedModel<RetrieveUserDTO>> response = restTemplate.exchange(
                "/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedModel<RetrieveUserDTO>>() {});
        PagedModel<RetrieveUserDTO> resources = response.getBody();
        List<RetrieveUserDTO> users = new ArrayList<>(resources.getContent());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(users).size().isEqualTo(1);
        assertThat(users).contains(expectedUser);
    }
    @Test
    public void retrieveById_shouldReturnUser() {
        User createdUser = userService.create(newUser());
        RetrieveUserDTO expectedUser = new RetrieveUserDTO(createdUser);
        String url = "/users/" + expectedUser.getId();

        ResponseEntity<RetrieveUserDTO> response =
                restTemplate.getForEntity(url, RetrieveUserDTO.class);
        RetrieveUserDTO retrievedUserDto = response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(retrievedUserDto).isEqualTo(expectedUser);
    }

    @Test
    public void deleteUserById_shouldWork() {
        User createdUser = userService.create(newUser());
        String url = "/users/" + createdUser.getId();

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                String.class);
        Optional<User> fetchedUser = userRepository.findById(createdUser.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(fetchedUser.isPresent()).isFalse();
    }

    @Test
    public void updateById_shouldWork() {
        User createdUser = userService.create(newUser());
        String url = "/users/" + createdUser.getId();
        HttpHeaders headers = new HttpHeaders();
        UpdateUserDTO updateDto = new UpdateUserDTOBuilder()
                .withName("Dwight Schrute")
                .withEmail("dwight@dundermifflen.com.br")
                .withPassword("newpassword")
                .build();
        HttpEntity<UpdateUserDTO> httpEntity = new HttpEntity<>(updateDto, headers);

        ResponseEntity<RetrieveUserDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpEntity,
                RetrieveUserDTO.class);
        RetrieveUserDTO responseUser = response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseUser).isNotNull();
        assertThat(responseUser.getId()).isEqualTo(createdUser.getId());
        assertThat(responseUser.getName()).isEqualTo(updateDto.getName());
        assertThat(responseUser.getEmail()).isEqualTo(updateDto.getEmail());
    }

    @Test
    public void updateUserRoles_shouldWork() {
        User createdUser = userService.create(newUser());
        Role role = roleService.create(newRole());
        RetrieveRoleDTO expectedRoleDto = new RetrieveRoleDTO(role);
        UpdateUserRolesDTO updateDto = new UpdateUserRolesDTO(Arrays.asList(role.getId()));
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UpdateUserRolesDTO> httpEntity = new HttpEntity<>(updateDto, headers);
        String url = "/users/" + createdUser.getId() + "/roles";

        ResponseEntity<List<RetrieveRoleDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpEntity,
                new ParameterizedTypeReference<List<RetrieveRoleDTO>>() {});
        List<RetrieveRoleDTO> rolesListDto = response.getBody();
        List<Role> fetchedRoles = userService.findAllRoles(createdUser.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(rolesListDto).contains(expectedRoleDto);
        assertThat(fetchedRoles).contains(role);
    }

    @Test
    public void retrieveUserRoles_shouldReturnRolesPage() {
        User user = userService.create(newUser());
        Role role = roleService.create(newRole());
        userService.updateRoles(user.getId(), toRoleSet(role));
        RetrieveRoleDTO expectedRoleDto = new RetrieveRoleDTO(role);
        String url = "/users/" + user.getId() + "/roles";

        ResponseEntity<PagedModel<RetrieveRoleDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedModel<RetrieveRoleDTO>>() {});
        PagedModel<RetrieveRoleDTO> resources = response.getBody();
        List<RetrieveRoleDTO> retrievedRolesList = new ArrayList<>(resources.getContent());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(retrievedRolesList).contains(expectedRoleDto);
    }

    private Set<Role> toRoleSet(Role role) {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        return roleSet;
    }
}
