package online.shopping.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import online.shopping.entity.Order;
import online.shopping.entity.OrderSummary;
import online.shopping.exception.NotFoundException;
import online.shopping.service.OrderSummaryRepository;

@RestController
public class OrderController {
	@Autowired
	private OrderSummaryRepository orderSummaryRespository;
	
	@PostMapping("/order/save") 
	public OrderSummary saveOrderSummary(@RequestBody OrderSummary orderSummary) {
		List<Order> orderList = orderSummary.getOrderList();
		if(orderList == null || orderList.isEmpty()) {
			throw new NotFoundException("Product Not Found In This Order!");
		}
		
		for(Order order : orderList) {
			order.setOrderSummary(orderSummary);
		}
		
		BigDecimal originalCost = orderSummary.calculateOriginalCost();
		orderSummary.setOriginalCost(originalCost);
		orderSummary.setFinalDiscount(BigDecimal.ZERO);
		orderSummary.setFinalCost(originalCost);
		
		return this.orderSummaryRespository.save(orderSummary);
	}
}
