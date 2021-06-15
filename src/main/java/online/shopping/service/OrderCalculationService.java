package online.shopping.service;

import java.math.BigDecimal;
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
	
	public void updateCostAndDiscount(OrderSummary orderSummary) {
		List<Order> orderList = orderSummary.getOrderList();
		
		BigDecimal originalCost = orderSummary.calculateOriginalCost();
		orderSummary.setOriginalCost(originalCost);
		
		BigDecimal finalDiscount = orderSummary.calculateFinalDiscount();
		orderSummary.setFinalDiscount(finalDiscount);
		
		BigDecimal finalCost = originalCost.add(finalDiscount.negate());
		if(finalCost.compareTo(BigDecimal.ZERO) < 0) {
			orderSummary.setFinalCost(BigDecimal.ZERO);
		} else {
			orderSummary.setFinalCost(finalCost);
		}
	}
	
	public void applyOrderPromotion(OrderSummary orderSummary) {
		List<Order> orderList = orderSummary.getOrderList();
		
		// no promotion
		if(orderList == null || orderList.isEmpty()) {
			updateCostAndDiscount(orderSummary);
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
		
		updateCostAndDiscount(orderSummary);
	}
}
