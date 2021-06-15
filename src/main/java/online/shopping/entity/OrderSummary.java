package online.shopping.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class OrderSummary {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Timestamp orderTime;
	
	@OneToOne
    @JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "orderSummary", cascade = CascadeType.ALL)
	private List<Order> orderList;
	
	// Limitation: Hibernate doesn't allow more than one list used with EAGER fetch type, so use Set instead of List
	// MultipleBagFetchException: cannot simultaneously fetch multiple bags
	@OneToMany(mappedBy = "orderSummary", cascade = CascadeType.ALL)
	private List<OrderPromo> promoList;
	
	@Column
	private BigDecimal originalCost;
	
	@Column
	private BigDecimal finalDiscount;
	
	@Column
	private BigDecimal finalCost;
	
	public OrderSummary() {
		
	}
	
	public OrderSummary(Timestamp orderTime, Customer customer) {
		this.orderTime = orderTime;
		this.customer = customer;
	}
	
	public OrderSummary(Long id, Timestamp orderTime, Customer customer) {
		this.id = id;
		this.orderTime = orderTime;
		this.customer = customer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
		if(this.orderList != null) {
			for(Order order : orderList) {
				order.setOrderSummary(this);
			}
		}
	}

	public List<OrderPromo> getPromoList() {
		return promoList;
	}

	public void setPromoList(List<OrderPromo> promoList) {
		this.promoList = promoList;
		if(this.promoList != null) {
			for(OrderPromo promo : promoList) {
				promo.setOrderSummary(this);
			}
		}
	}

	public BigDecimal getOriginalCost() {
		return originalCost;
	}

	public void setOriginalCost(BigDecimal originalCost) {
		this.originalCost = originalCost;
	}

	public BigDecimal getFinalDiscount() {
		return finalDiscount;
	}

	public void setFinalDiscount(BigDecimal finalDiscount) {
		this.finalDiscount = finalDiscount;
	}

	public BigDecimal getFinalCost() {
		return finalCost;
	}

	public void setFinalCost(BigDecimal finalCost) {
		this.finalCost = finalCost;
	}
	
	public void addOrder(Order order) {
		if(this.orderList == null) {
			this.orderList = new ArrayList<>();
		}
		order.setOrderSummary(this);
		this.orderList.add(order);
	}
	
	public void addPromo(OrderPromo promo) {
		if(this.promoList == null) {
			this.promoList = new ArrayList<>();
		}
		promo.setOrderSummary(this);
		this.promoList.add(promo);
	}
	
	public BigDecimal calculateOriginalCost() {
		BigDecimal cost = BigDecimal.ZERO;
		if(this.orderList != null) {
			for(Order order : this.orderList) {
				cost = cost.add(order.calculateCost());
			}
		}
		return cost;
	}
	
	public BigDecimal calculateFinalDiscount() {
		BigDecimal discount = BigDecimal.ZERO;
		if(this.promoList != null) {
			for(OrderPromo promo : this.promoList) {
				discount = discount.add(promo.getDiscount());
			}
		}
		return discount;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("OrderSummary [");
		sb.append("id=").append(id);
		sb.append(", orderTime=").append(orderTime);
		
		if(customer != null) {
			sb.append(", customer_id=").append(customer.getId());
		}
		else {
			sb.append(", customer=null");
		}
		
		sb.append(", originalCost=").append(originalCost);
		sb.append(", finalDiscount=").append(finalDiscount);
		sb.append(", finalCost=").append(finalCost);
		
		if(orderList != null && !orderList.isEmpty()) {
			for(Order order : orderList) {
				sb.append(", order_id=").append(order.getId());
			}
		}
		else {
			sb.append(", orderList=null");
		}
		
		if(promoList != null && !promoList.isEmpty()) {
			for(OrderPromo promo : promoList) {
				sb.append(", promo_id=").append(promo.getId());
			}
		}
		else {
			sb.append(", promoList=null");
		}
		
		sb.append("]");
		return sb.toString();
	}
}
