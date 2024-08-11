package com.ecom.project.service;

import com.ecom.project.exceptions.ResourceNotFoundException;
import com.ecom.project.model.Category;
import com.ecom.project.model.Product;
import com.ecom.project.payload.ProductDTO;
import com.ecom.project.payload.ProductResponse;
import com.ecom.project.repositories.CategoryRepository;
import com.ecom.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Long categoryId, Product product) {

        //First get the category from categoryId
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Category","categoryId",categoryId));
        product.setCategory(category);
        product.setImage("default.png");
        double specialPrice = product.getPrice() - ((product.getDiscount()*0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = products.stream()
                .map((product -> modelMapper.map(product,ProductDTO.class)))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse
                .setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        //First get the category from categoryId
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Category","categoryId",categoryId));
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOS = products.stream()
                .map((product -> modelMapper.map(product,ProductDTO.class)))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse
                .setContent(productDTOS);
        return productResponse;
    }
}
