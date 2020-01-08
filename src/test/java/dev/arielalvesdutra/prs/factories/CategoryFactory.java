package dev.arielalvesdutra.prs.factories;

import dev.arielalvesdutra.prs.builders.CategoryBuilder;
import dev.arielalvesdutra.prs.entities.Category;

import java.time.Instant;

public class CategoryFactory {

    public static Category newCategory() {
        return new CategoryBuilder()
                .withName("Categoria")
                .withDescription("Descrição")
                .withCreatedAt(Instant.now())
                .build();
    }

    public static Category newCategoryWithId() {
        Category category = CategoryFactory.newCategory();
        category.setId(1L);
        return category;
    }
}
