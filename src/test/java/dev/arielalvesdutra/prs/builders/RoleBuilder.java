package dev.arielalvesdutra.prs.builders;

import dev.arielalvesdutra.prs.entities.Role;

import java.time.Instant;

public class RoleBuilder {

    private Role role = new Role();

    public RoleBuilder withId(Long id) {
        role.setId(id);
        return this;
    }

    public RoleBuilder withName(String name) {
        role.setName(name);
        return this;
    }

    public RoleBuilder withDescription(String description) {
        role.setDescription(description);
        return this;
    }

    public RoleBuilder withCreatedAt(Instant createdAt) {
        role.setCreatedAt(createdAt);
        return this;
    }

    public Role build() {
        return role;
    }
}
