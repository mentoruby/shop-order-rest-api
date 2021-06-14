package online.shopping;

import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import online.shopping.entity.Customer;
import online.shopping.entity.Order;
import online.shopping.entity.OrderSummary;
import online.shopping.entity.Product;
import online.shopping.service.CustomerRepository;
import online.shopping.service.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {CustomerRepository.class, ProductRepository.class})
public class OrderTests {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
    private MockMvc mvc;
	
	@MockBean
    private CustomerRepository customerRepository;
	
	@MockBean
    private ProductRepository productRepository;
	
	@Test
	public void saveOrderSummary() throws Exception {
		
		Product apple = new Product(1L, "Apple", BigDecimal.valueOf(0.6));
		doReturn(Optional.of(apple)).when(productRepository).findById(1L);
		
		Product orange = new Product(2L, "Orange", BigDecimal.valueOf(0.25));
		doReturn(Optional.of(orange)).when(productRepository).findById(2L);
		
		Customer customer = new Customer(1L, "Customer A");
		doReturn(Optional.of(customer)).when(customerRepository).findById(1L);
		
		OrderSummary orderSummary = new OrderSummary(Timestamp.from(ZonedDateTime.now().toInstant()), customer);
		Order appleOrder = new Order(apple, 10);
		Order orangeOrder = new Order(orange, 20);
		orderSummary.addOrder(appleOrder);
		orderSummary.addOrder(orangeOrder);
		
		String testContent = new ObjectMapper().writeValueAsString(orderSummary);
		logger.info(testContent);
		
		mvc.perform(MockMvcRequestBuilders.post("/order/save")
        .contentType(MediaType.APPLICATION_JSON)
        .content(testContent))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
   }
}
