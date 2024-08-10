package com.ecom.project.service;

import com.ecom.project.payload.CategoryDTO;
import com.ecom.project.payload.CategoryResponse;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer page, Integer limit, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long categoryId);
    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}
