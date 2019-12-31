package dev.arielalvesdutra.prs.unit.entities;

import dev.arielalvesdutra.prs.entities.Category;
import dev.arielalvesdutra.prs.entities.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.Column;
import javax.persistence.Entity;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class PostTest {

    @Test
    public void createPost_shouldWork() {
        Long id = 1L;
        String title = "Post Z";
        String subtitle = "Subtitulo do post";
        String body = "Corpo do post";
        Instant createdAt = Instant.now();
        Category category = new Category();

        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        post.setSubtitle(subtitle);
        post.setBody(body);
        post.setCreatedAt(createdAt);
        post.setCategory(category);

        assertThat(post.getId()).isEqualTo(id);
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getSubtitle()).isEqualTo(subtitle);
        assertThat(post.getBody()).isEqualTo(body);
        assertThat(post.getCreatedAt()).isEqualTo(createdAt);
        assertThat(post.getCategory()).isEqualTo(category);
    }

    @Test
    public void mustHaveEntityAnnotation() {
        assertThat(Post.class.isAnnotationPresent(Entity.class)).isTrue();
    }

    @Test
    public void equals_shouldBeById() {
        Long id = 1L;
        Post post1 = new Post();
        Post post2 = new Post();

        post1.setId(id);
        post2.setId(id);

        assertThat(post1).isEqualTo(post2);
    }

    @Test
    public void body_mustHaveColumnAnnotationWithTextDefinition()
            throws NoSuchFieldException, SecurityException {
        Column column = Post.class
                .getDeclaredField("body")
                .getAnnotation(Column.class);

        assertThat(column).isNotNull();
        assertThat(column.columnDefinition()).isEqualTo("TEXT");
    }
}
