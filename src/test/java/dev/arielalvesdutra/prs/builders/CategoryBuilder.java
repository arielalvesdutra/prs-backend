package dev.arielalvesdutra.prs.builders;

import dev.arielalvesdutra.prs.entities.Category;

import java.time.Instant;

public class CategoryBuilder {

    private Category category = new Category();

    public CategoryBuilder withId(Long id) {
        category.setId(id);
        return this;
    }

    public CategoryBuilder withName(String name) {
        category.setName(name);
        return this;
    }

    public CategoryBuilder withDescription(String description) {
        category.setDescription(description);
        return this;
    }

    public CategoryBuilder withCreatedAt(Instant createdAt) {
        category.setCreatedAt(createdAt);
        return this;
    }

    public Category build() {
        return category;
    }
}
