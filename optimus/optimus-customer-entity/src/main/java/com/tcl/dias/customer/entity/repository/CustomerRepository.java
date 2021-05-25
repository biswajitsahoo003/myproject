package com.tcl.dias.customer.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.Customer;

/**
 * This is a repository for Customer Data
 * 
 * @author SEKHAR ER
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limi
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	
	public List<Customer> findByIdInOrderByCustomerNameAsc(List<Integer> customerIds);
	
	public List<Customer> findByStatus(Byte status);
	
	@Query (value ="SELECT * from customer where customer_name like :searchValue% ",nativeQuery =true)
	public List<Customer> findCustomerNameBySearch(@Param("searchValue") String searchValue);
	
	/**
	 * 
	 * Find all customer with pagination
	 * @param specification
	 * @param pageable
	 * @return
	 */
	Page<Customer> findAll(Specification<Customer> specification, Pageable pageable);

}
