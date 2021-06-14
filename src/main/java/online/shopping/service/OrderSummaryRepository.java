package online.shopping.service;

import org.springframework.data.jpa.repository.JpaRepository;

import online.shopping.entity.OrderSummary;

public interface OrderSummaryRepository extends JpaRepository<OrderSummary, Long>{

}
