package dev.arielalvesdutra.prs.unit.services;

import dev.arielalvesdutra.prs.entities.Permission;
import dev.arielalvesdutra.prs.entities.Role;
import dev.arielalvesdutra.prs.entities.User;
import dev.arielalvesdutra.prs.repositories.UserRepository;
import dev.arielalvesdutra.prs.services.RoleService;
import dev.arielalvesdutra.prs.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.*;

import static dev.arielalvesdutra.prs.AssertionErrorMessages.*;
import static dev.arielalvesdutra.prs.factories.PermissionFactory.newPermissionWithId;
import static dev.arielalvesdutra.prs.factories.RoleFactory.newRoleWithId;
import static dev.arielalvesdutra.prs.factories.UserFactory.newUser;
import static dev.arielalvesdutra.prs.factories.UserFactory.newUserWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserService(userRepository, roleService);
    }

    @Test
    public void class_mustHaveServiceAnnotation() {
        assertThat(UserService.class.isAnnotationPresent(Service.class))
                .withFailMessage("A classe deve ter a anotaçao @Service")
                .isTrue();
    }

    @Test
    public void create_shouldWork() {
        User userToCreate = newUser();
        User expectedUser = newUserWithId();
        given((userRepository.save(userToCreate))).willReturn(expectedUser);

        User createdUser = userService.create(userToCreate);

        assertThat(createdUser)
                .withFailMessage("Retorno do serviço não pode ser nulo.")
                .isNotNull();
        assertThat(expectedUser.getId()).isEqualTo(createdUser.getId());
        assertThat(expectedUser.getName()).isEqualTo(createdUser.getName());
        assertThat(expectedUser.getEmail()).isEqualTo(createdUser.getEmail());
        assertThat(expectedUser.getPassword()).isEqualTo(createdUser.getPassword());
    }


    @Test
    public void findById_shouldReturnUser() {
        User expectedUser = newUserWithId();
        given(userRepository.findById(expectedUser.getId()))
                .willReturn(Optional.of(expectedUser));

        User fetchedUser = userService.findById(expectedUser.getId());

        assertThat(fetchedUser).withFailMessage(NOT_NULL_RETURN).isNotNull();
        assertThat(expectedUser.getId()).isEqualTo(fetchedUser.getId());
        assertThat(expectedUser.getName()).isEqualTo(fetchedUser.getName());
        assertThat(expectedUser.getEmail()).isEqualTo(fetchedUser.getEmail());
        assertThat(expectedUser.getPassword()).isEqualTo(fetchedUser.getPassword());
    }

    @Test
    public void findAll_shouldReturnUserList() {
        User expectedUser = newUserWithId();
        given(userRepository.findAll()).willReturn(Arrays.asList(expectedUser));

        List<User> fetchedUser = userService.findAll();

        assertThat(fetchedUser).withFailMessage(NOT_NULL_RETURN).isNotNull();
        assertThat(fetchedUser).contains(expectedUser);
    }

    @Test
    public void findAll_withPageable_shouldReturnUserPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        User expectedUser = newUserWithId();
        List<User> expectedList = Arrays.asList(expectedUser);
        Page<User> userPage = new PageImpl<>(expectedList, pageable, 10);
        given(userRepository.findAll(pageable)).willReturn(userPage);

        Page<User> fetchedUserPage = userService.findAll(pageable);
        if (fetchedUserPage == null) fail(NOT_NULL_PAGINATION_RETURN);
        List<User> userList = fetchedUserPage.getContent();

        assertThat(userList).withFailMessage("Lista de papéis não pode ser nula").isNotNull();
        assertThat(userList).contains(expectedUser);
    }

    @Test
    public void update_shouldReturnUpdatedUser() {
        User user = newUserWithId();
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        User fetchedUser = userService.findById(user.getId());
        fetchedUser
                .setName("Jim Halpert")
                .setEmail("jim@dundermifflin.com")
                .setPassword("pam");
        User updatedUser = userService.update(fetchedUser.getId(), fetchedUser);

        assertThat(updatedUser)
                .withFailMessage("Retorno da atualização não pode ser nulo").isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(fetchedUser.getId());
        assertThat(updatedUser.getName()).isEqualTo(fetchedUser.getName());
        assertThat(updatedUser.getEmail()).isEqualTo(fetchedUser.getEmail());
        assertThat(updatedUser.getPassword()).isEqualTo(fetchedUser.getPassword());
        verify(userRepository, times(2)).findById(user.getId());
    }

    @Test
    public void update_mustHaveTransactionalAnnotation() throws NoSuchMethodException {
        boolean isAnnotationPresent = UserService.class
                .getDeclaredMethod("update", Long.class, User.class)
                .isAnnotationPresent(Transactional.class);

        assertThat(isAnnotationPresent)
                .withFailMessage(METHOD_MUST_HAVE_TRANSACTION_ANNOTATION)
                .isTrue();
    }

    @Test
    public void deleteById_shouldDeleteUser() {
        User userToDelete = newUserWithId();
        given(userRepository.findById(userToDelete.getId())).willReturn(Optional.of(userToDelete));

        userService.deleteById(userToDelete.getId());

        verify(userRepository, atLeastOnce()).deleteById(userToDelete.getId());
    }

    @Test
    public void deleteById_mustHaveTransactionalAnnotation() throws NoSuchMethodException {
        boolean isAnnotationPresent = UserService.class
                .getDeclaredMethod("deleteById", Long.class)
                .isAnnotationPresent(Transactional.class);

        assertThat(isAnnotationPresent)
                .withFailMessage(METHOD_MUST_HAVE_TRANSACTION_ANNOTATION)
                .isTrue();
    }

    @Test
    public void updateUserRoles_shouldWork() {
        User user = newUserWithId();
        Role role = newRoleWithId();
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        given(userRepository.findById(role.getId())).willReturn(Optional.of(user));

        Set<Role> updatedUserRolesSet = userService.updateRoles(role.getId(), roleSet);

        assertThat(updatedUserRolesSet).withFailMessage(NOT_NULL_RETURN).isNotNull();
        assertThat(updatedUserRolesSet).contains(role);
        verify(userRepository, atLeastOnce()).findById(role.getId());
    }

    @Test
    public void updateUserRoles_mustHaveTransactionalAnnotation() throws NoSuchMethodException {
        boolean isAnnotationPresent = UserService.class
                .getDeclaredMethod("updateRoles", Long.class, Set.class)
                .isAnnotationPresent(Transactional.class);

        assertThat(isAnnotationPresent)
                .withFailMessage(METHOD_MUST_HAVE_TRANSACTION_ANNOTATION)
                .isTrue();
    }

    @Test
    public void findAllUserRoles_shouldReturnRolesList(){
        User user = newUserWithId();
        Role role = newRoleWithId();
        List<Role> roleList = Arrays.asList(role);
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(roleService.findAllByUserId(role.getId())).willReturn(roleList);

        List<Role> fetchedRolesList = userService.findAllRoles(user.getId());

        assertThat(fetchedRolesList).withFailMessage(NOT_NULL_RETURN).isNotNull();
        assertThat(fetchedRolesList).contains(role);
        verify(userRepository, atLeastOnce()).findById(user.getId());
        verify(roleService, atLeastOnce()).findAllByUserId(user.getId());
    }

    @Test
    public void findAllUserRoles_withPageable_shouldReturnRolesPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        User user = newUserWithId();
        Role role = newRoleWithId();
        List<Role> roleList = Arrays.asList(role);
        Page<Role> rolePage = new PageImpl<>(roleList, pageable, 10);
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(roleService.findAllByUserId(role.getId(), pageable)).willReturn(rolePage);

        Page<Role> responseRolesPage = userService.findAllRoles(role.getId(), pageable);
        if (responseRolesPage == null) fail(NOT_NULL_RETURN);
        List<Role> responseRolesList = responseRolesPage.getContent();

        assertThat(responseRolesList).withFailMessage(NOT_NULL_RETURN).isNotNull();
        assertThat(responseRolesList).contains(role);
        verify(userRepository, atLeastOnce()).findById(user.getId());
        verify(roleService, atLeastOnce()).findAllByUserId(user.getId(), pageable);
    }
}
