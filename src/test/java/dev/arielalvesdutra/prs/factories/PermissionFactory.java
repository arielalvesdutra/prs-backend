package dev.arielalvesdutra.prs.factories;

import dev.arielalvesdutra.prs.builders.PermissionBuilder;
import dev.arielalvesdutra.prs.entities.Permission;

public class PermissionFactory {

    public static Permission newPermission() {
        return new PermissionBuilder()
                .withName("category.delete")
                .withDescription("Remover categoria")
                .build();
    }

    public static Permission newPermissionWithId() {
        Permission permission = PermissionFactory.newPermission();
        permission.setId(1L);
        return permission;
    }
}
