package shop.alten.shop.Product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.alten.shop.Product.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
