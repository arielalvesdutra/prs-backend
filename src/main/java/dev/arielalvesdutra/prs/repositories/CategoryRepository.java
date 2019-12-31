package dev.arielalvesdutra.prs.repositories;

import dev.arielalvesdutra.prs.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> { }
