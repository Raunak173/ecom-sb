package com.ecom.project.service;

import com.ecom.project.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();
    String createCategory(Category category);
    String deleteCategory(Long categoryId);
    Category updateCategory(Category category, Long categoryId);
}
