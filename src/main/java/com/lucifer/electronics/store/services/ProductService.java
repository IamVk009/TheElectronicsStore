package com.lucifer.electronics.store.services;

import com.lucifer.electronics.store.dtos.PageableResponse;
import com.lucifer.electronics.store.dtos.ProductDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto, String productId);

    void deleteProduct(String productId);

    ProductDto getProductById(String productId);

    PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDirection);

    PageableResponse<ProductDto> getAllLiveProducts(int pageNumber, int pageSize, String sortBy, String sortDirection);

    PageableResponse<ProductDto> searchProduct(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection);

    String uploadProductImage(String productId, MultipartFile imageFile) throws IOException;

    void serveProductImage(String productId, HttpServletResponse response) throws IOException;

    ProductDto createProductWithCategory(ProductDto productDto, String categoryId);
}
