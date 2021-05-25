package com.tcl.dias.customer.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.CustomerLeContact;
import com.tcl.dias.customer.entity.entities.CustomerLeCountry;

/**
 * 
 * Repository Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
/**
 * 
 */
@Repository
public interface CustomerLeCountryRepository extends JpaRepository<CustomerLeCountry, Integer> {

	List<CustomerLeContact> findByMstCountryAndCustomerLegalEntity_IdIn(String mstCountry,
			List<Integer> customerLeIds);
	
	List<CustomerLeCountry> findByCustomerLegalEntityId(Integer customerLeId);

}
