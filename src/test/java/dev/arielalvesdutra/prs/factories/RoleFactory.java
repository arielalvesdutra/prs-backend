package dev.arielalvesdutra.prs.factories;

import dev.arielalvesdutra.prs.builders.RoleBuilder;
import dev.arielalvesdutra.prs.entities.Role;

public class RoleFactory {

    public static Role newRole() {
        return new RoleBuilder()
                .withName("Assistant to the Regional Manager")
                .withDescription("Assistant to the Regional Manager Role")
                .build();
    }

    public static Role newRoleWithId() {
        Role role = RoleFactory.newRole();
        role.setId(1L);
        return role;
    }
}
