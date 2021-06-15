package online.shopping.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Proxy(lazy = false)
public class OrderPromo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String promoName;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "order_summary_id", nullable = false)
	private OrderSummary orderSummary;
	
	@Column
	private int quantity;
	
	@Column
	private BigDecimal discount;
	
	public OrderPromo() {
		
	}
	
	public OrderPromo(String promoName, int quantity, BigDecimal discount) {
		this.promoName = promoName;
		this.quantity = quantity;
		this.discount = discount;
	}
	
	public OrderPromo(Long id, String promoName, int quantity, BigDecimal discount) {
		this.id = id;
		this.promoName = promoName;
		this.quantity = quantity;
		this.discount = discount;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getPromoName() {
		return promoName;
	}

	public void setPromoName(String promoName) {
		this.promoName = promoName;
	}

	@JsonIgnore
	public OrderSummary getOrderSummary() {
		return orderSummary;
	}

	public void setOrderSummary(OrderSummary orderSummary) {
		this.orderSummary = orderSummary;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("OrderPromo [");
		sb.append("id=").append(id);
		sb.append(", promoName=").append(promoName);
		if(orderSummary != null) {
			sb.append(", order_summary_id=").append(orderSummary.getId());
		}
		else {
			sb.append(", orderSummary=null");
		}
		sb.append(", quantity=").append(quantity);
		sb.append(", discount=").append(discount);
		sb.append("]");
		return sb.toString();
	}
}
