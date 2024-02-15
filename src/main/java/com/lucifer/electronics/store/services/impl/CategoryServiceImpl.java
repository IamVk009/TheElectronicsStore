package com.lucifer.electronics.store.services.impl;

import com.lucifer.electronics.store.dtos.CategoryDto;
import com.lucifer.electronics.store.dtos.PageableResponse;
import com.lucifer.electronics.store.entities.Category;
import com.lucifer.electronics.store.exceptions.ResourceNotFoundException;
import com.lucifer.electronics.store.helper.Helper;
import com.lucifer.electronics.store.repositories.CategoryRepository;
import com.lucifer.electronics.store.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${category.profile.image.path}")
    private String uploadCategoryImagePath;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
//      Generating categoryId randomly
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
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
//      Delete category image from DB associated with corresponding category.
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category with given ID does not exist..!"));
        String coverImage = category.getCoverImage();
        String coverImageDestination = uploadCategoryImagePath + coverImage;
        try {
            Path path = Paths.get(coverImageDestination);
            log.info("Path = " + path);
            Files.delete(path);
        } catch (NoSuchFileException e) {
            log.error("User Image does not exist in folder");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategories(int pageSize, int pageNumber, String sortBy, String sortDirection) {
//      Creating sort object
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
//      Creating pageable object using PageRequest implementation class of Pageable interface
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        return Helper.getPageableResponse(page, CategoryDto.class);
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
}
