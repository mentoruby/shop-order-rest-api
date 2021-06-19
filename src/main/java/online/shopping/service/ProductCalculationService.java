package online.shopping.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import online.shopping.entity.Order;
import online.shopping.entity.Product;
import online.shopping.entity.ProductInventory;
import online.shopping.exception.NotFoundException;
import online.shopping.exception.OutOfStockException;

@Service
public class ProductCalculationService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	public void refreshProductDetails(List<Order> orderList) {
		for(Order order : orderList) {
			Long productId = order.getProduct().getId();
			if(productId == null) {
				throw new NotFoundException(String.format("Product ID Must Be Provided [ID:%d]", productId));
			}
			Product product = this.productRepository.findById(productId)
					.orElseThrow(() -> new NotFoundException(String.format("Product Not Found [ID:%d]", productId)));
			order.setProduct(product);
		}
	}
	
	private HashMap<Long, ProductInventory> getInventoryMap() {
		List<Object[]> objectList = this.orderRepository.findAllProductInventory();
		HashMap<Long, ProductInventory> inventoryMap = new HashMap<>();
		
		for(Object[] objArray : objectList) {
			ProductInventory inventory = new ProductInventory();
			Long productId = Long.parseLong(objArray[0].toString());
			inventory.setQuantitySold(Integer.parseInt(objArray[1].toString()));
			inventory.setQuantityLeft(Integer.parseInt(objArray[2].toString()));
			inventoryMap.put(productId, inventory);
		}
		
		return inventoryMap;
	}
	
	public List<Product> listInventory() {
		List<Product> productList = this.productRepository.findAll();
		HashMap<Long, ProductInventory> inventoryMap = this.getInventoryMap();
		
		for(Product product : productList) {
			ProductInventory inventory = inventoryMap.get(product.getId());
			if(inventory == null) {
				product.setInventory(new ProductInventory());
			} else {
				product.setInventory(inventory);
			}
		}
		
		return productList;
	}
	
	public void checkProductStock(List<Order> orderList) {
		HashMap<Long, ProductInventory> inventoryMap = this.getInventoryMap();
		for (Order order:orderList) {
			ProductInventory inventory = inventoryMap.get(order.getProduct().getId());
			if(inventory == null) {
				throw new NotFoundException(String.format("Product Does Not Exist [ID:%d]", order.getProduct().getId()));
			} else {
				if(inventory.getQuantityLeft() < order.getQuantity()) {
					throw new OutOfStockException(String.format("Product Is Out Of Stock [ID:%d, left:%d, buy:%d]", order.getProduct().getId(), inventory.getQuantityLeft(), order.getQuantity()));
				}
			}
		}
	}
}
