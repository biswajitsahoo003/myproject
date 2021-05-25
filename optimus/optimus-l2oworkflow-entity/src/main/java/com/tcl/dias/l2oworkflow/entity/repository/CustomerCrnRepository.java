package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.CustomerCrn;

/**
 * 
 * Repository class for CustomerCrn - for Auditing entries like account number,sap code 
 * 
 *
 * @author yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface CustomerCrnRepository extends JpaRepository<CustomerCrn, Integer>{

	CustomerCrn findByCustomerRefAndCustomerLegalIdAndState(String customerRef,Integer custLegalId, String State);
	
	List<CustomerCrn> findByCustomerRef(String customerRef);
}
