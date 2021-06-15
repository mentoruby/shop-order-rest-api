package online.shopping.service;

import java.util.ArrayList;
import java.util.List;

import online.shopping.entity.Order;
import online.shopping.entity.Product;

public abstract class PromotionServiceImpl implements PromotionService {
	protected String promoName;
	protected String productName;
	
	public PromotionServiceImpl(String promoName, String productName) {
		this.promoName = promoName;
		this.productName = productName;
	}
	
	protected Order groupOrders(List<Order> orderList) {
		Order groupedOrder = null;
		if(orderList != null) {
			for(Order order : orderList) {
				Product product = order.getProduct();
				
				if(!product.getName().equals(this.productName)) {
					continue;
				}
				
				if(groupedOrder == null) {
					groupedOrder = new Order(product, order.getQuantity());
				} else {
					groupedOrder.setQuantity(groupedOrder.getQuantity() + order.getQuantity());
				}
			}
		}
		return groupedOrder;
	}

	@Override
	public List<Order> getUnrelatedOrderList(List<Order> orderList) {
		List<Order> unrelatedOrderList = new ArrayList<Order>();
		if(orderList != null) {
			for(Order order : orderList) {
				Product product = order.getProduct();
				
				if(!product.getName().equals(this.productName)) {
					unrelatedOrderList.add(order);
				}
			}
		}
		return unrelatedOrderList;
	}

}
