package com.ecom.project.service;

import com.ecom.project.exceptions.APIException;
import com.ecom.project.exceptions.ResourceNotFoundException;
import com.ecom.project.model.Category;
import com.ecom.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> availableCategories = categoryRepository.findAll();
        if(availableCategories.isEmpty()){
            throw new APIException("There are no categories to show!");
        }
        return availableCategories;
    }

    @Override
    public String createCategory(Category category) {
        Category savedcategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedcategory != null){
            throw new APIException("Category with the name " + category.getCategoryName() + " already exists!");
        }
        categoryRepository.save(category);
        return "Category created successfully!";
    }

    @Override
    public String deleteCategory(Long categoryId) {

        Optional<Category> savedCategoryOptional = categoryRepository.findById(categoryId);

        Category savedCategory = savedCategoryOptional.orElseThrow(
                ()->new ResourceNotFoundException("Category","categoryId",categoryId));

        categoryRepository.delete(savedCategory);
        return "Category with id:" + categoryId + " removed successfully!";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Optional<Category> savedCategoryOptional = categoryRepository.findById(categoryId);

        Category savedCategory = savedCategoryOptional.orElseThrow(
                ()->new ResourceNotFoundException("Category","categoryId",categoryId));

        savedCategory.setCategoryName(category.getCategoryName());
        Category updatedCategory = categoryRepository.save(savedCategory);
        return updatedCategory;
    }
}
