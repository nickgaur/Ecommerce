package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.services.CategoryService;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("/")
public class CategoryController {
    private CategoryService categoryService;
    private int id=-1;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/public/categories")
    public List getCategory() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/api/public/categories")
    public String createCategory(@RequestBody Category category){
        try{
            categoryService.createCategory(category);
            return "New Category Created";
        }
        catch (Exception e){
            System.out.println(e);
            throw new RuntimeException();

        }
    }

    @DeleteMapping("/api/admin/categories/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId){
        return categoryService.deleteCategory(categoryId);

    }
}
