package online.shopping.service;

import org.springframework.data.jpa.repository.JpaRepository;

import online.shopping.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

}
