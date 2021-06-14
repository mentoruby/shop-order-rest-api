package online.shopping.service;

import org.springframework.data.jpa.repository.JpaRepository;

import online.shopping.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
