package online.shopping.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import online.shopping.entity.OrderSummary;

public interface OrderSummaryRepository extends JpaRepository<OrderSummary, Long>{
	public List<OrderSummary> findByCustomerId(Long customerId);
}
