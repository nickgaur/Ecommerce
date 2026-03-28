package com.ecommerce.project.services;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findAll(pageDetails);
        List<Product> foundProducts = pageProducts.getContent();

        List<ProductDTO> productDTOS = foundProducts.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        if (foundProducts.isEmpty()) {
            throw new APIException("No Products Exist!!");
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;
    }

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Category", "CategoryId", categoryId);
        });
        boolean isProductNotPresent = true;
        List<Product> products = category.getProducts();
        for (Product value : products) {
            if (value.getProductName().equals(productDTO.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }
        if (isProductNotPresent) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setImage("default.png");
            product.setCategory(category);
            double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
//        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        } else {
            throw new APIException("Product already exist!!");
        }
    }

    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> {
            return new ResourceNotFoundException("Category", "categoryId", categoryId);
        });
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);
        List<Product> productsById = pageProducts.getContent();
        if(productsById.isEmpty()){
            throw new APIException(category.getCategoryName() + " category does not have any products");
        }
//        List<Product> productsById = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOS = productsById.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;
    }

    public ProductResponse searchByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findProductByProductNameLikeIgnoreCase('%' + keyword + '%', pageDetails);
        List<Product> productByKeyword = pageProducts.getContent();
        if(productByKeyword.isEmpty()){
            throw new APIException("Products not found with keyword " + keyword);
        }
//        List<Product> productByKeyword = productRepository.findProductByProductNameLikeIgnoreCase('%' + keyword + '%');
//        List<Product> result = new ArrayList<>();
//        for(Product pr: product){
//            if(pr.getProductName().contains(keyword.toLowerCase())){
//                result.add(pr);
//            }
//        }
        List<ProductDTO> productDTOS = productByKeyword.stream()
                .map(product ->
                        modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;
    }

    public ProductDTO updateProductDTO(Long productId, ProductDTO productDTO) {
        Product productFromDB = productRepository.findById(productId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Product", "productId", productId);
        });
        Product product = modelMapper.map(productDTO, Product.class);
        productFromDB.setProductName(product.getProductName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setQuantity(product.getQuantity());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setDiscount(product.getDiscount());
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        productFromDB.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(productFromDB);
        ProductDTO productDTOS = modelMapper.map(savedProduct, ProductDTO.class);
        return productDTOS;
    }

    public ProductDTO deleteProduct(Long productId) {
        Product productFromDb = productRepository.findById(productId).orElseThrow(() -> {
                    throw new ResourceNotFoundException("Product", "ProductID", productId);
                }
        );
        productRepository.deleteById(productId);
        return modelMapper.map(productFromDb, ProductDTO.class);
    }

    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
//  Get the product from DB
        Product productFromDb = productRepository.findById(productId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Product", "Product Id", productId);
        });
//Upload image to server

//Get the file name of uploaded image
        String path = "images/";
        String fileName = fileService.uploadImage(path, image);
//Updating the new file name to the product
        productFromDb.setImage(fileName);

//Save updated product
        Product updatedProduct = productRepository.save(productFromDb);
//return DTO after mapping product to DTO
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

//    public String uploadImage(String path, MultipartFile file) throws IOException {
////File names of current / original file
//        String originalFileName = file.getOriginalFilename();
//
////        Generate a unique file name
//        String randomId = UUID.randomUUID().toString();
//
//        // mat.jpg --> 1234 --> 1234.jpg
//        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
//
//        String filePath = path + File.separator + fileName;
//
////        Check if path exist and create
//        File folder = new File(path);
//        if(!folder.exists()){
//            folder.mkdir();
//        }
//
////        Upload to server
//        Files.copy(file.getInputStream(), Paths.get(filePath));
//
////        Returning file
//        return fileName;
//
//
//    }

}