package online.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import online.shopping.entity.Order;
import online.shopping.entity.OrderSummary;
import online.shopping.exception.NotFoundException;
import online.shopping.service.OrderCalculationService;
import online.shopping.service.OrderSummaryRepository;

@RestController
public class OrderController {
	@Autowired
	private OrderSummaryRepository orderSummaryRespository;
	
	@Autowired
	private OrderCalculationService orderCalculationService;
	
	@PostMapping("/order/save")
	public OrderSummary saveOrders(@RequestBody OrderSummary orderSummary) {
		List<Order> orderList = orderSummary.getOrderList();
		if(orderList == null || orderList.isEmpty()) {
			throw new NotFoundException("Product Not Found In This Order!");
		}
		
		orderCalculationService.updateCostAndDiscount(orderSummary);
		
		return this.orderSummaryRespository.save(orderSummary);
	}
	
	@PostMapping("/order/promo/save")
	public OrderSummary saveOrdersWithPromotion(@RequestBody OrderSummary orderSummary) {
		List<Order> orderList = orderSummary.getOrderList();
		if(orderList == null || orderList.isEmpty()) {
			throw new NotFoundException("Product Not Found In This Order!");
		}
		
		orderCalculationService.applyOrderPromotion(orderSummary);
		
		return this.orderSummaryRespository.save(orderSummary);
	}
}
