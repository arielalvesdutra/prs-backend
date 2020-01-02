package dev.arielalvesdutra.prs.unit.entities;

import dev.arielalvesdutra.prs.entities.Permission;
import dev.arielalvesdutra.prs.entities.Role;
import dev.arielalvesdutra.prs.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static dev.arielalvesdutra.prs.factories.PermissionFactory.newPermissionWithId;
import static dev.arielalvesdutra.prs.factories.UserFactory.newUserWithId;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class RoleTest {

    @Test
    public void createRole_shouldWork() {
        Long id = 1L;
        String name = "TI";
        String description = "Descrição do papél";
        Instant createdAt = Instant.now();

        Role role = new Role();
        role.setId(id);
        role.setName(name);
        role.setDescription(description);
        role.setCreatedAt(createdAt);

        assertThat(role).isNotNull();
        assertThat(role.getId()).isEqualTo(id);
        assertThat(role.getDescription()).isEqualTo(description);
        assertThat(role.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    public void setAndGetPermissions_shouldWork() {
        Set<Permission> permissions = buildPermissionsSet();
        Role role = new Role();

        role.setPermissions(permissions);

        assertThat(role.getPermissions()).isEqualTo(permissions);
    }

    @Test
    public void setAndGetUsers_shouldWork() {
        Set<User> userSet = buildUserSet();
        Role role = new Role();

        role.setUsers(userSet);

        assertThat(role.getUsers()).isEqualTo(userSet);
    }

    @Test
    public void users_mustHaveManyToManyAnnotation_mappedByRoles() throws NoSuchFieldException {
        ManyToMany manyToMany = Role.class
                .getDeclaredField("users")
                .getAnnotation(ManyToMany.class);

        assertThat(manyToMany.mappedBy()).isEqualTo("roles");
    }

    @Test
    public void mustHaveEntityAnnotation() {
        assertThat(Role.class.isAnnotationPresent(Entity.class)).isTrue();
    }

    @Test
    public void equals_shouldBeById() {
        Long id = 1L;
        Role role1 = new Role();
        Role role2 = new Role();

        role1.setId(id);
        role2.setId(id);

        assertThat(role1).isEqualTo(role2);
    }

    @Test
    public void permissions_mustHaveManyToManyAnnotation() throws NoSuchFieldException {
        boolean isAnnotationPresent = Role.class
                .getDeclaredField("permissions")
                .isAnnotationPresent(ManyToMany.class);

        assertThat(isAnnotationPresent).isTrue();
    }

    @Test
    public void permissions_mustHaveJoinTableAnnotation_withCurrentConfiguration()
            throws NoSuchFieldException, SecurityException {

        JoinTable joinTable = Role.class
                .getDeclaredField("permissions")
                .getAnnotation(JoinTable.class);

        JoinColumn inverseJoinColumn = joinTable.inverseJoinColumns()[0];
        JoinColumn joinColumn = joinTable.joinColumns()[0];

        assertThat(joinTable.name()).isEqualTo("role_permission");
        assertThat(joinColumn.name()).isEqualTo("role_id");
        assertThat(joinColumn.referencedColumnName()).isEqualTo("id");
        assertThat(inverseJoinColumn.name()).isEqualTo("permission_id");
        assertThat(inverseJoinColumn.referencedColumnName()).isEqualTo("id");
    }

    @Test
    public void name_mustHaveColumnAnnotation_withUniqueTrue() throws NoSuchFieldException {
        Column column = Role.class
                .getDeclaredField("name")
                .getAnnotation(Column.class);

        assertThat(column).isNotNull();
        assertThat(column.unique()).isTrue();
    }

    public Set<Permission> buildPermissionsSet() {
        Permission permission = newPermissionWithId();
        Set<Permission> permissions = new HashSet<>();
        permissions.add(permission);
        return permissions;
    }

    private Set<User> buildUserSet() {
        User user = newUserWithId();
        Set<User> userSet = new HashSet<>();
        userSet.add(user);
        return userSet;
    }
}
