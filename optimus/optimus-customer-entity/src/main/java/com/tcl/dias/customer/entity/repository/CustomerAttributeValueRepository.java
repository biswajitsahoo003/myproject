package com.tcl.dias.customer.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.Customer;
import com.tcl.dias.customer.entity.entities.CustomerAttributeValue;
import com.tcl.dias.customer.entity.entities.MstCustomerSpAttribute;

/**
 * This file contains the CustomerAttributeValueRepository.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface CustomerAttributeValueRepository extends JpaRepository<CustomerAttributeValue, Integer> {
	
	List<CustomerAttributeValue> findByCustomerAndMstCustomerSpAttribute(Customer customer,MstCustomerSpAttribute mstCustomerSpAttribute);
	
	List<CustomerAttributeValue> findByCustomerIdAndMstCustomerSpAttribute(Integer customerId,MstCustomerSpAttribute mstCustomerSpAttribute);

	List<CustomerAttributeValue> findByCustomer(Customer customer);

	List<CustomerAttributeValue> findByCustomerId(Integer customerId);

}
