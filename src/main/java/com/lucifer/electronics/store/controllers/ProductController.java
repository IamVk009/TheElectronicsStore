package com.lucifer.electronics.store.controllers;

import com.lucifer.electronics.store.dtos.ApiResponseMessage;
import com.lucifer.electronics.store.dtos.ImageResponseMessage;
import com.lucifer.electronics.store.dtos.PageableResponse;
import com.lucifer.electronics.store.dtos.ProductDto;
import com.lucifer.electronics.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto product = productService.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String productId, @RequestBody ProductDto productDto) {
        ProductDto updatedProduct = productService.updateProduct(productDto, productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Product Deleted Successfully..")
                .success(true)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String productId) {
        ProductDto productDto = productService.getProductById(productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(@RequestParam(defaultValue = "1") int pageNumber,
                                                                       @RequestParam(defaultValue = "0") int pageSize,
                                                                       @RequestParam(defaultValue = "productTitle") String sortBy,
                                                                       @RequestParam(defaultValue = "asc") String sortDirection) {
        PageableResponse<ProductDto> allProducts = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProducts(@RequestParam(defaultValue = "0") int pageNumber,
                                                                           @RequestParam(defaultValue = "5") int pageSize,
                                                                           @RequestParam(defaultValue = "productTitle") String sortBy,
                                                                           @RequestParam(defaultValue = "asc") String sortDirection) {
        PageableResponse<ProductDto> allLiveProducts = productService.getAllLiveProducts(pageNumber, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(allLiveProducts, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProduct(@PathVariable String keyword,
                                                                      @RequestParam(defaultValue = "0") int pageNumber,
                                                                      @RequestParam(defaultValue = "10") int pageSize,
                                                                      @RequestParam(defaultValue = "productTitle") String sortBy,
                                                                      @RequestParam(defaultValue = "asc") String sortDirection) {
        PageableResponse<ProductDto> products = productService.searchProduct(keyword, pageNumber, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload/image/{productId}")
    public ResponseEntity<ImageResponseMessage> uploadProductImage(@PathVariable String productId, @RequestParam MultipartFile productImageFile) throws IOException {
        String fileName = productService.uploadProductImage(productId, productImageFile);
        ImageResponseMessage responseMessage = ImageResponseMessage.builder()
                .imageFileName(fileName)
                .message("Product Image uploaded successfully..!")
                .success(true)
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }

    @GetMapping("/image/{productId}")
    public void serveProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        productService.serveProductImage(productId, response);
    }

}
