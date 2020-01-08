package dev.arielalvesdutra.prs.controllers;

import dev.arielalvesdutra.prs.controllers.dto.CreateCategoryDTO;
import dev.arielalvesdutra.prs.controllers.dto.RetrieveCategoryDTO;
import dev.arielalvesdutra.prs.controllers.dto.UpdateCategoryDTO;
import dev.arielalvesdutra.prs.entities.Category;
import dev.arielalvesdutra.prs.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RequestMapping("/categories")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RetrieveCategoryDTO> create(
            @Valid @RequestBody CreateCategoryDTO createDto, UriComponentsBuilder uriBuilder) {

        Category createdCategory = categoryService.create(createDto.toCategory());
        URI uri = uriBuilder.path("/categories/{id}")
                .buildAndExpand(createdCategory.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new RetrieveCategoryDTO(createdCategory));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<RetrieveCategoryDTO>> retrieveAll(
            @PageableDefault(sort="name", page = 0, size = 10) Pageable pagination) {

        Page<Category> categoriesPage = categoryService.findAll(pagination);

        return ResponseEntity.ok().body(RetrieveCategoryDTO.toPage(categoriesPage));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{categoryId}")
    public ResponseEntity<RetrieveCategoryDTO> retrieveById(@PathVariable Long categoryId) {

        Category category = categoryService.findById(categoryId);

        return ResponseEntity.ok().body(new RetrieveCategoryDTO(category));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{categoryId}")
    public ResponseEntity<?> deleteById(@PathVariable Long categoryId) {

        categoryService.deleteById(categoryId);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{categoryId}")
    public ResponseEntity<RetrieveCategoryDTO> updateById(
            @PathVariable Long categoryId,
            @Valid @RequestBody UpdateCategoryDTO updateDto) {

        Category updatedCategory = categoryService.update(categoryId, updateDto.toCategory());

        return ResponseEntity.ok().body(new RetrieveCategoryDTO(updatedCategory));
    }
}
