package com.ecommerce.project.services;

import com.ecommerce.project.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        category.setCategoryId(index++);
        categories.add(category);
    }

    public String deleteCategory(Long categoryId){
        for(Category category: categories) {
            if (category.getCategoryId() == categoryId) {
                categories.remove(category);
                return "Category with id " + categoryId + " Deleted Successfully";
            }
        }
        return "Category Not Found";
    }
}
