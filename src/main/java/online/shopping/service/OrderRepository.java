package online.shopping.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import online.shopping.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
	
	@Query(nativeQuery = true, 
			value = "SELECT P.ID, NVL(OP.SOLD_CNT,0), NVL(P.QUANTITY-OP.SOLD_CNT,0) AS LEFT_CNT" + "\n"
			+ "FROM PRODUCT P" + "\n"
			+ "RIGHT JOIN (SELECT PRODUCT_ID, SUM(QUANTITY) AS SOLD_CNT FROM ORDER_TABLE GROUP BY PRODUCT_ID) OP" + "\n"
			+ "ON P.ID = OP.PRODUCT_ID;")
	public List<Object[]> findAllProductInventory();
}
