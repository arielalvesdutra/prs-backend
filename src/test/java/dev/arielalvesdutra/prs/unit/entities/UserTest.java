package dev.arielalvesdutra.prs.unit.entities;

import dev.arielalvesdutra.prs.builders.PermissionBuilder;
import dev.arielalvesdutra.prs.entities.Permission;
import dev.arielalvesdutra.prs.entities.Role;
import dev.arielalvesdutra.prs.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

import static dev.arielalvesdutra.prs.factories.PermissionFactory.newPermissionWithId;
import static dev.arielalvesdutra.prs.factories.RoleFactory.newRoleWithId;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
public class UserTest {

    @Test
    public void createUser_shouldWork() {
        Long id = 1L;
        String name = "Usuário";
        String email = "email@email.com";
        String password = "password";

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
    }

    @Test
    public void setAndGetRoles_shouldWork() {
        User user = new User();
        Set<Role> roles = buildRoleSet();

        user.setRoles(roles);

        assertThat(user.getRoles()).isEqualTo(roles);
    }

    @Test
    public void getAuthorities_shouldReturnUserRolesPermissions() {
        Permission permission = newPermissionWithId();
        Set<Permission> permissions = buildPermissionSet();
        Set<Role> roles = buildRoleSetWithPermissionSet(permissions);
        User user = new User();
        GrantedAuthority expectedAuthority = new SimpleGrantedAuthority(permission.getName());

        user.setRoles(roles);

        assertThat(user.getAuthorities())
                .describedAs("Deve retornar conjunto de autoridades")
                .isNotNull();
        assertThat(user.getAuthorities().contains(expectedAuthority)).isTrue();
    }

    @Test
    public void username_shouldBeEqualToEmail() {
        String email = "teste@teste.com";
        User user = new User();

        user.setEmail(email);

        assertThat(user.getUsername()).isEqualTo(email);
    }

    @Test
    public void mustHaveEntityAnnotation() {
        assertThat(User.class.isAnnotationPresent(Entity.class)).isTrue();
    }

    @Test
    public void equals_shouldBeById() {
        Long id = 1L;
        User user1 = new User();
        User user2 = new User();

        user1.setId(id);
        user2.setId(id);

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    public void shouldImplementUserDetails() {
        assertThat(new User() instanceof UserDetails).isTrue();
    }

    @Test
    public void role_mustHaveManyToManyAnnotation() throws NoSuchFieldException {
        boolean isAnnotationPresent = User.class
                .getDeclaredField("roles")
                .isAnnotationPresent(ManyToMany.class);

        assertThat(isAnnotationPresent).isTrue();
    }

    @Test
    public void role_mustHaveJoinTableAnnotation_withCurrentConfiguration()
            throws NoSuchFieldException, SecurityException {

        JoinTable joinTable = User.class
                .getDeclaredField("roles")
                .getAnnotation(JoinTable.class);

        JoinColumn inverseJoinColumn = joinTable.inverseJoinColumns()[0];
        JoinColumn joinColumn = joinTable.joinColumns()[0];

        assertThat(joinTable.name()).isEqualTo("user_role");
        assertThat(joinColumn.name()).isEqualTo("user_id");
        assertThat(joinColumn.referencedColumnName()).isEqualTo("id");
        assertThat(inverseJoinColumn.name()).isEqualTo("role_id");
        assertThat(inverseJoinColumn.referencedColumnName()).isEqualTo("id");
    }

    private Set<Role> buildRoleSet() {
        Role role = newRoleWithId();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return roles;
    }

    private Set<Role> buildRoleSetWithPermissionSet(Set<Permission> permissions) {
        Role role = newRoleWithId();
        role.setPermissions(permissions);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return roles;
    }

    private Set<Permission> buildPermissionSet() {
        Permission permission = newPermissionWithId();
        Set<Permission> permissions = new HashSet<>();
        permissions.add(permission);
        return permissions;
    }
}
