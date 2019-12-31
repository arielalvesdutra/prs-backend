package dev.arielalvesdutra.prs.services;

import dev.arielalvesdutra.prs.entities.Category;
import dev.arielalvesdutra.prs.repositories.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CategoryService {
    
    private CategoryRepository categoryRepository;
    
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId).get();
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Page<Category> findAll(Pageable pageable) {
        return  categoryRepository.findAll(pageable);
    }

    @Transactional
    public Category update(Long id, Category parameterCategory) {
        Category existingCategory = findById(id);

        existingCategory.setName(parameterCategory.getName());
        existingCategory.setDescription(parameterCategory.getDescription());

        return existingCategory;
    }

    @Transactional
    public void deleteById(Long id) {
        Category category = findById(id);
        categoryRepository.deleteById(category.getId());
    }
}
