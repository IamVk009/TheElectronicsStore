package com.lucifer.electronics.store.services;

import com.lucifer.electronics.store.dtos.CategoryDto;
import com.lucifer.electronics.store.dtos.PageableResponse;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

    void deleteCategory(String categoryId);

    PageableResponse<CategoryDto> getAllCategories(int pageSize, int pageNumber, String soryBy, String sortDirection);

    CategoryDto getCategoryById(String categoryId);

    List<CategoryDto> searchCategoryByTitle(String categoryTitle);
}
