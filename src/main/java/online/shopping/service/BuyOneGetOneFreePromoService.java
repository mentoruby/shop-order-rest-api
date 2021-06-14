package online.shopping.service;

import java.math.BigDecimal;
import java.util.List;

import online.shopping.entity.Order;
import online.shopping.entity.OrderPromo;
import online.shopping.entity.Product;

public class BuyOneGetOneFreePromoService extends PromotionServiceImpl {
	
	public BuyOneGetOneFreePromoService(String productName) {
		super(String.format("Buy 1 Get 1 Free on %s", productName), productName);
	}

	@Override
	public OrderPromo findOrderPromo(List<Order> orderList) {
		Order groupedOrder = this.groupOrders(orderList);
		if(groupedOrder != null && groupedOrder.getQuantity() >= 2) {
			Product product = groupedOrder.getProduct();
			
			int discountedQuantity = Math.floorDiv(groupedOrder.getQuantity(), 2);
			BigDecimal discount = product.getPrice().multiply(BigDecimal.valueOf(discountedQuantity));
			return this.formOrderPromo(discountedQuantity, discount);
		}
		return null;
	}
}
