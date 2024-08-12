package com.ecom.project.service;

import com.ecom.project.exceptions.APIException;
import com.ecom.project.exceptions.ResourceNotFoundException;
import com.ecom.project.model.Category;
import com.ecom.project.model.Product;
import com.ecom.project.payload.ProductDTO;
import com.ecom.project.payload.ProductResponse;
import com.ecom.project.repositories.CategoryRepository;
import com.ecom.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        //First get the category from categoryId
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Category","categoryId",categoryId));
        Product product = modelMapper.map(productDTO,Product.class);
        product.setCategory(category);
        product.setImage("default.png");
        double specialPrice = product.getPrice() - ((product.getDiscount()*0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer page,Integer limit, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        //Provided by spring for pagination
        Pageable pageDetails = PageRequest.of(page,limit, sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);

        List<Product> availableProducts = productPage.getContent();
        if(availableProducts.isEmpty()){
            throw new APIException("There are no products to show!");
        }
        List<ProductDTO> productDTOS = availableProducts.stream()
                .map((product -> modelMapper.map(product,ProductDTO.class)))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse
                .setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId,Integer page, Integer limit, String sortBy, String sortOrder) {
        //First get the category from categoryId
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Category","categoryId",categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        //Provided by spring for pagination
        Pageable pageDetails = PageRequest.of(page,limit, sortByAndOrder);
        Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);
        List<Product> availableProducts = productPage.getContent();
        if(availableProducts.isEmpty()){
            throw new APIException("There are no products to show!");
        }
        List<ProductDTO> productDTOS = availableProducts.stream()
                .map((product -> modelMapper.map(product,ProductDTO.class)))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse
                .setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchByKeyword(String keyword,Integer page, Integer limit, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(page,limit, sortByAndOrder);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%',pageDetails);
        List<Product> availableProducts = productPage.getContent();
        if(availableProducts.isEmpty()){
            throw new APIException("There are no products to show!");
        }
        List<ProductDTO> productDTOS = availableProducts.stream()
                .map((product -> modelMapper.map(product,ProductDTO.class)))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse
                .setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {

        //Get existing product by id
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Product","productId",productId));
        Product product = modelMapper.map(productDTO,Product.class);
        Product updatedProduct = new Product();
        updatedProduct.setProductName(product.getProductName());
        updatedProduct.setDescription(product.getDescription());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setQuantity(product.getQuantity());
        updatedProduct.setDiscount(product.getDiscount());

        double specialPrice = product.getPrice() - ((product.getDiscount()*0.01) * product.getPrice());
        updatedProduct.setSpecialPrice(specialPrice);

        Product savedProduct = productRepository.save(updatedProduct);
        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        //Get existing product by id
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Product","productId",productId));
        productRepository.delete(existingProduct);
        return modelMapper.map(existingProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {

        //Get existing product by id
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Product","productId",productId));

        //Upload image to the server
        String fileName = uploadImage(path,image);

        productFromDB.setImage(fileName);

        Product updatedProduct = productRepository.save(productFromDB);
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {

        String originalFileName = file.getOriginalFilename();

        //Generate unique filename
        String randomId = UUID.randomUUID().toString();
        //mac.png -> 1234 -> 1234.png
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;

        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }

        //Upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }
}
