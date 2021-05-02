package com.whiterabbit.services;

import com.whiterabbit.dao.CategoryRepository;
import com.whiterabbit.entities.CategoryOld;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class CategoryServiceImpl implements CategoryService{


    @Override
    public CategoryOld saveCategory(CategoryOld categoryOld) {
        return null;
    }

    @Override
    public CategoryOld updateCategoryById(String id, CategoryOld categoryOld) {
        return null;
    }

    @Override
    public void deleteCategoryById(String id) {

    }

    @Override
    public Optional<CategoryOld> getCategoryById(String id) {
        return Optional.empty();
    }

    @Override
    public List<CategoryOld> findAll() {
        return null;
    }
}
