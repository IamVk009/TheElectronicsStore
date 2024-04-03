package com.lucifer.electronics.store.controllers;

import com.lucifer.electronics.store.dtos.*;
import com.lucifer.electronics.store.services.CategoryService;
import com.lucifer.electronics.store.services.FileService;
import com.lucifer.electronics.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ProductService productService;

    @Value("${category.profile.image.path}")
    private String uploadCategoryImagePath;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String categoryId, @Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) throws IOException {
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
                                                                          @RequestParam(defaultValue = "title") String sortBy,
                                                                          @RequestParam(defaultValue = "asc") String sortDirection) {
        PageableResponse<CategoryDto> allCategories = categoryService.getAllCategories(pageSize, pageNumber, sortBy, sortDirection);
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId) {
        CategoryDto category = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchCategory(@PathVariable String keyword) {
        List<CategoryDto> categoryDtos = categoryService.searchCategoryByTitle(keyword);
        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }

    @PostMapping("/image/upload/{categoryId}")
    public ResponseEntity<ImageResponseMessage> uploadCategoryCoverImage(@PathVariable String categoryId, @RequestParam MultipartFile imageFile) throws IOException {
//      Fetching category based on categoryId
        CategoryDto category = categoryService.getCategoryById(categoryId);
//      Uploading Category cover image at uploadImageFilePath
        String imageFileName = fileService.uploadImageFile(imageFile, uploadCategoryImagePath);

//      Updating existing categoryCoverImageName with new one
        category.setCoverImage(imageFileName);
        CategoryDto updatedCategory = categoryService.updateCategory(category, categoryId);

        ImageResponseMessage responseMessage = ImageResponseMessage.builder()
                .imageFileName(imageFileName)
                .message("Category cover Image uploaded successfully..!")
                .status(HttpStatus.CREATED)
                .success(true)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }

    @GetMapping("/image/{categoryId}")
    public void serveCategoryCoverImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto category = categoryService.getCategoryById(categoryId);
        InputStream imageFile = fileService.getImageFile(uploadCategoryImagePath, category.getCoverImage());

//      Reading data from imageFile inputStream and adding it to response (Which requires HttpServletResponse)
//      Setting content type of response
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
//      Copying data from imageFile inputStream to response
        StreamUtils.copy(imageFile, response.getOutputStream());
    }

    //  Creating product by adding it to proper category at the time of creation itself.
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable String categoryId, @RequestBody ProductDto productDto) {
        ProductDto productDtoWithCategory = productService.createProductWithCategory(productDto, categoryId);
        return new ResponseEntity<>(productDtoWithCategory, HttpStatus.CREATED);
    }

//  Update category of existing product
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateProductWithCategoruId(@PathVariable String productId, @PathVariable String categoryId) {
        ProductDto productDto = productService.updateProductWithCategoruId(productId, categoryId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

//  Get all products of same category
    @GetMapping("/{categoryId}/product/all")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProductsOfSameCategory(@PathVariable String categoryId,
                                                                                     @RequestParam(defaultValue = "3") int pageSize,
                                                                                     @RequestParam(defaultValue = "0") int pageNumber,
                                                                                     @RequestParam(defaultValue = "title") String sortBy,
                                                                                     @RequestParam(defaultValue = "asc") String sortDirection) {
        PageableResponse<ProductDto> products = productService.getAllProductsWithSameCategory(categoryId, pageNumber, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}