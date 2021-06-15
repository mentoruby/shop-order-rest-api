package online.shopping;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import online.shopping.entity.Customer;
import online.shopping.entity.Order;
import online.shopping.entity.OrderSummary;
import online.shopping.entity.Product;

@AutoConfigureMockMvc
@SpringBootTest
public class PromoTests {
	@Autowired
    private MockMvc mvc;
	
	@Test
	public void applyNoPromotion() throws Exception {
		
		Product apple = new Product(1L, "Apple", BigDecimal.valueOf(0.6));
		
		Product orange = new Product(2L, "Orange", BigDecimal.valueOf(0.25));
		
		Customer customer = new Customer(1L, "Customer A");
		
		OrderSummary orderSummary = new OrderSummary(Timestamp.from(ZonedDateTime.now().toInstant()), customer);
		Order appleOrder = new Order(apple, 1);
		Order orangeOrder = new Order(orange, 1);
		orderSummary.addOrder(appleOrder);
		orderSummary.addOrder(orangeOrder);
		
		String testContent = new ObjectMapper().writeValueAsString(orderSummary);
		
		mvc.perform(MockMvcRequestBuilders.post("/order/promo/save")
        .contentType(MediaType.APPLICATION_JSON)
        .content(testContent)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.jsonPath("$.promoList").isEmpty())
		.andExpect(MockMvcResultMatchers.jsonPath("$.originalCost").value(0.85))
		.andExpect(MockMvcResultMatchers.jsonPath("$.finalDiscount").value(0))
		.andExpect(MockMvcResultMatchers.jsonPath("$.finalCost").value(0.85))
		;
	}
	
	@Test
	public void applySinglePromotion() throws Exception {
		
		Product apple = new Product(1L, "Apple", BigDecimal.valueOf(0.6));
		
		Product orange = new Product(2L, "Orange", BigDecimal.valueOf(0.25));
		
		Customer customer = new Customer(1L, "Customer A");
		
		OrderSummary orderSummary = new OrderSummary(Timestamp.from(ZonedDateTime.now().toInstant()), customer);
		Order appleOrder = new Order(apple, 1);
		Order orangeOrder = new Order(orange, 8);
		orderSummary.addOrder(appleOrder);
		orderSummary.addOrder(orangeOrder);
		
		String testContent = new ObjectMapper().writeValueAsString(orderSummary);
		
		mvc.perform(MockMvcRequestBuilders.post("/order/promo/save")
        .contentType(MediaType.APPLICATION_JSON)
        .content(testContent)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.jsonPath("$.promoList.length()").value(1))
		.andExpect(MockMvcResultMatchers.jsonPath("$.promoList[0].id").isNotEmpty())
		.andExpect(MockMvcResultMatchers.jsonPath("$.promoList[0].promoName").value("Buy 3 For 2 on Orange"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.promoList[0].quantity").value(2))
		.andExpect(MockMvcResultMatchers.jsonPath("$.promoList[0].discount").value(0.5))
		.andExpect(MockMvcResultMatchers.jsonPath("$.originalCost").value(2.6))
		.andExpect(MockMvcResultMatchers.jsonPath("$.finalDiscount").value(0.5))
		.andExpect(MockMvcResultMatchers.jsonPath("$.finalCost").value(2.1))
		;
	}
    
	@Test
	public void applyMultiPromotions() throws Exception {
		
		Product apple = new Product(1L, "Apple", BigDecimal.valueOf(0.6));
		
		Product orange = new Product(2L, "Orange", BigDecimal.valueOf(0.25));
		
		Customer customer = new Customer(1L, "Customer A");
		
		OrderSummary orderSummary = new OrderSummary(Timestamp.from(ZonedDateTime.now().toInstant()), customer);
		Order appleOrder = new Order(apple, 10);
		Order orangeOrder = new Order(orange, 20);
		orderSummary.addOrder(appleOrder);
		orderSummary.addOrder(orangeOrder);
		
		String testContent = new ObjectMapper().writeValueAsString(orderSummary);
		
		mvc.perform(MockMvcRequestBuilders.post("/order/promo/save")
        .contentType(MediaType.APPLICATION_JSON)
        .content(testContent)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.jsonPath("$.promoList.length()").value(2))
		.andExpect(MockMvcResultMatchers.jsonPath("$.promoList[0].id").isNotEmpty())
		.andExpect(MockMvcResultMatchers.jsonPath("$.promoList[0].promoName").value("Buy 1 Get 1 Free on Apple"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.promoList[0].quantity").value(5))
		.andExpect(MockMvcResultMatchers.jsonPath("$.promoList[0].discount").value(3))
		.andExpect(MockMvcResultMatchers.jsonPath("$.promoList[1].id").isNotEmpty())
		.andExpect(MockMvcResultMatchers.jsonPath("$.promoList[1].promoName").value("Buy 3 For 2 on Orange"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.promoList[1].quantity").value(6))
		.andExpect(MockMvcResultMatchers.jsonPath("$.promoList[1].discount").value(1.5))
		.andExpect(MockMvcResultMatchers.jsonPath("$.originalCost").value(11))
		.andExpect(MockMvcResultMatchers.jsonPath("$.finalDiscount").value(4.5))
		.andExpect(MockMvcResultMatchers.jsonPath("$.finalCost").value(6.5))
		;
   }
}
