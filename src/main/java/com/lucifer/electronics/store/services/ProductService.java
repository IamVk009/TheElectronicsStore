package com.lucifer.electronics.store.services;

import com.lucifer.electronics.store.dtos.PageableResponse;
import com.lucifer.electronics.store.dtos.ProductDto;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto, String productId);

    void deleteProduct(String productId);

    ProductDto getProductById(String productId);

    PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String soryBy, String sortDirection);

    PageableResponse<ProductDto> getAllLiveProducts(int pageNumber, int pageSize, String soryBy, String sortDirection);

    PageableResponse<ProductDto> searchProduct(String keyword, int pageNumber, int pageSize, String soryBy, String sortDirection);
}
