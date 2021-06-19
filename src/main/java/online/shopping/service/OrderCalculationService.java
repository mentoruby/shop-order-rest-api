package online.shopping.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import online.shopping.entity.Order;
import online.shopping.entity.OrderPromo;
import online.shopping.entity.OrderSummary;

@Service
public class OrderCalculationService {
	
	private List<PromotionService> promoServices;
	
	public OrderCalculationService() {
		promoServices = new ArrayList<>();
		promoServices.add(new BuyOneGetOneFreePromoService("Apple"));
		promoServices.add(new BuyThreeForTwoPromoService("Orange"));
	}
	
	public void calculateCostAndDiscount(OrderSummary orderSummary) {
		orderSummary.calculateOriginalCost();
		
		orderSummary.calculateFinalDiscount();
		
		orderSummary.calculateFinalCost();
	}
	
	public void applyOrderPromotion(OrderSummary orderSummary) {
		List<Order> orderList = orderSummary.getOrderList();
		
		// no promotion
		if(orderList == null || orderList.isEmpty()) {
			calculateCostAndDiscount(orderSummary);
			return;
		}
		
		// check and apply promotion one by one
		if(promoServices != null) {
			for(PromotionService ps : promoServices) {
				OrderPromo promo = ps.findOrderPromo(orderList);
				if(promo != null) {
					orderSummary.addPromo(promo);
					orderList = ps.getUnrelatedOrderList(orderList);
				}
			}
		}
		
		calculateCostAndDiscount(orderSummary);
	}
}
