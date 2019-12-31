package dev.arielalvesdutra.prs.services;

import dev.arielalvesdutra.prs.entities.Category;
import dev.arielalvesdutra.prs.entities.Post;
import dev.arielalvesdutra.prs.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final CategoryService categoryService;

    public PostService(PostRepository postRepository, CategoryService categoryService) {
        this.postRepository = postRepository;
        this.categoryService = categoryService;
    }

    @Transactional
    public Post create(Post post) {
        Category category = categoryService.findById(post.getCategory().getId());
        post.setCategory(category);
        return postRepository.save(post);
    }

    public Post findById(Long postId) {
        return postRepository.findById(postId).get();
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Transactional
    public Post update(Long id, Post fetchedPost) {
        Post existingPost = findById(id);
        Category category = categoryService.findById(fetchedPost.getCategory().getId());

        existingPost
                .setTitle(fetchedPost.getTitle())
                .setSubtitle(fetchedPost.getSubtitle())
                .setBody(fetchedPost.getBody())
                .setCategory(category);

        return existingPost;
    }

    @Transactional
    public void deleteById(Long id) {
        Post existingPost = findById(id);

        postRepository.deleteById(existingPost.getId());
    }
}
