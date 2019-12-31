package dev.arielalvesdutra.prs.controllers.dto;

import dev.arielalvesdutra.prs.entities.Category;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.Instant;

@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class RetrieveCategoryDTO {

    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private Instant createdAt;

    public RetrieveCategoryDTO(Category category) {
        setId(category.getId());
        setName(category.getName());
        setDescription(category.getDescription());
        setCreatedAt(category.getCreatedAt());
    }

    public static Page<RetrieveCategoryDTO> toPage(Page<Category> categoriesPage) {
        return categoriesPage.map(RetrieveCategoryDTO::new);
    }
}
