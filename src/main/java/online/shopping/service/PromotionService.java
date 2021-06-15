package online.shopping.service;

import java.util.List;

import online.shopping.entity.Order;
import online.shopping.entity.OrderPromo;

public interface PromotionService {
	public OrderPromo findOrderPromo(List<Order> orderList);
	
	public List<Order> getUnrelatedOrderList(List<Order> orderList);
}
