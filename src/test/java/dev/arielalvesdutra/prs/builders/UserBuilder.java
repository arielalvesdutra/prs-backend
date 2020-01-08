package dev.arielalvesdutra.prs.builders;

import dev.arielalvesdutra.prs.entities.User;

public class UserBuilder {
    User user = new User();

    public UserBuilder withName(String name) {
        user.setName(name);
        return this;
    }

    public UserBuilder withEmail(String email) {
        user.setEmail(email);
        return this;
    }

    public UserBuilder withPassword(String password) {
        user.setPassword(password);
        return this;
    }

    public User build() {
        return user;
    }
}
