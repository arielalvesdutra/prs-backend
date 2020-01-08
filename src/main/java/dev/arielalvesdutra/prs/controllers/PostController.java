package dev.arielalvesdutra.prs.controllers;

import dev.arielalvesdutra.prs.controllers.dto.CreatePostDTO;
import dev.arielalvesdutra.prs.controllers.dto.RetrievePostDTO;
import dev.arielalvesdutra.prs.controllers.dto.UpdatePostDTO;
import dev.arielalvesdutra.prs.entities.Post;
import dev.arielalvesdutra.prs.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RequestMapping("/posts")
@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @PreAuthorize("hasAuthority('posts.create')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RetrievePostDTO> create(
            @Valid @RequestBody CreatePostDTO createDto, UriComponentsBuilder uriBuilder) {

        Post createdPost = postService.create(createDto.toPost());
        URI uri = uriBuilder.path("/posts/{id}")
                .buildAndExpand(createdPost.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new RetrievePostDTO(createdPost));
    }

    @PreAuthorize("hasAuthority('posts.read')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<RetrievePostDTO>> retrieveAll(
            @PageableDefault(sort="title", page = 0, size = 10) Pageable pagination) {

        Page<Post> postsPage = postService.findAll(pagination);

        return ResponseEntity.ok().body(RetrievePostDTO.toPage(postsPage));
    }

    @PreAuthorize("hasAuthority('posts.read')")
    @RequestMapping(method = RequestMethod.GET, path = "/{postId}")
    public ResponseEntity<RetrievePostDTO> retrieveById(@PathVariable Long postId) {

        Post post = postService.findById(postId);

        return ResponseEntity.ok().body(new RetrievePostDTO(post));
    }

    @PreAuthorize("hasAuthority('posts.delete')")
    @RequestMapping(method = RequestMethod.DELETE, path = "/{postId}")
    public ResponseEntity<?> deleteById(@PathVariable Long postId) {

        postService.deleteById(postId);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('posts.update')")
    @RequestMapping(method = RequestMethod.PUT, path = "/{postId}")
    public ResponseEntity<RetrievePostDTO> updateById(
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostDTO updateDto) {

        Post updatedPost = postService.update(postId, updateDto.toPost());

        return ResponseEntity.ok().body(new RetrievePostDTO(updatedPost));
    }
}
