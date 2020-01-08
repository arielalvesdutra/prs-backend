package dev.arielalvesdutra.prs.controllers.dto;

import dev.arielalvesdutra.prs.entities.Category;
import dev.arielalvesdutra.prs.entities.Post;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.Instant;

@Getter @Setter @ToString @NoArgsConstructor @EqualsAndHashCode
public class RetrievePostDTO {

    private Long id;
    private String title;
    private String subtitle;
    private String body;
    private Category category;
    private Instant createdAt;

    public RetrievePostDTO(Post post) {
        setId(post.getId());
        setTitle(post.getTitle());
        setSubtitle(post.getSubtitle());
        setBody(post.getBody());
        setCategory(post.getCategory());
        setCreatedAt(post.getCreatedAt());
    }

    public static Page<RetrievePostDTO> toPage(Page<Post> postsPage) {
        return postsPage.map(RetrievePostDTO::new);
    }
}
