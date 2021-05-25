package com.tcl.dias.customer.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.AccountManager;

/**
 * 
 * This file contains the AccountManagerRepository.java class.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface AccountManagerRepository extends JpaRepository<AccountManager, Integer>{
	
	public List<AccountManager> findByErfCustCustomerIdAndErfUserUserId(Integer customerId,Integer userId);
}
