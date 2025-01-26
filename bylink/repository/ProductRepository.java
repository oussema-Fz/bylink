package com.bylink.repository;

import com.bylink.entity.Product;
import com.bylink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByMerchant(User merchant);
    Optional<Product> findByProductLink(String productLink);
}
