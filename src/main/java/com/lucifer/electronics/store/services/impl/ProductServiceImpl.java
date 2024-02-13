package com.lucifer.electronics.store.services.impl;

import com.lucifer.electronics.store.dtos.PageableResponse;
import com.lucifer.electronics.store.dtos.ProductDto;
import com.lucifer.electronics.store.entities.Category;
import com.lucifer.electronics.store.entities.Product;
import com.lucifer.electronics.store.exceptions.ResourceNotFoundException;
import com.lucifer.electronics.store.repositories.CategoryRepository;
import com.lucifer.electronics.store.repositories.ProductRepository;
import com.lucifer.electronics.store.services.FileService;
import com.lucifer.electronics.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${product.profile.image.path}")
    private String uploadProductImagePath;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
//      Generating product Id
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
//      Add current date
        productDto.setAddedDate(new Date());
        Product product = productRepository.save(mapper.map(productDto, Product.class));
        return mapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with given Id does not exist .."));

        product.setProductTitle(productDto.getProductTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setStock(productDto.isStock());
        product.setLive(productDto.isLive());

        Product updatedProduct = productRepository.save(product);
        return mapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with given Id does not exist.."));
//      Delete product image from folder location associated with corresponding product.
        String imageName = product.getImageName();
        String productImageDestination = uploadProductImagePath + imageName;
        try {
            Path path = Paths.get(productImageDestination);
            Files.delete(path);
        } catch (NoSuchFileException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        productRepository.delete(product);
    }

    @Override
    public ProductDto getProductById(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with given Id does not exist.."));
        return mapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findAll(pageable);
        List<Product> productList = page.getContent();

        List<ProductDto> productDtoList = productList.stream().map(product -> mapper.map(product, ProductDto.class)).toList();
        return getPageableResponse(productDtoList, page);
    }

    @Override
    public PageableResponse<ProductDto> getAllLiveProducts(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);
        List<ProductDto> productDtoList = page.stream().map(product -> mapper.map(product, ProductDto.class)).toList();
        return getPageableResponse(productDtoList, page);
    }

    @Override
    public PageableResponse<ProductDto> searchProduct(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByProductTitleContaining(keyword, pageable);
        List<ProductDto> productDtoList = page.stream().map(product -> mapper.map(product, ProductDto.class)).toList();
        return getPageableResponse(productDtoList, page);
    }

    @Override
    public String uploadProductImage(String productId, MultipartFile imageFile) throws IOException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with given Id does not exist.."));
        String fileName = fileService.uploadImageFile(imageFile, uploadProductImagePath);
        product.setImageName(fileName);
        productRepository.save(product);
        return fileName;
    }

    @Override
    public void serveProductImage(String productId, HttpServletResponse response) throws IOException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with given Id does not exist.."));
        String imageFileName = product.getImageName();
        InputStream imageFile = fileService.getImageFile(uploadProductImagePath, imageFileName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(imageFile, response.getOutputStream());
    }

    @Override
    public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category with given Id does not exists...!"));
        Product product = mapper.map(productDto, Product.class);

        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateProductWithCategoruId(String productId, String categoryId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with given Id does not exist..!"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category with given Id does not exist..!"));
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProductsWithSameCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category with given Id does not exist..!"));
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findByCategory(category, pageable);
        List<Product> productList = productPage.getContent();
        List<ProductDto> productDtoList = productList.stream().map(product -> mapper.map(product, ProductDto.class)).toList();
        return getPageableResponse(productDtoList, productPage);
    }

    private PageableResponse<ProductDto> getPageableResponse(List<ProductDto> productDtos, Page page) {
        PageableResponse<ProductDto> response = new PageableResponse<>();
        response.setContent(productDtos);
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setPageNumber(page.getNumber());
        response.setLastPage(page.isLast());
        response.setPageSize(page.getSize());

        return response;
    }
}