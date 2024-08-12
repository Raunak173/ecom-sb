package com.ecom.project.service;

import com.ecom.project.model.Product;
import com.ecom.project.payload.ProductDTO;
import com.ecom.project.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);
    ProductResponse getAllProducts(Integer page, Integer limit, String sortBy, String sortOrder);
    ProductResponse searchByCategory(Long categoryId,Integer page, Integer limit, String sortBy, String sortOrder);
    ProductResponse searchByKeyword(String keyword,Integer page, Integer limit, String sortBy, String sortOrder);
    ProductDTO updateProduct(Long productId, ProductDTO productDTO);
    ProductDTO deleteProduct(Long productId);
    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
