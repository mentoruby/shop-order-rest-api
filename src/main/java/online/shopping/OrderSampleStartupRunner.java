package online.shopping;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import online.shopping.entity.Customer;
import online.shopping.entity.Order;
import online.shopping.entity.OrderPromo;
import online.shopping.entity.OrderSummary;
import online.shopping.entity.Product;
import online.shopping.service.CustomerRepository;
import online.shopping.service.OrderCalculationService;
import online.shopping.service.OrderSummaryRepository;
import online.shopping.service.ProductRepository;

@Component
public class OrderSampleStartupRunner implements CommandLineRunner {
	protected final Log logger = LogFactory.getLog(getClass());
		
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderSummaryRepository orderSummaryRepository;
	
	@Autowired
	private OrderCalculationService orderCalculationService;
	 
	@Override
	public void run(String... args) throws Exception {
		logger.debug("OrderSampleStartupRunner run method calling");
		
		insertSampleOrder1();
		insertSampleOrder2();
	}
	
	private void printOrderDetails(OrderSummary sampleOrderSummary) {
		logger.info("Sample "+sampleOrderSummary);
		for(Order order : sampleOrderSummary.getOrderList()) {
			logger.info("Sample "+order);
		}
		List<OrderPromo> promoList = sampleOrderSummary.getPromoList();
		if(promoList != null) {
			for(OrderPromo promo : promoList) {
				logger.info("Sample "+promo);
			}
		}
	}
	
	private void insertSampleOrder1() {
		Product apple = productRepository.queryFirstByName("Apple");
		logger.info("Apple "+apple);
		
		Product orange = productRepository.queryFirstByName("Orange");
		logger.info("Orange "+orange);
		
		Customer sampleCustomer = new Customer("Customer A");
		sampleCustomer = customerRepository.save(sampleCustomer);
		logger.info("Sample "+sampleCustomer);
		
		OrderSummary sampleOrderSummary = new OrderSummary(Timestamp.from(ZonedDateTime.now().toInstant()), sampleCustomer);
		Order sampleOrder1 = new Order(apple, 10);
		Order sampleOrder2 = new Order(orange, 20);
		sampleOrderSummary.addOrder(sampleOrder1);
		sampleOrderSummary.addOrder(sampleOrder2);
		
		orderCalculationService.applyOrderPromotion(sampleOrderSummary);
		
		sampleOrderSummary = orderSummaryRepository.save(sampleOrderSummary);
		
		printOrderDetails(sampleOrderSummary);
	}
	
	private void insertSampleOrder2() {
		Product banana = new Product("Banana", BigDecimal.valueOf(0.43));
		banana = productRepository.save(banana);
		logger.info("Banana "+banana);
	
		Customer sampleCustomer = customerRepository.findById(1L).get();
		logger.info("Sample "+sampleCustomer);
		
		OrderSummary sampleOrderSummary = new OrderSummary(Timestamp.from(ZonedDateTime.now().toInstant()), sampleCustomer);
		Order sampleOrder1 = new Order(banana, 5);
		sampleOrderSummary.addOrder(sampleOrder1);
		
		orderCalculationService.updateCostAndDiscount(sampleOrderSummary);
		
		sampleOrderSummary = orderSummaryRepository.save(sampleOrderSummary);
		
		printOrderDetails(sampleOrderSummary);
	}
}
