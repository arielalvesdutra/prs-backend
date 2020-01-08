package dev.arielalvesdutra.prs.builders;

import dev.arielalvesdutra.prs.entities.Permission;

import java.time.Instant;

public class PermissionBuilder {

    private Permission permission = new Permission();

    public PermissionBuilder withId(Long id) {
        permission.setId(id);
        return this;
    }

    public PermissionBuilder withName(String name) {
        permission.setName(name);
        return this;
    }

    public PermissionBuilder withDescription(String description) {
        permission.setDescription(description);
        return this;
    }

    public PermissionBuilder withCreatedAt(Instant createdAt) {
        permission.setCreatedAt(createdAt);
        return this;
    }

    public Permission build() {
        return permission;
    }
}
