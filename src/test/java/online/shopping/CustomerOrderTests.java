package online.shopping;

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

@AutoConfigureMockMvc
@SpringBootTest
public class CustomerOrderTests {
	@Autowired
    private MockMvc mvc;
    
    @Test
    public void getOrdersOfSingleCustomer() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/customer/{id}/orders", 1L)
    	.accept(MediaType.APPLICATION_JSON))
    	.andExpect(MockMvcResultMatchers.status().isOk())
    	.andDo(MockMvcResultHandlers.print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(Matchers.greaterThan(0)))
    	.andExpect(MockMvcResultMatchers.jsonPath("$[*].id").isNotEmpty())
    	.andExpect(MockMvcResultMatchers.jsonPath("$[*].customer.id").value(Matchers.everyItem(Matchers.is(1))))
    	.andExpect(MockMvcResultMatchers.jsonPath("$[*].orderList[*].id").isNotEmpty())
    	;
    }
    
    @Test
    public void getOrdersOfNotExistCustomer() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/customer/{id}/orders", 100L)
    	.accept(MediaType.APPLICATION_JSON))
    	.andExpect(MockMvcResultMatchers.status().isOk())
    	.andDo(MockMvcResultHandlers.print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0))
    	;
    }
}
