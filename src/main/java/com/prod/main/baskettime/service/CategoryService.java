package com.prod.main.baskettime.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.prod.main.baskettime.entity.Category;
import com.prod.main.baskettime.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAllByOrderByAlignAsc();
    }
}
