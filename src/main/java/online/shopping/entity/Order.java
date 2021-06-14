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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Proxy(lazy = false)
@Table(name = "order_table")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
    @JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "order_summary_id", nullable = false)
	private OrderSummary orderSummary;
	
	@Column
	private int quantity;
	
	public Order() {
		
	}
	
	public Order(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}
	
	public Order(Long id, Product product, int quantity) {
		this.id = id;
		this.product = product;
		this.quantity = quantity;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
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
	
	public BigDecimal calculateCost() {
		return product.getPrice().multiply(BigDecimal.valueOf(quantity));
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Order [");
		sb.append("id=").append(id);
		sb.append(", quantity=").append(quantity);
		if(product != null) {
			sb.append(", product_id=").append(product.getId());
		}
		else {
			sb.append(", product=null");
		}
		if(orderSummary != null) {
			sb.append(", order_summary_id=").append(orderSummary.getId());
		}
		else {
			sb.append(", orderSummary=null");
		}
		sb.append("]");
		return sb.toString();
	}
}
