package online.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import online.shopping.entity.Customer;
import online.shopping.entity.Order;
import online.shopping.entity.OrderSummary;
import online.shopping.exception.NotFoundException;
import online.shopping.service.CustomerRepository;
import online.shopping.service.OrderCalculationService;
import online.shopping.service.OrderRepository;
import online.shopping.service.OrderSummaryRepository;
import online.shopping.service.ProductCalculationService;
import online.shopping.service.ProductRepository;

@RestController
public class OrderController {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderSummaryRepository orderSummaryRespository;
	
	@Autowired
	private OrderCalculationService orderCalculationService;
	
	@Autowired
	private ProductCalculationService productCalculationService;
	
	@RequestMapping("/orders")
	public List<OrderSummary> list() {
		return this.orderSummaryRespository.findAll();
	}
	
	@RequestMapping("/order/{id}")
	public OrderSummary getOrderById(@PathVariable("id") Long id) {
		return this.orderSummaryRespository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Order Not Found [ID:%d]", id)));
	}
	
	@RequestMapping("/customer/{id}/orders")
	public List<OrderSummary> getOrdersByCustomerId(@PathVariable("id") Long id) {
		return this.orderSummaryRespository.findByCustomerId(id);
	}
	
	@PostMapping("/order/save")
	public OrderSummary saveOrders(@RequestBody OrderSummary orderSummary) {
		List<Order> orderList = orderSummary.getOrderList();
		if(orderList == null || orderList.isEmpty()) {
			throw new NotFoundException("Product Not Found In This Order!");
		}
		
		// refresh customer data by customer id
		Long customerId = orderSummary.getCustomer().getId();
		if(customerId == null) {
			throw new NotFoundException(String.format("Customer ID Must Be Provided [ID:%d]", customerId));
		}
		Customer customer = this.customerRepository.findById(customerId)
				.orElseThrow(() -> new NotFoundException(String.format("Customer Not Found [ID:%d]", customerId)));
		orderSummary.setCustomer(customer);
		
		// check if product is out of stock
		productCalculationService.checkProductStock(orderList);
		
		// refresh product data by product id
		productCalculationService.refreshProductDetails(orderList);
		
		// calculate cost and discount
		orderCalculationService.calculateCostAndDiscount(orderSummary);
		
		// Product object is not cascade-saved, and it is not refreshed after save 
		this.orderSummaryRespository.save(orderSummary);
		
		return orderSummary;
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
