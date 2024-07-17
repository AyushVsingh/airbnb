package com.airbnb.controller;

import com.airbnb.entity.Product;
import com.airbnb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final String IMAGE_FOLDER = "src/main/resources/static/images/";

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

//    @PostMapping
//    public Product saveProduct(@RequestParam("title") String title,
//                               @RequestParam("description") String description,
//                               @RequestParam("price") double price,
//                               @RequestParam("category") String category,
//                               @RequestParam("image") MultipartFile imageFile) throws IOException {
//        String imageName = StringUtils.cleanPath(imageFile.getOriginalFilename());
//        Path imagePath = Paths.get(IMAGE_FOLDER, imageName);
//        Files.copy(imageFile.getInputStream(), imagePath);
//
//        Product product = new Product();
//        product.setTitle(title);
//        product.setDescription(description);
//        product.setPrice(price);
//        product.setCategory(category);
//        product.setImage("/images/" + imageName); // Save relative path
//
//        return productService.saveProduct(product);
//    }

    @PostMapping("/bulk")
    public void saveAllProducts(@RequestBody List<Product> products) {
        productService.saveAllProducts(products);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path imagePath = Paths.get(IMAGE_FOLDER).resolve(filename).normalize();
            Resource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
