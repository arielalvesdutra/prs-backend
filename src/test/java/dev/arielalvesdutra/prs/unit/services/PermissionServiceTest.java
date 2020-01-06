package dev.arielalvesdutra.prs.unit.services;

import dev.arielalvesdutra.prs.entities.Permission;
import dev.arielalvesdutra.prs.repositories.PermissionRepository;
import dev.arielalvesdutra.prs.services.PermissionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static dev.arielalvesdutra.prs.factories.PermissionFactory.newPermission;
import static dev.arielalvesdutra.prs.factories.PermissionFactory.newPermissionWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    private PermissionService permissionService;

    @Before
    public void setUp() {
        permissionService = new PermissionService(permissionRepository);
    }

    @Test
    public void createPermission_shouldWork() {
        Permission permissionToCreate = newPermission();
        Permission expectedPermission = newPermissionWithId();
        given(permissionRepository.save(permissionToCreate)).willReturn(expectedPermission);

        Permission createdPermission = permissionService.create(permissionToCreate);

        assertThat(createdPermission)
                .withFailMessage("O retorno do serviço não pode ser nulo.").isNotNull();
        assertThat(expectedPermission.getId()).isEqualTo(createdPermission.getId());
        assertThat(expectedPermission.getName()).isEqualTo(createdPermission.getName());
        assertThat(expectedPermission.getDescription()).isEqualTo(createdPermission.getDescription());
        assertThat(expectedPermission.getCreatedAt()).isEqualTo(createdPermission.getCreatedAt());
    }

    @Test
    public void findById_shouldReturnPermission() {
        Permission expectedPermission = newPermissionWithId();
        given(permissionRepository.findById(expectedPermission.getId()))
                .willReturn(Optional.of(expectedPermission));

        Permission fetchedPermission = permissionService.findById(expectedPermission.getId());

        assertThat(fetchedPermission).withFailMessage("Retorno não pode ser nulo").isNotNull();
        assertThat(expectedPermission.getId()).isEqualTo(fetchedPermission.getId());
        assertThat(expectedPermission.getName()).isEqualTo(fetchedPermission.getName());
        assertThat(expectedPermission.getDescription()).isEqualTo(fetchedPermission.getDescription());
        assertThat(expectedPermission.getCreatedAt()).isEqualTo(fetchedPermission.getCreatedAt());
    }

    @Test
    public void findAllByIds_shouldReturnPermissionsFoundByIds() {
        Permission permission = newPermissionWithId();
        List<Long> permissionsIds = Arrays.asList(permission.getId());
        List<Permission> permissionList = Arrays.asList(permission);
        given(permissionRepository.findAllById(permissionsIds)).willReturn(permissionList);

        List<Permission> fetchedPermissionList = permissionService.findAllByIds(permissionsIds);

        assertThat(fetchedPermissionList)
                .withFailMessage("Retorno não pode ser nulo").isNotNull();
        assertThat(fetchedPermissionList).contains(permission);
    }

    @Test
    public void findAll_shouldReturnPermissionList() {
        Permission expectedPermission = newPermissionWithId();
        given(permissionRepository.findAll()).willReturn(Arrays.asList(expectedPermission));

        List<Permission> fetchedPermission = permissionService.findAll();

        assertThat(fetchedPermission).withFailMessage("Retorno não pode ser nulo").isNotNull();
        assertThat(fetchedPermission).contains(expectedPermission);
    }

    @Test
    public void findAll_withPageable_shouldReturnPermissionPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Permission expectedPermission = newPermissionWithId();
        List<Permission> expectedList = Arrays.asList(expectedPermission);
        given(permissionRepository.findAll(pageable))
                .willReturn(new PageImpl<>(expectedList, pageable, 10));

        Page<Permission> fetchedPermissionPage = permissionService.findAll(pageable);
        if (fetchedPermissionPage == null) fail("Retorno da paginação não pode ser nulo");
        List<Permission> permissionList = fetchedPermissionPage.getContent();

        assertThat(permissionList).describedAs("Lista de permissões não pode ser nula").isNotNull();
        assertThat(permissionList).contains(expectedPermission);
    }

    @Test
    public void update_shouldReturnUpdatedPermission() {
        Permission permission = newPermissionWithId();
        given(permissionRepository.findById(permission.getId())).willReturn(Optional.of(permission));

        Permission fetchedPermission = permissionService.findById(permission.getId());
        fetchedPermission
                .setName("category.create")
                .setDescription("Alterar Categoria");
        Permission updatedPermission = permissionService.update(fetchedPermission.getId(), fetchedPermission);

        assertThat(updatedPermission)
                .withFailMessage("Retorno da atualização não pode ser nulo").isNotNull();
        assertThat(updatedPermission.getId()).isEqualTo(fetchedPermission.getId());
        assertThat(updatedPermission.getName()).isEqualTo(fetchedPermission.getName());
        assertThat(updatedPermission.getDescription()).isEqualTo(fetchedPermission.getDescription());
        assertThat(updatedPermission.getCreatedAt()).isEqualTo(fetchedPermission.getCreatedAt());
        verify(permissionRepository, times(2)).findById(permission.getId());
    }

    @Test
    public void update_mustHaveTransactionalAnnotation() throws NoSuchMethodException {
        boolean isAnnotationPresent = PermissionService.class
                .getDeclaredMethod("update", Long.class, Permission.class)
                .isAnnotationPresent(Transactional.class);

        assertThat(isAnnotationPresent)
                .withFailMessage("O método deve ter anotação @Transactional")
                .isTrue();
    }

    @Test
    public void deleteById_shouldDeletePermission() {
        Permission permissionToDelete = newPermissionWithId();
        given(permissionRepository.findById(permissionToDelete.getId())).willReturn(Optional.of(permissionToDelete));

        permissionService.deleteById(permissionToDelete.getId());

        verify(permissionRepository, atLeastOnce()).deleteById(permissionToDelete.getId());
    }

    @Test
    public void deleteById_mustHaveTransactionalAnnotation() throws NoSuchMethodException {
        boolean isAnnotationPresent = PermissionService.class
                .getDeclaredMethod("deleteById", Long.class)
                .isAnnotationPresent(Transactional.class);

        assertThat(isAnnotationPresent)
                .withFailMessage("O método deve ter anotação @Transactional")
                .isTrue();
    }

    @Test
    public void class_mustHaveServiceAnnotation() {
        assertThat(PermissionService.class.isAnnotationPresent(Service.class))
                .withFailMessage("A classe deve ter a anotaçao @Service")
                .isTrue();
    }
}
