package dev.arielalvesdutra.prs.factories;

import dev.arielalvesdutra.prs.builders.CategoryBuilder;
import dev.arielalvesdutra.prs.builders.UserBuilder;
import dev.arielalvesdutra.prs.entities.Category;
import dev.arielalvesdutra.prs.entities.User;

import java.time.Instant;

public class UserFactory {

    public static User newUser() {
        return new UserBuilder()
                .withName("Michael Scott")
                .withEmail("michael@dundermifflin.com")
                .withPassword("password")
                .build();
    }

    public static User newUserWithId() {
        User user = newUser();
        user.setId(1L);
        return user;
    }
}
