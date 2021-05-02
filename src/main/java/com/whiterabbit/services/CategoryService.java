package com.whiterabbit.services;

import com.whiterabbit.entities.CategoryOld;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    CategoryOld saveCategory(CategoryOld categoryOld);
    CategoryOld updateCategoryById(String id, CategoryOld categoryOld);
    void deleteCategoryById(String id);
    Optional<CategoryOld> getCategoryById(String id);
    List<CategoryOld> findAll();
}
