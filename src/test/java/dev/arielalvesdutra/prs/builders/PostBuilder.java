package dev.arielalvesdutra.prs.builders;

import dev.arielalvesdutra.prs.entities.Category;
import dev.arielalvesdutra.prs.entities.Post;

import java.time.Instant;

public class PostBuilder {

    private Post post = new Post();

    public PostBuilder withId(Long id) {
        post.setId(id);
        return this;
    }

    public PostBuilder withTitle(String title) {
        post.setTitle(title);
        return this;
    }

    public PostBuilder withSubtitle(String subtitle) {
        post.setSubtitle(subtitle);
        return this;
    }

    public PostBuilder withBody(String body) {
        post.setBody(body);
        return this;
    }

    public PostBuilder withCreatedAt(Instant createdAt) {
        post.setCreatedAt(createdAt);
        return this;
    }

    public PostBuilder withCategory(Category category) {
        post.setCategory(category);
        return this;
    }    

    public Post build() {
        return post;
    }
}
