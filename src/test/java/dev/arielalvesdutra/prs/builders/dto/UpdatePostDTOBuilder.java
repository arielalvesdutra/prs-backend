package dev.arielalvesdutra.prs.builders.dto;

import dev.arielalvesdutra.prs.controllers.dto.UpdatePostDTO;

public class UpdatePostDTOBuilder {

    private UpdatePostDTO updateDto = new UpdatePostDTO();

    public UpdatePostDTOBuilder withTitle(String title) {
        updateDto.setTitle(title);
        return this;
    }

    public UpdatePostDTOBuilder withSubtitle(String subtitle) {
        updateDto.setSubtitle(subtitle);
        return this;
    }

    public UpdatePostDTOBuilder withBody(String body) {
        updateDto.setBody(body);
        return this;
    }

    public UpdatePostDTOBuilder withCategoryId(Long categoryId) {
        updateDto.setCategoryId(categoryId);
        return this;
    }

    public UpdatePostDTO build() {
        return updateDto;
    }
}
