package dev.arielalvesdutra.prs.controllers.dto;

import dev.arielalvesdutra.prs.entities.Category;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateCategoryDTO {

    @NotNull
    @Size(min = 2, max = 255)
    private String name;

    @NotNull @Size(min = 2, max = 255)
    private String description;

    public Category toCategory() {
        return new Category().setName(getName()).setDescription(getDescription());
    }
}
