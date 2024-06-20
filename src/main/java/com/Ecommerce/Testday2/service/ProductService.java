package com.Ecommerce.Testday2.service;

import com.Ecommerce.Testday2.model.Product;
import com.Ecommerce.Testday2.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService (ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getALlProducts () {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById (UUID id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalStateException("Product with ID " + product.getId() + " does not exist."));
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        return productRepository.save(existingProduct);
    }

    public String saveImage(MultipartFile image) throws IOException {
        String folder = "src/main/resources/static/images/";
        Path path = Paths.get(folder);

        // Ensure the directory exists
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        // Save the image file
        byte[] bytes = image.getBytes();
        Path filePath = path.resolve(image.getOriginalFilename());
        Files.write(filePath, bytes);
        return "/images/" + image.getOriginalFilename();
    }

    public boolean deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            return false;
        }
        productRepository.deleteById(id);
        return true;
    }
}
