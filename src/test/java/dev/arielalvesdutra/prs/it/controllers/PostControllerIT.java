package dev.arielalvesdutra.prs.it.controllers;

import dev.arielalvesdutra.prs.builders.CategoryBuilder;
import dev.arielalvesdutra.prs.builders.PostBuilder;
import dev.arielalvesdutra.prs.builders.dto.CreatePostDTOBuilder;
import dev.arielalvesdutra.prs.builders.dto.UpdatePostDTOBuilder;
import dev.arielalvesdutra.prs.controllers.dto.CreatePostDTO;
import dev.arielalvesdutra.prs.controllers.dto.RetrievePostDTO;
import dev.arielalvesdutra.prs.controllers.dto.UpdatePostDTO;
import dev.arielalvesdutra.prs.entities.Category;
import dev.arielalvesdutra.prs.entities.Post;
import dev.arielalvesdutra.prs.repositories.CategoryRepository;
import dev.arielalvesdutra.prs.repositories.PostRepository;
import dev.arielalvesdutra.prs.services.CategoryService;
import dev.arielalvesdutra.prs.services.PostService;
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
public class PostControllerIT {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @After
    public void tearDown() {
        postRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void createPost_shouldReturnCreatedPostWithStatus201() {
        Category category = buildAndSaveASimpleCategory();
        CreatePostDTO createDto = new CreatePostDTOBuilder()
                .withTitle("Post")
                .withSubtitle("Subtítulo")
                .withBody("Corpo")
                .withCategoryId(category.getId())
                .build();
        HttpEntity<CreatePostDTO> httpEntity = new HttpEntity<>(createDto, null);

        ResponseEntity<RetrievePostDTO> response = this.restTemplate.exchange(
                "/posts",
                HttpMethod.POST,
                httpEntity,
                RetrievePostDTO.class);
        RetrievePostDTO retrieveDto = response.getBody();

        assertThat(response)
                .describedAs("Retorno da requisição não pode ser nulo").isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(retrieveDto).isNotNull();
        assertThat(retrieveDto.getId()).isNotNull();
        assertThat(retrieveDto.getCreatedAt()).isNotNull();
        assertThat(retrieveDto.getTitle()).isEqualTo(createDto.getTitle());
        assertThat(retrieveDto.getSubtitle()).isEqualTo(createDto.getSubtitle());
        assertThat(retrieveDto.getBody()).isEqualTo(createDto.getBody());
        assertThat(retrieveDto.getCategory().getId()).isEqualTo(createDto.getCategoryId());
    }

    @Test
    public void retrieveAllPosts_withPagination_shouldWork() {
        Post createdPost = buildAndSaveASimplePost();
        RetrievePostDTO expectedPost = new RetrievePostDTO(createdPost);

        ResponseEntity<PagedModel<RetrievePostDTO>> response = restTemplate.exchange(
                "/posts",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedModel<RetrievePostDTO>>() {});
        PagedModel<RetrievePostDTO> resources = response.getBody();
        List<RetrievePostDTO> posts = new ArrayList<>(resources.getContent());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(posts).size().isEqualTo(1);
        assertThat(posts).contains(expectedPost);
    }

    @Test
    public void retrievePostById_shouldReturnPost() {
        Post createdPost = buildAndSaveASimplePost();
        RetrievePostDTO expectedPost = new RetrievePostDTO(createdPost);
        String url = "/posts/" + expectedPost.getId();

        ResponseEntity<RetrievePostDTO> response =
                restTemplate.getForEntity(url, RetrievePostDTO.class);
        RetrievePostDTO retrievedPostDto = response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(retrievedPostDto).isEqualTo(expectedPost);
    }

    @Test
    public void deletePostById_shouldWork() {
        Post createdPost = buildAndSaveASimplePost();
        String url = "/posts/" + createdPost.getId();

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                String.class);
        Optional<Post> fetchedPost = postRepository.findById(createdPost.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(fetchedPost.isPresent()).isFalse();
    }

    @Test
    public void updatePostById_shouldWork() {
        Post createdPost = buildAndSaveASimplePost();
        Category category = buildAndSaveASimpleCategory();
        String url = "/posts/" + createdPost.getId();
        HttpHeaders headers = new HttpHeaders();
        UpdatePostDTO updateDto = new UpdatePostDTOBuilder()
                .withTitle("Post")
                .withSubtitle("Subtítulo")
                .withBody("Corpo")
                .withCategoryId(category.getId())
                .build();
        HttpEntity<UpdatePostDTO> httpEntity = new HttpEntity<>(updateDto, headers);

        ResponseEntity<RetrievePostDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                httpEntity,
                RetrievePostDTO.class);
        RetrievePostDTO responsePost = response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(responsePost).isNotNull();
        assertThat(responsePost.getTitle()).isEqualTo(updateDto.getTitle());
        assertThat(responsePost.getSubtitle()).isEqualTo(updateDto.getSubtitle());
        assertThat(responsePost.getBody()).isEqualTo(updateDto.getBody());
        assertThat(responsePost.getCategory().getId()).isEqualTo(category.getId());
        assertThat(responsePost.getCreatedAt()).isEqualTo(createdPost.getCreatedAt());
    }

    private Post buildAndSaveASimplePost() {
        Category category = buildAndSaveASimpleCategory();
        Post post = new PostBuilder()
                .withTitle("Post")
                .withSubtitle("Subtítulo")
                .withBody("Corpo")
                .withCategory(category)
                .build();

        return postService.create(post);
    }

    private Category buildAndSaveASimpleCategory() {
        Category category = new CategoryBuilder()
                .withName("Categoria")
                .withDescription("Descrição")
                .build();

        return categoryService.create(category);
    }
}
