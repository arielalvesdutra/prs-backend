package dev.arielalvesdutra.prs.unit.services;

import dev.arielalvesdutra.prs.builders.CategoryBuilder;
import dev.arielalvesdutra.prs.entities.Category;
import dev.arielalvesdutra.prs.repositories.CategoryRepository;
import dev.arielalvesdutra.prs.services.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static dev.arielalvesdutra.prs.factories.CategoryFactory.newCategory;
import static dev.arielalvesdutra.prs.factories.CategoryFactory.newCategoryWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    @Before
    public void setUp() {
        categoryService = new CategoryService(this.categoryRepository);
    }

    @Test
    public void createCategory_shouldWork() {
        Category categoryToCreate = newCategory();
        Category expectedCategory = newCategoryWithId();
        given(this.categoryRepository.save(categoryToCreate)).willReturn(expectedCategory);

        Category createdCategory = this.categoryService.create(categoryToCreate);

        assertThat(createdCategory)
                .withFailMessage("O retorno do serviço não pode ser nulo.").isNotNull();
        assertThat(expectedCategory.getId()).isEqualTo(createdCategory.getId());
        assertThat(expectedCategory.getName()).isEqualTo(createdCategory.getName());
        assertThat(expectedCategory.getDescription()).isEqualTo(createdCategory.getDescription());
        assertThat(expectedCategory.getCreatedAt()).isEqualTo(createdCategory.getCreatedAt());
    }

    @Test
    public void findCategoryById_shouldReturnCategory() {
        Category expectedCategory = newCategoryWithId();
        Long categoryId = 1L;
        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(expectedCategory));

        Category fetchedCategory = categoryService.findById(categoryId);

        assertThat(fetchedCategory)
                .withFailMessage("O retorno do serviço não pode ser nulo.").isNotNull();
        assertThat(expectedCategory.getId()).isEqualTo(fetchedCategory.getId());
        assertThat(expectedCategory.getName()).isEqualTo(fetchedCategory.getName());
        assertThat(expectedCategory.getDescription()).isEqualTo(fetchedCategory.getDescription());
        assertThat(expectedCategory.getCreatedAt()).isEqualTo(fetchedCategory.getCreatedAt());
    }

    @Test
    public void findAllCategories_shouldReturnAllCategoriesList() {
        Category expectedCategory = newCategoryWithId();
        given(categoryRepository.findAll()).willReturn(Arrays.asList(expectedCategory));

        List<Category> fetchedCategories = categoryService.findAll();

        assertThat(fetchedCategories).isNotNull();
        assertThat(fetchedCategories).contains(expectedCategory);
    }

    @Test
    public void findAllCategories_withPageable_shouldReturnAllCategoriesPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Category expectedCategory = newCategoryWithId();
        List<Category> expectedList = Arrays.asList(expectedCategory);
        given(categoryRepository.findAll(pageable))
                .willReturn(new PageImpl<Category>(expectedList, pageable, 10));

        Page<Category> fetchedCategories = categoryService.findAll(pageable);
        List<Category> categoriesList = fetchedCategories.getContent();

        assertThat(fetchedCategories).describedAs("Retorno da paginação não pode ser nulo").isNotNull();
        assertThat(categoriesList).describedAs("Lista de categorias não pode ser nula").isNotNull();
        assertThat(categoriesList).contains(expectedCategory);
    }

    @Test
    public void updateCategory_shouldReturnUpdatedCategory() {
        Category category = newCategoryWithId();
        Long categoryId = 1L;
        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(category));

        Category fetchedCategory = categoryService.findById(categoryId);
        fetchedCategory
                .setName("Nome modificado")
                .setDescription("Descrição modificada");
        Category updatedCategory =
                    categoryService.update(fetchedCategory.getId(), fetchedCategory);

        assertThat(updatedCategory)
                .describedAs("Retorno da atualização não pode ser nulo").isNotNull();
        assertThat(updatedCategory.getId()).isEqualTo(fetchedCategory.getId());
        assertThat(updatedCategory.getName()).isEqualTo(fetchedCategory.getName());
        assertThat(updatedCategory.getDescription()).isEqualTo(fetchedCategory.getDescription());
        assertThat(updatedCategory.getCreatedAt()).isEqualTo(fetchedCategory.getCreatedAt());
    }

    @Test
    public void deleteCategoryById_shouldDeleteCategory() {
        Category categoryToDelete = newCategoryWithId();
        given(categoryRepository.findById(categoryToDelete.getId())).willReturn(Optional.of(categoryToDelete));

        categoryService.deleteById(categoryToDelete.getId());

        verify(categoryRepository, atLeastOnce()).deleteById(categoryToDelete.getId());
    }

    @Test
    public void mustHaveServiceAnnotation() {
        assertThat(CategoryService.class.isAnnotationPresent(Service.class)).isTrue();
    }
}
