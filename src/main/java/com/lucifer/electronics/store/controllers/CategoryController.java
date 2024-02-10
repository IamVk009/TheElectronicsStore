package com.lucifer.electronics.store.controllers;

import com.lucifer.electronics.store.dtos.ApiResponseMessage;
import com.lucifer.electronics.store.dtos.CategoryDto;
import com.lucifer.electronics.store.dtos.PageableResponse;
import com.lucifer.electronics.store.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String categoryId,@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
        categoryService.deleteCategory(categoryId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Category deleted successfully..!")
                .success(true)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(@RequestParam(defaultValue = "3") int pageSize,
                                                             @RequestParam(defaultValue = "0") int pageNumber,
                                                             @RequestParam(defaultValue = "title") String soryBy,
                                                             @RequestParam(defaultValue = "asc") String sortDirection){
        PageableResponse<CategoryDto> allCategories = categoryService.getAllCategories(pageSize, pageNumber, soryBy, sortDirection);
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId){
        CategoryDto category = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchCategory(@PathVariable String keyword){
        List<CategoryDto> categoryDtos = categoryService.searchCategoryByTitle(keyword);
        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }

}
