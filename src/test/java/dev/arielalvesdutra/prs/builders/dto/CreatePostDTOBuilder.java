package dev.arielalvesdutra.prs.builders.dto;

import dev.arielalvesdutra.prs.controllers.dto.CreatePostDTO;

public class CreatePostDTOBuilder {

    private CreatePostDTO createDto = new CreatePostDTO();

    public CreatePostDTOBuilder withTitle(String title) {
        createDto.setTitle(title);
        return this;
    }

    public CreatePostDTOBuilder withSubtitle(String subtitle) {
        createDto.setSubtitle(subtitle);
        return this;
    }

    public CreatePostDTOBuilder withBody(String body) {
        createDto.setBody(body);
        return this;
    }

    public CreatePostDTOBuilder withCategoryId(Long categoryId) {
        createDto.setCategoryId(categoryId);
        return this;
    }

    public CreatePostDTO build() {
        return createDto;
    }
}
