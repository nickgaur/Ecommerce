package com.ecommerce.project.services;

import com.ecommerce.project.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {


    private List<Category> categories = new ArrayList<>();
    private Long index = 1l;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }


    public void createCategory(Category category) {
        if(category.getCategoryName() != null){
            category.setCategoryId(index++);
            categories.add(category);
            return;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New Category Can't be Created");

    }

    public String deleteCategory(Long categoryId){
            for(Category category: categories) {
                if (category.getCategoryId() == categoryId) {
                    categories.remove(category);
                    return "Category with id " + categoryId + " Deleted Successfully";
                }
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found");
    }
}
