package com.whiterabbit.controller;

import com.whiterabbit.dao.CategoryRepository;
import com.whiterabbit.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @RequestMapping(value = "/categories")
    public List<Category> listeCategories(){
        return categoryRepository.findAll();
    }
}
