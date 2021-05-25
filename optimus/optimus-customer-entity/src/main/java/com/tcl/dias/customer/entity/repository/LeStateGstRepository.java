package com.tcl.dias.customer.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.CustomerLegalEntity;
import com.tcl.dias.customer.entity.entities.LeStateGst;

/**
 * 
 * Repo Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface LeStateGstRepository extends JpaRepository<LeStateGst, Integer> {

	List<LeStateGst> findByCustomerLegalEntity(CustomerLegalEntity customerLegalEntity);

	List<LeStateGst> findByCustomerLegalEntityAndState(CustomerLegalEntity customerLegalEntity,String state);

	LeStateGst findByCustomerLegalEntityAndGstNo(CustomerLegalEntity customerLegalEntity,String gstNo);

	LeStateGst findByGstNoAndCustomerLegalEntityAndStateAndIsActive(String gstNo,
			CustomerLegalEntity customerLegalEntity, String state, String isActive);
	
	List<LeStateGst> findByCustomerLegalEntityAndIsActive(CustomerLegalEntity customerLegalEntity,String isActive);

}
