package com.ecommerce.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class CategoryController {

    @GetMapping("/categories")
    public String getCategory(){
        return "This is the category route.";
    }
}
