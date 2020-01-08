package dev.arielalvesdutra.prs.it.controllers;

import dev.arielalvesdutra.prs.builders.PostBuilder;
import dev.arielalvesdutra.prs.builders.dto.CreatePostDTOBuilder;
import dev.arielalvesdutra.prs.builders.dto.UpdatePostDTOBuilder;
import dev.arielalvesdutra.prs.controllers.dto.CreatePostDTO;
import dev.arielalvesdutra.prs.controllers.dto.RetrievePostDTO;
import dev.arielalvesdutra.prs.controllers.dto.UpdatePostDTO;
import dev.arielalvesdutra.prs.entities.*;
import dev.arielalvesdutra.prs.repositories.*;
import dev.arielalvesdutra.prs.services.*;
import org.junit.After;
import org.junit.Before;
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

import java.util.*;

import static dev.arielalvesdutra.prs.factories.CategoryFactory.newCategory;
import static dev.arielalvesdutra.prs.factories.RoleFactory.newRole;
import static dev.arielalvesdutra.prs.factories.UserFactory.newUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@ActiveProfiles("it")
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class PostControllerIT {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PostService postService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private User userForAuth;

    private Role userRole;

    @Before
    public void setUp() {
        userForAuth = userService.create(newUser());
        userRole = roleService.create(newRole());
        userForAuth.addRole(userRole);
        userService.updateRoles(userForAuth.getId(), userForAuth.getRoles());
    }

    @After
    public void tearDown() {
        postRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        permissionRepository.deleteAll();
    }

    @Test
    public void createPost_withCreatePostPermission_shouldReturnCreatedPostWithStatus201() {
        Category category = createCategory();
        CreatePostDTO createDto = new CreatePostDTOBuilder()
                .withTitle("Post")
                .withSubtitle("Subtítulo")
                .withBody("Corpo")
                .withCategoryId(category.getId())
                .build();
        createPermissionAndAddToUserRole("posts.create");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", generateToken());
        HttpEntity<CreatePostDTO> httpEntity = new HttpEntity<>(createDto, headers);

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
    public void retrieveAllPosts_withReadPostsPermission_andWithPagination_shouldWork() {
        Post createdPost = createPostWithCategory();
        RetrievePostDTO expectedPost = new RetrievePostDTO(createdPost);
        createPermissionAndAddToUserRole("posts.read");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", generateToken());
        HttpEntity<?> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<PagedModel<RetrievePostDTO>> response = restTemplate.exchange(
                "/posts",
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<PagedModel<RetrievePostDTO>>() {});
        PagedModel<RetrievePostDTO> resources = response.getBody();
        List<RetrievePostDTO> posts = new ArrayList<>(resources.getContent());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(posts).size().isEqualTo(1);
        assertThat(posts).contains(expectedPost);
    }

    @Test
    public void retrievePostById_withReadPostsPermission_shouldReturnPost() {
        Post createdPost = createPostWithCategory();
        RetrievePostDTO expectedPost = new RetrievePostDTO(createdPost);
        createPermissionAndAddToUserRole("posts.read");
        String url = "/posts/" + expectedPost.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", generateToken());
        HttpEntity<?> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<RetrievePostDTO> response =
                restTemplate.exchange(url,
                        HttpMethod.GET,
                        httpEntity,
                        RetrievePostDTO.class);
        RetrievePostDTO retrievedPostDto = response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(retrievedPostDto).isEqualTo(expectedPost);
    }

    @Test
    public void deletePostById_withDeletePostsPermission_shouldWork() {
        Post createdPost = createPostWithCategory();
        String url = "/posts/" + createdPost.getId();
        createPermissionAndAddToUserRole("posts.delete");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", generateToken());
        HttpEntity<?> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                httpEntity,
                String.class);
        Optional<Post> fetchedPost = postRepository.findById(createdPost.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(fetchedPost.isPresent()).isFalse();
    }

    @Test
    public void updatePostById_withUpdatePostsPermission_shouldWork() {
        Post createdPost = createPostWithCategory();
        Category newCategory = createCategory();
        String url = "/posts/" + createdPost.getId();
        createPermissionAndAddToUserRole("posts.update");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", generateToken());
        UpdatePostDTO updateDto = new UpdatePostDTOBuilder()
                .withTitle("Post")
                .withSubtitle("Subtítulo")
                .withBody("Corpo")
                .withCategoryId(newCategory.getId())
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
        assertThat(responsePost.getCategory().getId()).isEqualTo(newCategory.getId());
        assertThat(responsePost.getCreatedAt()).isEqualTo(createdPost.getCreatedAt());
    }

    private Post createPostWithCategory() {
        Post post = new PostBuilder()
                .withTitle("Post")
                .withSubtitle("Subtítulo")
                .withBody("Corpo")
                .withCategory(createCategory())
                .build();

        return postService.create(post);
    }

    private Category createCategory() {
        return categoryService.create(newCategory());
    }

    private String generateToken() {
        return "Bearer " + tokenService.generateToken(userForAuth);
    }

    private Permission createPermissionAndAddToUserRole(String permissionName) {
        Permission permission = permissionService.create(new Permission(permissionName));
        userRole.setPermissions(new HashSet<>(Arrays.asList(permission)));
        roleService.updatePermissions(userRole.getId(), userRole.getPermissions());
        return  permission;
    }
}
