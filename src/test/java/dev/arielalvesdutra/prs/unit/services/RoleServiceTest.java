package dev.arielalvesdutra.prs.unit.services;

import dev.arielalvesdutra.prs.entities.Permission;
import dev.arielalvesdutra.prs.entities.Role;
import dev.arielalvesdutra.prs.repositories.RoleRepository;
import dev.arielalvesdutra.prs.services.PermissionService;
import dev.arielalvesdutra.prs.services.RoleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.*;

import static dev.arielalvesdutra.prs.AssertionErrorMessages.METHOD_MUST_HAVE_TRANSACTION_ANNOTATION;
import static dev.arielalvesdutra.prs.AssertionErrorMessages.NOT_NULL_RETURN;
import static dev.arielalvesdutra.prs.factories.PermissionFactory.newPermissionWithId;
import static dev.arielalvesdutra.prs.factories.RoleFactory.newRole;
import static dev.arielalvesdutra.prs.factories.RoleFactory.newRoleWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionService permissionService;

    private RoleService roleService;

    @Before
    public void setUp() {
        roleService = new RoleService(roleRepository, permissionService);
    }

    @Test
    public void class_mustHaveServiceAnnotation() {
        assertThat(RoleService.class.isAnnotationPresent(Service.class))
                .withFailMessage("A classe deve ter a anotaçao @Service")
                .isTrue();
    }

    @Test
    public void create_shouldWork() {
        Role roleToCreate = newRole();
        Role expectedRole = newRoleWithId();
        given((roleRepository.save(roleToCreate))).willReturn(expectedRole);

        Role createdRole = roleService.create(roleToCreate);

        assertThat(createdRole)
                .withFailMessage("Retorno do serviço não pode ser nulo.")
                .isNotNull();
        assertThat(expectedRole.getId()).isEqualTo(createdRole.getId());
        assertThat(expectedRole.getName()).isEqualTo(createdRole.getName());
        assertThat(expectedRole.getDescription()).isEqualTo(createdRole.getDescription());
        assertThat(expectedRole.getCreatedAt()).isEqualTo(createdRole.getCreatedAt());
    }

    @Test
    public void findById_shouldReturnRole() {
        Role expectedRole = newRoleWithId();
        given(roleRepository.findById(expectedRole.getId()))
                .willReturn(Optional.of(expectedRole));

        Role fetchedRole = roleService.findById(expectedRole.getId());

        assertThat(fetchedRole).withFailMessage(NOT_NULL_RETURN).isNotNull();
        assertThat(expectedRole.getId()).isEqualTo(fetchedRole.getId());
        assertThat(expectedRole.getName()).isEqualTo(fetchedRole.getName());
        assertThat(expectedRole.getDescription()).isEqualTo(fetchedRole.getDescription());
        assertThat(expectedRole.getCreatedAt()).isEqualTo(fetchedRole.getCreatedAt());
    }

    @Test
    public void findAll_shouldReturnRoleList() {
        Role expectedRole = newRoleWithId();
        given(roleRepository.findAll()).willReturn(Arrays.asList(expectedRole));

        List<Role> fetchedRole = roleService.findAll();

        assertThat(fetchedRole).withFailMessage(NOT_NULL_RETURN).isNotNull();
        assertThat(fetchedRole).contains(expectedRole);
    }

    @Test
    public void findAll_withPageable_shouldReturnRolePage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Role expectedRole = newRoleWithId();
        List<Role> expectedList = Arrays.asList(expectedRole);
        Page<Role> rolePage = new PageImpl<>(expectedList, pageable, 10);
        given(roleRepository.findAll(pageable)).willReturn(rolePage);

        Page<Role> fetchedRolePage = roleService.findAll(pageable);
        if (fetchedRolePage == null) fail("Retorno da paginação não pode ser nulo");
        List<Role> roleList = fetchedRolePage.getContent();

        assertThat(roleList).withFailMessage("Lista de papéis não pode ser nula").isNotNull();
        assertThat(roleList).contains(expectedRole);
    }

    @Test
    public void findAllByIds_shouldReturnRoleList(){
        Role role = newRoleWithId();
        List<Role> roleList = Arrays.asList(role);
        List<Long> roleIdsList = Arrays.asList(role.getId());
        given(roleRepository.findAllById(roleIdsList)).willReturn(roleList);

        List<Role> fetchedRoleList = roleService.findAllByIds(roleIdsList);

        assertThat(fetchedRoleList).withFailMessage(NOT_NULL_RETURN).isNotNull();
        assertThat(fetchedRoleList).contains(role);
    }

    @Test
    public void findAllByUsersId_shouldReturnRoleList() {
        Role role = newRoleWithId();
        List<Role> roleList = Arrays.asList(role);
        Long userId = 1L;
        given(roleRepository.findAllByUsers_Id(userId)).willReturn(roleList);

        List<Role> fetchedRoleList = roleService.findAllByUserId(userId);

        assertThat(fetchedRoleList).withFailMessage(NOT_NULL_RETURN).isNotNull();
        assertThat(fetchedRoleList).contains(role);
        verify(roleRepository, times(1)).findAllByUsers_Id(userId);
    }

    @Test
    public void update_shouldReturnUpdatedRole() {
        Role role = newRoleWithId();
        given(roleRepository.findById(role.getId())).willReturn(Optional.of(role));

        Role fetchedRole = roleService.findById(role.getId());
        fetchedRole
                .setName("Assistant Regional Manager")
                .setDescription("Assistant Regional Manager Role");
        Role updatedRole = roleService.update(fetchedRole.getId(), fetchedRole);

        assertThat(updatedRole)
                .withFailMessage("Retorno da atualização não pode ser nulo").isNotNull();
        assertThat(updatedRole.getId()).isEqualTo(fetchedRole.getId());
        assertThat(updatedRole.getName()).isEqualTo(fetchedRole.getName());
        assertThat(updatedRole.getDescription()).isEqualTo(fetchedRole.getDescription());
        assertThat(updatedRole.getCreatedAt()).isEqualTo(fetchedRole.getCreatedAt());
        verify(roleRepository, times(2)).findById(role.getId());
    }

    @Test
    public void update_mustHaveTransactionalAnnotation() throws NoSuchMethodException {
        boolean isAnnotationPresent = RoleService.class
                .getDeclaredMethod("update", Long.class, Role.class)
                .isAnnotationPresent(Transactional.class);

        assertThat(isAnnotationPresent)
                .withFailMessage(METHOD_MUST_HAVE_TRANSACTION_ANNOTATION)
                .isTrue();
    }

    @Test
    public void deleteById_shouldDeleteRole() {
        Role roleToDelete = newRoleWithId();
        given(roleRepository.findById(roleToDelete.getId())).willReturn(Optional.of(roleToDelete));

        roleService.deleteById(roleToDelete.getId());

        verify(roleRepository, atLeastOnce()).deleteById(roleToDelete.getId());
    }

    @Test
    public void deleteById_mustHaveTransactionalAnnotation() throws NoSuchMethodException {
        boolean isAnnotationPresent = RoleService.class
                .getDeclaredMethod("deleteById", Long.class)
                .isAnnotationPresent(Transactional.class);

        assertThat(isAnnotationPresent)
                .withFailMessage(METHOD_MUST_HAVE_TRANSACTION_ANNOTATION)
                .isTrue();
    }

    @Test
    public void updateRolePermissions_shouldWork() {
        Role role = newRoleWithId();
        Permission permission = newPermissionWithId();
        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(permission);
        given(roleRepository.findById(role.getId())).willReturn(Optional.of(role));

        Set<Permission> updatedRolePermissionSet =
                roleService.updatePermissions(role.getId(), permissionSet);

        assertThat(updatedRolePermissionSet)
                .withFailMessage("Retorno não pode ser nulo")
                .isNotNull();
        assertThat(updatedRolePermissionSet).contains(permission);
        verify(roleRepository, atLeastOnce()).findById(role.getId());
    }

    @Test
    public void updateRolePermissions_mustHaveTransactionalAnnotation() throws NoSuchMethodException {
        boolean isAnnotationPresent = RoleService.class
                .getDeclaredMethod("updatePermissions", Long.class, Set.class)
                .isAnnotationPresent(Transactional.class);

        assertThat(isAnnotationPresent)
                .withFailMessage(METHOD_MUST_HAVE_TRANSACTION_ANNOTATION)
                .isTrue();
    }

    @Test
    public void findAllRolePermissions_shouldReturnPermissionsList(){
        Role role = newRoleWithId();
        Permission permission = newPermissionWithId();
        List<Permission> permissionList = Arrays.asList(permission);
        given(roleRepository.findById(role.getId())).willReturn(Optional.of(role));
        given(permissionService.findAllByRoleId(role.getId())).willReturn(permissionList);

        List<Permission> fetchedPermissionsList = roleService.findAllPermissions(role.getId());

        assertThat(fetchedPermissionsList).withFailMessage(NOT_NULL_RETURN).isNotNull();
        assertThat(fetchedPermissionsList).contains(permission);
        verify(roleRepository, atLeastOnce()).findById(role.getId());
        verify(permissionService, atLeastOnce()).findAllByRoleId(role.getId());
    }

    @Test
    public void findAllRolePermissions_withPageable_shouldReturnPermissionsPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Role role = newRoleWithId();
        Permission permission = newPermissionWithId();
        List<Permission> permissionList = Arrays.asList(permission);
        Page<Permission> permissionPage = new PageImpl<>(permissionList, pageable, 10);
        given(roleRepository.findById(role.getId())).willReturn(Optional.of(role));
        given(permissionService.findAllByRoleId(role.getId(), pageable)).willReturn(permissionPage);

        Page<Permission> responsePermissionPage = roleService.findAllPermissions(role.getId(), pageable);
        if (permissionPage == null) fail(NOT_NULL_RETURN);
        List<Permission> responsePermissionList = responsePermissionPage.getContent();

        assertThat(responsePermissionList).withFailMessage(NOT_NULL_RETURN).isNotNull();
        assertThat(responsePermissionList).contains(permission);
        verify(roleRepository, atLeastOnce()).findById(role.getId());
        verify(permissionService, atLeastOnce()).findAllByRoleId(role.getId(), pageable);
    }
}
