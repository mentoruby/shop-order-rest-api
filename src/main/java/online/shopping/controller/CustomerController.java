package online.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import online.shopping.entity.Customer;
import online.shopping.exception.NotFoundException;
import online.shopping.service.CustomerRepository;

@RestController
public class CustomerController {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@RequestMapping("/customers")
	public List<Customer> list() {
		return this.customerRepository.findAll();
	}
	
	@RequestMapping("/customer/{id}")
	public Customer getCustomerById(@PathVariable("id") Long id) {
		return this.customerRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Customer Not Found [ID:%d]", id)));
	}
	
	@PostMapping("/customer/save") 
	public Customer save(@RequestBody Customer newCustomer) {
		return this.customerRepository.save(newCustomer);
	}
}
