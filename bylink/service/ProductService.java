package com.bylink.service;

import com.bylink.entity.Product;
import com.bylink.entity.User;
import com.bylink.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product createProduct(Product product, User merchant) {
        product.setMerchant(merchant);
        product.setProductLink(generateProductLink());
        return productRepository.save(product);
    }

    public List<Product> getMerchantProducts(User merchant) {
        return productRepository.findByMerchant(merchant);
    }

    public Product getProductByLink(String productLink) {
        return productRepository.findByProductLink(productLink)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    public Product updateProduct(Long productId, Product updatedProduct) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setDeliveryFee(updatedProduct.getDeliveryFee());
        product.setImageUrl(updatedProduct.getImageUrl());

        return productRepository.save(product);
    }

    private String generateProductLink() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
