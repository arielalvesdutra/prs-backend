package dev.arielalvesdutra.prs.unit.entities;

import static dev.arielalvesdutra.prs.factories.PostFactory.newPostWithId;
import static org.assertj.core.api.Assertions.assertThat;

import dev.arielalvesdutra.prs.entities.Category;
import dev.arielalvesdutra.prs.entities.Post;
import dev.arielalvesdutra.prs.factories.PostFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.Entity;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
public class CategoryTest {

    @Test
    public void createCategory_shouldWork() {
        Long id = 1L;
        String name = "Categoria Z";
        String description = "Descrição da categoria";
        Instant createdAt = Instant.now();

        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDescription(description);
        category.setCreatedAt(createdAt);

        assertThat(category).isNotNull();
        assertThat(category.getId()).isEqualTo(id);
        assertThat(category.getName()).isEqualTo(name);
        assertThat(category.getDescription()).isEqualTo(description);
        assertThat(category.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    public void setAndGetPosts_shouldWork() {
        Post post = newPostWithId();
        Set<Post> posts = new HashSet<>();
        posts.add(post);
        Category category = new Category();

        category.setPosts(posts);

        assertThat(category.getPosts()).isEqualTo(posts);
    }

    @Test
    public void mustHaveEntityAnnotation() {
        assertThat(Category.class.isAnnotationPresent(Entity.class)).isTrue();
    }

    @Test
    public void equals_shouldBeById() {
        Long id = 1L;
        Category category1 = new Category();
        Category category2 = new Category();

        category1.setId(id);
        category2.setId(id);

        assertThat(category1).isEqualTo(category2);
    }
}
