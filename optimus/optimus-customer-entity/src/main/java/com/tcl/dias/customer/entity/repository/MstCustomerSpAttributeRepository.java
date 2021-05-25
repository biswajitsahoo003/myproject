package com.tcl.dias.customer.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.MstCustomerSpAttribute;

/**
 * This file contains the MstCustomerSpAttributeRepository.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstCustomerSpAttributeRepository extends JpaRepository<MstCustomerSpAttribute, Integer> {
	
	List<MstCustomerSpAttribute> findByNameInAndStatus(List<String> names,byte status);
	
	MstCustomerSpAttribute findByNameAndStatus(String names,byte status);

	
	

}
