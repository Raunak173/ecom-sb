package com.ecom.project.service;

import com.ecom.project.model.Product;
import com.ecom.project.payload.ProductDTO;
import com.ecom.project.payload.ProductResponse;

public interface ProductService {

    ProductDTO addProduct(Long categoryId, Product product);
    ProductResponse getAllProducts();
    ProductResponse searchByCategory(Long categoryId);
}
