package online.shopping.service;

import org.springframework.data.jpa.repository.JpaRepository;

import online.shopping.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
