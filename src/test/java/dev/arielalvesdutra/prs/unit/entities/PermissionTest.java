package dev.arielalvesdutra.prs.unit.entities;

import dev.arielalvesdutra.prs.entities.Permission;
import dev.arielalvesdutra.prs.entities.Role;
import dev.arielalvesdutra.prs.factories.RoleFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class PermissionTest {

    @Test
    public void createPermission_shouldWork() {
        Long id = 1L;
        String name = "post.create";
        String description = "Criar Post";
        Instant createdAt = Instant.now();

        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);
        permission.setDescription(description);
        permission.setCreatedAt(createdAt);

        assertThat(permission).isNotNull();
        assertThat(permission.getId()).isEqualTo(id);
        assertThat(permission.getName()).isEqualTo(name);
        assertThat(permission.getDescription()).isEqualTo(description);
        assertThat(permission.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    public void mustHaveEntityAnnotation() {
        assertThat(Permission.class.isAnnotationPresent(Entity.class)).isTrue();
    }

    @Test
    public void equals_shouldBeById() {
        Long id = 1L;
        Permission permission1 = new Permission();
        Permission permission2 = new Permission();

        permission1.setId(id);
        permission2.setId(id);

        assertThat(permission1).isEqualTo(permission2);
    }

    @Test
    public void name_mustHaveColumnAnnotation_withUniqueTrue() throws NoSuchFieldException {
        Column column = Permission.class
                .getDeclaredField("name")
                .getAnnotation(Column.class);

        assertThat(column.unique()).isTrue();
    }

    @Test
    public void setAndGetRoles_shouldWork() {
        Permission permission = new Permission();
        Role role = RoleFactory.newRoleWithId();
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        permission.setRoles(roleSet);

        assertThat(permission.getRoles()).isEqualTo(roleSet);
    }

    @Test
    public void roles_mustHaveManyToManyAnnotation() throws NoSuchFieldException {
        ManyToMany manyToMany = Permission.class
                .getDeclaredField("roles")
                .getAnnotation(ManyToMany.class);

        assertThat(manyToMany.mappedBy()).isEqualTo("permissions");
    }
}
