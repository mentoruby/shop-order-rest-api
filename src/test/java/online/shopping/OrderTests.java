package online.shopping;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import online.shopping.exception.NotFoundException;

@AutoConfigureMockMvc
@SpringBootTest
public class OrderTests {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
    private MockMvc mvc;
	
	@Test
	public void testNoOrder() throws Exception {
		
		Customer customer = new Customer(1L, "Customer A");
		
		OrderSummary orderSummary = new OrderSummary(Timestamp.from(ZonedDateTime.now().toInstant()), customer);
		
		String testContent = new ObjectMapper().writeValueAsString(orderSummary);
		
		mvc.perform(MockMvcRequestBuilders.post("/order/save")
		.contentType(MediaType.APPLICATION_JSON)
		.content(testContent)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(mvcResult -> {
			assertTrue(mvcResult.getResolvedException() instanceof NotFoundException);
			assertTrue(mvcResult.getResolvedException().getMessage().startsWith("Product Not Found"));
		});
	}
	
	@Test
	public void testSingleOrder() throws Exception {
		
		Product orange = new Product(2L, "Orange", BigDecimal.valueOf(0.25));
		Customer customer = new Customer(1L, "Customer A");
		
		OrderSummary orderSummary = new OrderSummary(Timestamp.from(ZonedDateTime.now().toInstant()), customer);
		Order orangeOrder = new Order(orange, 3);
		orderSummary.addOrder(orangeOrder);
		
		String testContent = new ObjectMapper().writeValueAsString(orderSummary);
		
		mvc.perform(MockMvcRequestBuilders.post("/order/save")
        .contentType(MediaType.APPLICATION_JSON)
        .content(testContent)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
		.andExpect(MockMvcResultMatchers.jsonPath("$.orderList.length()").value(1))
		.andExpect(MockMvcResultMatchers.jsonPath("$.orderList[0].id").isNotEmpty())
		.andExpect(MockMvcResultMatchers.jsonPath("$.originalCost").value(0.75))
		.andExpect(MockMvcResultMatchers.jsonPath("$.finalDiscount").value(0))
		.andExpect(MockMvcResultMatchers.jsonPath("$.finalCost").value(0.75))
		;
	}
	
	@Test
	public void testMultiOrders() throws Exception {
		
		Product apple = new Product(1L, "Apple", BigDecimal.valueOf(0.6));
		Product orange = new Product(2L, "Orange", BigDecimal.valueOf(0.25));
		Customer customer = new Customer(1L, "Customer A");
		
		OrderSummary orderSummary = new OrderSummary(Timestamp.from(ZonedDateTime.now().toInstant()), customer);
		Order appleOrder = new Order(apple, 10);
		Order orangeOrder = new Order(orange, 20);
		orderSummary.addOrder(appleOrder);
		orderSummary.addOrder(orangeOrder);
		
		String testContent = new ObjectMapper().writeValueAsString(orderSummary);
		
		mvc.perform(MockMvcRequestBuilders.post("/order/save")
        .contentType(MediaType.APPLICATION_JSON)
        .content(testContent)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
		.andExpect(MockMvcResultMatchers.jsonPath("$.orderList.length()").value(2))
		.andExpect(MockMvcResultMatchers.jsonPath("$.orderList[0].id").isNotEmpty())
		.andExpect(MockMvcResultMatchers.jsonPath("$.originalCost").value(11))
		.andExpect(MockMvcResultMatchers.jsonPath("$.finalDiscount").value(0))
		.andExpect(MockMvcResultMatchers.jsonPath("$.finalCost").value(11))
		;
   }
}
