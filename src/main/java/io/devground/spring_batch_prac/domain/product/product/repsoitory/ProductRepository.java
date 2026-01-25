package io.devground.spring_batch_prac.domain.product.product.repsoitory;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devground.spring_batch_prac.domain.product.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
