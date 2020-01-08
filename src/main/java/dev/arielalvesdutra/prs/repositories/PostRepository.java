package dev.arielalvesdutra.prs.repositories;

import dev.arielalvesdutra.prs.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> { }
