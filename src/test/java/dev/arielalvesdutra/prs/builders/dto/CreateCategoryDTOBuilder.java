package dev.arielalvesdutra.prs.builders.dto;

import dev.arielalvesdutra.prs.controllers.dto.CreateCategoryDTO;

public class CreateCategoryDTOBuilder {

    CreateCategoryDTO categoryDTO = new CreateCategoryDTO();

    public  CreateCategoryDTOBuilder withName(String name) {
        categoryDTO.setName(name);
        return this;
    }

    public  CreateCategoryDTOBuilder withDescription(String description) {
        categoryDTO.setDescription(description);
        return this;
    }

    public CreateCategoryDTO build() {
        return categoryDTO;
    }
}
