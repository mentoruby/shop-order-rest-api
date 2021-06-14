package online.shopping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import online.shopping.entity.Product;
import online.shopping.service.ProductRepository;

// @Component is registered in the context as a Spring bean
@Component
public class ProductStartupRunner implements CommandLineRunner {
	protected final Log logger = LogFactory.getLog(getClass());
		
	@Autowired
	private ProductRepository productRepository;
	 
	@Override
	public void run(String... args) throws Exception {
		logger.debug("ProductStartupRunner run method calling");
		for(Product p : this.productRepository.findAll()) {
			logger.info(p);
		}
	}
}
