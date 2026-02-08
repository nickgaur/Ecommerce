package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
//@RequestMapping("/")
public class CategoryController {
    private CategoryService categoryService;
    private int id=-1;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/public/categories")
    public ResponseEntity<List<Category>> getCategory() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping("/api/public/categories")
    public ResponseEntity<String> createCategory(@RequestBody Category category){
        try{
            categoryService.createCategory(category);
            return new ResponseEntity<>("Category added successfully", HttpStatus.CREATED);
        }
        catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), HttpStatus.BAD_REQUEST);

        }
    }

    @DeleteMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        try{
            String status = categoryService.deleteCategory(categoryId);
//            return ResponseEntity.ok(status);
//            return ResponseEntity.status(HttpStatus.OK).body(status);
            return new ResponseEntity<>(status, HttpStatus.OK);
        }
        catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(),HttpStatus.NOT_FOUND);
        }

    }
}
