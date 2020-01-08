package dev.arielalvesdutra.prs.it.controllers;

import dev.arielalvesdutra.prs.builders.CategoryBuilder;
import dev.arielalvesdutra.prs.builders.dto.CreateCategoryDTOBuilder;
import dev.arielalvesdutra.prs.builders.dto.UpdateCategoryDTOBuilder;
import dev.arielalvesdutra.prs.controllers.dto.CreateCategoryDTO;
import dev.arielalvesdutra.prs.controllers.dto.RetrieveCategoryDTO;
import dev.arielalvesdutra.prs.controllers.dto.UpdateCategoryDTO;
import dev.arielalvesdutra.prs.entities.Category;
import dev.arielalvesdutra.prs.repositories.CategoryRepository;
import dev.arielalvesdutra.prs.services.CategoryService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@ActiveProfiles("it")
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CategoryControllerIT {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @After
    public void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    public void createCategory_shouldReturnCreatedCategoryWithStatus201() {
        CreateCategoryDTO createDto = new CreateCategoryDTOBuilder()
                .withName("Categoria")
                .withDescription("Descrição")
                .build();
        HttpEntity<CreateCategoryDTO> httpEntity = new HttpEntity<CreateCategoryDTO>(createDto, null);

        ResponseEntity<RetrieveCategoryDTO> response = this.restTemplate.exchange(
                "/categories",
                HttpMethod.POST,
                httpEntity,
                RetrieveCategoryDTO.class);
        RetrieveCategoryDTO retrieveDto = response.getBody();

        assertThat(response)
                .describedAs("Retorno da requisição não pode ser nulo").isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(retrieveDto.getId()).isNotNull();
        assertThat(retrieveDto.getCreatedAt()).isNotNull();
        assertThat(retrieveDto.getName()).isEqualTo(createDto.getName());
        assertThat(retrieveDto.getDescription()).isEqualTo(createDto.getDescription());
    }

    @Test
    public void retrieveAllCategories_withPagination_shouldWork() {
        Category createdCategory = buildAndSaveASimpleCategory();
        RetrieveCategoryDTO expectedCategory = new RetrieveCategoryDTO(createdCategory);

        ResponseEntity<PagedModel<RetrieveCategoryDTO>> response = restTemplate.exchange(
                "/categories",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedModel<RetrieveCategoryDTO>>() {});
        PagedModel<RetrieveCategoryDTO> resources = response.getBody();
        List<RetrieveCategoryDTO> categories = new ArrayList<>(resources.getContent());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(categories).size().isEqualTo(1);
        assertThat(categories).contains(expectedCategory);
    }

    @Test
    public void retrieveCategoryById_shouldReturnCategory() {
        Category createdCategory = buildAndSaveASimpleCategory();
        RetrieveCategoryDTO expectedCategory = new RetrieveCategoryDTO(createdCategory);
        String url = "/categories/" + expectedCategory.getId();

        ResponseEntity<RetrieveCategoryDTO> response =
                restTemplate.getForEntity(url, RetrieveCategoryDTO.class);
        RetrieveCategoryDTO retrievedCategoryDto = response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(retrievedCategoryDto).isEqualTo(expectedCategory);
    }

    @Test
    public void deleteCategoryById_shouldWork() {
        Category createdCategory = buildAndSaveASimpleCategory();
        String url = "/categories/" + createdCategory.getId();

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                String.class);
        Optional<Category> fetchedCategory = categoryRepository.findById(createdCategory.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(fetchedCategory.isPresent()).isFalse();
    }

    @Test
    public void updateCategoryById_shouldWork() {
        Category createdCategory = buildAndSaveASimpleCategory();
        String url = "/categories/" + createdCategory.getId();
        HttpHeaders headers = new HttpHeaders();
        UpdateCategoryDTO updateDto = new UpdateCategoryDTOBuilder()
                .withName("Categoria atualizada")
                .withDescription("Descrição atualizada")
                .build();
        HttpEntity<UpdateCategoryDTO> httpEntity = new HttpEntity<>(updateDto, headers);

        ResponseEntity<RetrieveCategoryDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpEntity,
                RetrieveCategoryDTO.class);
        RetrieveCategoryDTO responseCategory = response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseCategory).isNotNull();
        assertThat(responseCategory.getName()).isEqualTo(updateDto.getName());
        assertThat(responseCategory.getDescription()).isEqualTo(updateDto.getDescription());
        assertThat(responseCategory.getId()).isEqualTo(createdCategory.getId());
        assertThat(responseCategory.getCreatedAt().equals(createdCategory.getCreatedAt())).isTrue();
    }

    private Category buildAndSaveASimpleCategory() {
        Category category = new CategoryBuilder()
                .withName("Categoria")
                .withDescription("Descrição")
                .build();

        return categoryService.create(category);
    }
}
