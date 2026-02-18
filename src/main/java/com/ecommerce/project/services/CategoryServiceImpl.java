package com.ecommerce.project.services;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    //    private List<Category> categories = new ArrayList<>();
//    private Long index = 1l;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = repository.findAll(pageDetails);
        List<Category> foundCategories = categoryPage.getContent();
        if (foundCategories.isEmpty()) {
            throw new APIException("List contains empty result.");
        }
        List<CategoryDTO> categoryDTOS = foundCategories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        System.out.println("categoryDTOS: " + categoryDTOS);
        System.out.println("foundCate: " + foundCategories);
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getSize());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        categoryResponse.setSortBy(sortBy);
        categoryResponse.setSortOrder(sortOrder);
        return categoryResponse;
    }


    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
//        if(category.getCategoryName() != null){
//            category.setCategoryId(index++);
//        repository.save(category);
//        return;
//        }
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New Category Can't be Created");
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category cateforyFromDb = repository.findByCategoryName(categoryDTO.getCategoryName());
        if(cateforyFromDb != null){
            throw new APIException("Category already exists with category name: " + cateforyFromDb.getCategoryName());
        }
        Category savedCategory = repository.save(category);
        CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);
        return savedCategoryDTO;
    }

    public CategoryDTO deleteCategory(Long categoryId) {
//        List<Category> categories = repository.findAll();
//            for(Category category: categories) {
//                if (category.getCategoryId() == categoryId) {
//                    repository.delete(category);
//                    return "Category with id " + categoryId + " Deleted Successfully";
//                }
//            }
        Category category = repository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));

        repository.delete(category);
        CategoryDTO deleteCategoryDTO = modelMapper.map(category, CategoryDTO.class);
        return deleteCategoryDTO;
    }

    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
//        List<Category> categories = repository.findAll();
//        for (Category iter : categories) {
//            if (iter.getCategoryId() == categoryId) {
//                iter.setCategoryName(category.getCategoryName());
//                repository.save(iter);
//                return iter;
//            }
//        }
//        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id " + categoryId + " Not Found.");
        Category savedCategory = repository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
        Category category = modelMapper.map(categoryDTO, Category.class);
        savedCategory.setCategoryName(category.getCategoryName());
        repository.save(savedCategory);
        CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);
        return savedCategoryDTO;
    }
}
