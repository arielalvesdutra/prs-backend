package dev.arielalvesdutra.prs.factories;

import dev.arielalvesdutra.prs.builders.PostBuilder;
import dev.arielalvesdutra.prs.entities.Post;

import java.time.Instant;

public class PostFactory {
    public static Post newPost() {
        return new PostBuilder()
                .withTitle("Titulo do Post")
                .withSubtitle("Subtítulo")
                .withBody("Corpo do post")
                .withCreatedAt(Instant.now())
                .build();
    }

    public static Post newPostWithId() {
        Post post = newPost();
        post.setId(1L);
        return post;
    }
}
