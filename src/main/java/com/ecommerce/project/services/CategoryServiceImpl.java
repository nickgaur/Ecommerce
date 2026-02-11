package com.ecommerce.project.services;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    //    private List<Category> categories = new ArrayList<>();
    private Long index = 1l;

    @Override
    public List<Category> getAllCategories() {
        return repository.findAll();
    }


    public void createCategory(Category category) {
//        if(category.getCategoryName() != null){
//            category.setCategoryId(index++);
        repository.save(category);
        return;
//        }
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New Category Can't be Created");

    }

    public String deleteCategory(Long categoryId) {
//        List<Category> categories = repository.findAll();
//            for(Category category: categories) {
//                if (category.getCategoryId() == categoryId) {
//                    repository.delete(category);
//                    return "Category with id " + categoryId + " Deleted Successfully";
//                }
//            }
        Optional<Category> foundOptionalCategory = repository.findById(categoryId);
        Category foundCategory = foundOptionalCategory.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found"));

        repository.delete((Category) foundCategory);
        return "Category with id " + categoryId + " Deleted Successfully";
    }

    public Category updateCategory(Long categoryId, Category category) {
//        List<Category> categories = repository.findAll();
//        for (Category iter : categories) {
//            if (iter.getCategoryId() == categoryId) {
//                iter.setCategoryName(category.getCategoryName());
//                repository.save(iter);
//                return iter;
//            }
//        }
//        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id " + categoryId + " Not Found.");
        Optional<Category> optionalFoundCategory = repository.findById(categoryId);
        Category foundCategory = optionalFoundCategory.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id " + categoryId + " Not Found."));
        foundCategory.setCategoryName(category.getCategoryName());
        repository.save(foundCategory);
        return foundCategory;
    }
}
