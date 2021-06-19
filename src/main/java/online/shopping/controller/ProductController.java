package online.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import online.shopping.entity.Product;
import online.shopping.exception.NotFoundException;
import online.shopping.service.ProductCalculationService;
import online.shopping.service.ProductRepository;

@RestController
public class ProductController {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductCalculationService productCalculationService;
	
	@RequestMapping("/products")
	public List<Product> list() {
		return this.productRepository.findAll();
	}
	
	@RequestMapping("/products/inventory")
	public List<Product> listInventory() {
		return this.productCalculationService.listInventory();
	}
	
	@RequestMapping("/product/{id}")
	public Product getProductById(@PathVariable("id") Long id) {
		return this.productRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Product Not Found [ID:%d]", id)));
	}
	
	@PostMapping("/product/save") 
	public Product save(@RequestBody Product newProduct) {
		return this.productRepository.save(newProduct);
	}
}
