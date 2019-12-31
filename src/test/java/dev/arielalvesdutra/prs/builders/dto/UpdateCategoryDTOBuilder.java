package dev.arielalvesdutra.prs.builders.dto;

import dev.arielalvesdutra.prs.controllers.dto.UpdateCategoryDTO;

public class UpdateCategoryDTOBuilder {

    UpdateCategoryDTO updateDto = new UpdateCategoryDTO();

    public  UpdateCategoryDTOBuilder withName(String name) {
        updateDto.setName(name);
        return this;
    }

    public  UpdateCategoryDTOBuilder withDescription(String description) {
        updateDto.setDescription(description);
        return this;
    }

    public UpdateCategoryDTO build() {
        return updateDto;
    }
}
