package dev.arielalvesdutra.prs.controllers.dto;

import dev.arielalvesdutra.prs.entities.Category;
import dev.arielalvesdutra.prs.entities.Post;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdatePostDTO {

    @NotNull
    @Size(min = 2)
    private String title;
    @NotNull @Size(min = 2)
    private String subtitle;
    @NotNull @Size(min = 2)
    private String body;
    @NotNull
    private Long categoryId;

    public Post toPost() {
        return new Post()
                .setTitle(getTitle())
                .setSubtitle(getSubtitle())
                .setBody(getBody())
                .setCategory(new Category().setId(getCategoryId()));
    }

}