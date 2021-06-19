package online.shopping;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hamcrest.Matchers;
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
import online.shopping.exception.OutOfStockException;

@AutoConfigureMockMvc
@SpringBootTest
public class OrderTests {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
    private MockMvc mvc;
	
	@Test
	public void saveNoOrder() throws Exception {
		
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
	public void saveSingleOrder() throws Exception {
		
		Product orange = new Product(2L, "Orange", BigDecimal.valueOf(0.25), 30);
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
	public void saveOutOfStockOrder() throws Exception {
		
		Product apple = new Product(1L, "Apple", BigDecimal.valueOf(0.25), 10);
		Customer customer = new Customer(1L, "Customer A");
		
		OrderSummary orderSummary = new OrderSummary(Timestamp.from(ZonedDateTime.now().toInstant()), customer);
		Order appleOrder = new Order(apple, 3);
		orderSummary.addOrder(appleOrder);
		
		String testContent = new ObjectMapper().writeValueAsString(orderSummary);
		
		mvc.perform(MockMvcRequestBuilders.post("/order/save")
        .contentType(MediaType.APPLICATION_JSON)
        .content(testContent)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(mvcResult -> {
			assertTrue(mvcResult.getResolvedException() instanceof OutOfStockException);
			assertTrue(mvcResult.getResolvedException().getMessage().startsWith("Product Is Out Of Stock"));
		});
		;
	}
	
	@Test
	public void saveMultiOrders() throws Exception {
		
		Product orange = new Product(2L, "Orange", BigDecimal.valueOf(0.25), 30);
		Product banana = new Product(3L, "Banana", BigDecimal.valueOf(0.43), 50);
		Customer customer = new Customer(1L, "Customer A");
		
		OrderSummary orderSummary = new OrderSummary(Timestamp.from(ZonedDateTime.now().toInstant()), customer);
		Order orangeOrder = new Order(orange, 2);
		Order bananaOrder = new Order(banana, 10);
		orderSummary.addOrder(orangeOrder);
		orderSummary.addOrder(bananaOrder);
		
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
		.andExpect(MockMvcResultMatchers.jsonPath("$.originalCost").value(4))
		.andExpect(MockMvcResultMatchers.jsonPath("$.finalDiscount").value(0))
		.andExpect(MockMvcResultMatchers.jsonPath("$.finalCost").value(4))
		;
	}
    
    @Test
    public void getAllOrders() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/orders")
    	.accept(MediaType.APPLICATION_JSON))
    	.andExpect(MockMvcResultMatchers.status().isOk())
    	.andDo(MockMvcResultHandlers.print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(Matchers.greaterThan(0)))
    	.andExpect(MockMvcResultMatchers.jsonPath("$[*].id").isNotEmpty())
    	.andExpect(MockMvcResultMatchers.jsonPath("$[*].customer.id").isNotEmpty())
    	.andExpect(MockMvcResultMatchers.jsonPath("$[*].orderList[*].id").isNotEmpty())
    	;
    }
    
    @Test
    public void getSingleOrder() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/order/{id}", 1)
    	.accept(MediaType.APPLICATION_JSON))
    	.andExpect(MockMvcResultMatchers.status().isOk())
    	.andDo(MockMvcResultHandlers.print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
    	.andExpect(MockMvcResultMatchers.jsonPath("$.customer.id").isNotEmpty())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.orderList[*].id").isNotEmpty())
    	;
    }
    
    @Test
    public void getNotExistOrder() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/order/{id}", 100L)
    	.accept(MediaType.APPLICATION_JSON))
    	.andExpect(MockMvcResultMatchers.status().isBadRequest())
    	.andDo(MockMvcResultHandlers.print())
		.andExpect(mvcResult -> {
			assertTrue(mvcResult.getResolvedException() instanceof NotFoundException);
			assertTrue(mvcResult.getResolvedException().getMessage().startsWith("Order Not Found"));
		});
    	;
    }
}
