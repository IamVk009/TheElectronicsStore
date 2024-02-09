package com.lucifer.electronics.store.services.impl;

import com.lucifer.electronics.store.dtos.CategoryDto;
import com.lucifer.electronics.store.dtos.PageableResponse;
import com.lucifer.electronics.store.entities.Category;
import com.lucifer.electronics.store.exceptions.ResourceNotFoundException;
import com.lucifer.electronics.store.repositories.CategoryRepository;
import com.lucifer.electronics.store.services.CategoryService;
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
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.save(modelMapper.map(categoryDto, Category.class));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not found Exception..!"));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory = categoryRepository.save(category);
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category with given ID does not exist..!"));
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategories(int pageSize, int pageNumber, String soryBy, String sortDirection) {
//      Creating sort object
        Sort sort = (sortDirection.equalsIgnoreCase("asc"))? (Sort.by(soryBy).ascending()) : (Sort.by(soryBy).descending());
//      Creating pageable object using PageRequest implementation class of Pageable interface
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        List<Category> categoryList = page.getContent();
//      Converting list of Category Objects into list of Category DTO objects using stream api
        List<CategoryDto> categoryDtoList = categoryList.stream().map(category -> modelMapper.map(category, CategoryDto.class)).toList();
        return getPageableResponse(categoryDtoList, page);
    }

    @Override
    public CategoryDto getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category with given ID does not exist..!"));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> searchCategoryByTitle(String categoryTitle) {
        List<Category> categoryList = categoryRepository.findByTitleContainingIgnoreCase(categoryTitle);
        List<CategoryDto> categoryDtoList = categoryList.stream().map(category -> modelMapper.map(category, CategoryDto.class)).toList();
        return categoryDtoList;
    }

    private PageableResponse<CategoryDto> getPageableResponse(List<CategoryDto> categoryDtoList, Page page){
        PageableResponse<CategoryDto> response = new PageableResponse<>();
        response.setContent(categoryDtoList);
        response.setTotalElements(page.getTotalElements());
        response.setPageSize(page.getSize());
        response.setPageNumber(page.getNumber());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());

        return response;
    }
}
