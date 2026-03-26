package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private CategoryService categoryService;
    private int id = -1;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategory(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
//        try{
//            return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
//        }
//        catch (EmptyResultException e){
//            return new ResponseEntity<String>(e.getMessage, HttpStatus.NO_CONTENT);
//        }

    }

    @PostMapping("/public/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
//        try {
        categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>("Category added successfully", HttpStatus.CREATED);
//        } catch (ResponseStatusException e) {
//            return new ResponseEntity<>(e.getReason(), HttpStatus.BAD_REQUEST);

//        }
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
//        try {
        CategoryDTO categoryDTO = categoryService.deleteCategory(categoryId);
//            return ResponseEntity.ok(status);
//            return ResponseEntity.status(HttpStatus.OK).body(status);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
//        } catch (ResponseStatusException e) {
//            return new ResponseEntity<>(e.getReason(), HttpStatus.NOT_FOUND);
//        }
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO) {
//        try {
        CategoryDTO savedCategoryDTO = categoryService.updateCategory(categoryId, categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
//        } catch (ResponseStatusException e) {
//           return new ResponseEntity<>(e.getReason(), HttpStatus.NOT_FOUND);
//        }
    }
}
