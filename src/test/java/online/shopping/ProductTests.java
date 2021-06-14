package online.shopping;

import java.math.BigDecimal;

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

import online.shopping.entity.Product;

@AutoConfigureMockMvc
@SpringBootTest
public class ProductTests {
	@Autowired
    private MockMvc mvc;
    
    @Test
    public void getAllProductsAPI() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/products")
    	.accept(MediaType.APPLICATION_JSON))
    	.andExpect(MockMvcResultMatchers.status().isOk())
    	.andDo(MockMvcResultHandlers.print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$[*].id").isNotEmpty());
    }
     
    @Test
    public void getProductByIdAPI() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/product/{id}", 1)
    	.accept(MediaType.APPLICATION_JSON))
    	.andExpect(MockMvcResultMatchers.status().isOk())
    	.andDo(MockMvcResultHandlers.print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }
    
    @Test
    public void saveProduct() throws Exception {
    	Product testProduct = new Product();
    	testProduct.setName("ABC");
    	testProduct.setPrice(BigDecimal.valueOf(100));
   
    	String testContent = new ObjectMapper().writeValueAsString(testProduct);
   
    	mvc.perform(MockMvcRequestBuilders.post("/product/save")
		.content(testContent)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("ABC"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.price").value(100));
   }
}
