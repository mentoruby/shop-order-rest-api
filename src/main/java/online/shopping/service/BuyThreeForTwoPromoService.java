package online.shopping.service;

import java.math.BigDecimal;
import java.util.List;

import online.shopping.entity.Order;
import online.shopping.entity.OrderPromo;
import online.shopping.entity.Product;

public class BuyThreeForTwoPromoService extends PromotionServiceImpl {
	
	public BuyThreeForTwoPromoService(String productName) {
		super(String.format("Buy 3 For 2 on %s", productName), productName);
	}

	@Override
	public OrderPromo findOrderPromo(List<Order> orderList) {
		Order groupedOrder = this.groupOrders(orderList);
		if(groupedOrder != null && groupedOrder.getQuantity() >= 3) {
			Product product = groupedOrder.getProduct();
			
			int discountedQuantity = Math.floorDiv(groupedOrder.getQuantity(), 3);
			BigDecimal discount = product.getPrice().multiply(BigDecimal.valueOf(discountedQuantity));
			return new OrderPromo(this.promoName, discountedQuantity, discount);
		}
		return null;
	}
}
