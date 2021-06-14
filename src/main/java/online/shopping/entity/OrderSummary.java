package online.shopping.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Proxy;

@Entity
@Proxy(lazy = false)
public class OrderSummary {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Timestamp orderTime;
	
	@OneToOne
    @JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "orderSummary", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Order> orderList;
	
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
		this.orderList.add(order);
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
		
		sb.append("]");
		return sb.toString();
	}
}
