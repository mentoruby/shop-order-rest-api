package online.shopping;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import online.shopping.entity.Product;
import online.shopping.service.ProductRepository;

// @Component is registered in the context as a Spring bean
@Component
@Order(1)
public class ProductStartupRunner implements CommandLineRunner {
	protected final Log logger = LogFactory.getLog(getClass());
		
	@Autowired
	private ProductRepository productRepository;
	 
	@Override
	public void run(String... args) throws Exception {
		logger.debug("ProductStartupRunner run method calling");
		
		// Add new product sample - Banana
		Product banana = new Product("Banana", BigDecimal.valueOf(0.43), 50);
		this.productRepository.save(banana);
		
		for(Product p : this.productRepository.findAll()) {
			logger.info(p);
		}
	}
}
