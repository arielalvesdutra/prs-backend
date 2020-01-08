package dev.arielalvesdutra.prs.unit.services;

import dev.arielalvesdutra.prs.builders.CategoryBuilder;
import dev.arielalvesdutra.prs.builders.PostBuilder;
import dev.arielalvesdutra.prs.entities.Category;
import dev.arielalvesdutra.prs.entities.Post;
import dev.arielalvesdutra.prs.repositories.PostRepository;
import dev.arielalvesdutra.prs.services.CategoryService;
import dev.arielalvesdutra.prs.services.PostService;
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

import static dev.arielalvesdutra.prs.factories.CategoryFactory.newCategoryWithId;
import static dev.arielalvesdutra.prs.factories.PostFactory.newPost;
import static dev.arielalvesdutra.prs.factories.PostFactory.newPostWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CategoryService categoryService;

    private PostService postService;

    @Before
    public void setUp() {
        postService = new PostService(this.postRepository, this.categoryService);
    }

    @Test
    public void createPost_shouldWork() {
        Post postToCreate = newPost();
        Category category = newCategoryWithId();
        postToCreate.setCategory(category);
        Post expectedPost = newPostWithId();
        given(this.postRepository.save(postToCreate)).willReturn(expectedPost);

        Post createdPost = this.postService.create(postToCreate);

        assertThat(createdPost)
                .withFailMessage("O retorno do serviço não pode ser nulo.").isNotNull();
        assertThat(expectedPost.getId()).isEqualTo(createdPost.getId());
        assertThat(expectedPost.getTitle()).isEqualTo(createdPost.getTitle());
        assertThat(expectedPost.getSubtitle()).isEqualTo(createdPost.getSubtitle());
        assertThat(expectedPost.getBody()).isEqualTo(createdPost.getBody());
        assertThat(expectedPost.getCreatedAt()).isEqualTo(createdPost.getCreatedAt());
    }

    @Test
    public void findPostById_shouldReturnPost() {
        Post expectedPost = newPostWithId();
        Long postId = 1L;
        given(postRepository.findById(postId)).willReturn(Optional.of(expectedPost));

        Post fetchedPost = postService.findById(postId);

        assertThat(fetchedPost)
                .withFailMessage("O retorno do serviço não pode ser nulo.").isNotNull();
        assertThat(expectedPost.getId()).isEqualTo(fetchedPost.getId());
        assertThat(expectedPost.getTitle()).isEqualTo(fetchedPost.getTitle());
        assertThat(expectedPost.getSubtitle()).isEqualTo(fetchedPost.getSubtitle());
        assertThat(expectedPost.getBody()).isEqualTo(fetchedPost.getBody());
        assertThat(expectedPost.getCreatedAt()).isEqualTo(fetchedPost.getCreatedAt());
    }

    @Test
    public void findAllPosts_shouldReturnAllPostsOnList() {
        Post expectedPost = newPostWithId();
        given(postRepository.findAll()).willReturn(Arrays.asList(expectedPost));

        List<Post> fetchedPosts = postService.findAll();

        assertThat(fetchedPosts).isNotNull();
        assertThat(fetchedPosts).contains(expectedPost);
    }

    @Test
    public void findAllPosts_withPageable_shouldReturnAllPostsOnPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Post expectedPost = newPostWithId();
        List<Post> expectedList = Arrays.asList(expectedPost);
        given(postRepository.findAll(pageable))
                .willReturn(new PageImpl<Post>(expectedList, pageable, 10));

        Page<Post> fetchedPosts = postService.findAll(pageable);
        List<Post> postsList = fetchedPosts.getContent();

        assertThat(fetchedPosts).describedAs("Retorno da paginação não pode ser nulo").isNotNull();
        assertThat(postsList).describedAs("Lista de posts não pode ser nula").isNotNull();
        assertThat(postsList).contains(expectedPost);
    }

    @Test
    public void updatePost_shouldReturnUpdatedPost() {
        Post post = newPostWithId();
        Category category = newCategoryWithId();
        Long postId = 1L;
        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        Post fetchedPost = postService.findById(postId);
        fetchedPost
                .setTitle("Título modificado")
                .setSubtitle("Subtítulo modificado")
                .setBody("Corpo modificado")
                .setCategory(category);
        Post updatedPost = postService.update(fetchedPost.getId(), fetchedPost);

        assertThat(updatedPost)
                .describedAs("Retorno da atualização não pode ser nulo").isNotNull();
        assertThat(updatedPost.getId()).isEqualTo(fetchedPost.getId());
        assertThat(updatedPost.getTitle()).isEqualTo(fetchedPost.getTitle());
        assertThat(updatedPost.getSubtitle()).isEqualTo(fetchedPost.getSubtitle());
        assertThat(updatedPost.getBody()).isEqualTo(fetchedPost.getBody());
        assertThat(updatedPost.getCreatedAt()).isEqualTo(fetchedPost.getCreatedAt());
    }

    @Test
    public void deletePostById_shouldDeletePost() {
        Post postToDelete = newPostWithId();
        given(postRepository.findById(postToDelete.getId())).willReturn(Optional.of(postToDelete));

        postService.deleteById(postToDelete.getId());

        verify(postRepository, atLeastOnce()).deleteById(postToDelete.getId());
    }

    @Test
    public void mustHaveServiceAnnotation() {
        assertThat(PostService.class.isAnnotationPresent(Service.class)).isTrue();
    }

    private Category buildCategoryWithId() {
        return new CategoryBuilder()
                .withId(1L)
                .withName("Categoria")
                .withDescription("Descrição")
                .withCreatedAt(Instant.now())
                .build();
    }
}
