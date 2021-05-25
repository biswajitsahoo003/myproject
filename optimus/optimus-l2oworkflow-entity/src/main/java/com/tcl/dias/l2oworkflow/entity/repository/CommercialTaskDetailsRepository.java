package com.tcl.dias.l2oworkflow.entity.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.CommercialTaskDetails;

/**
 * 
 * This file contains the CommercialTaskDetailsRepository.java class.
 * 
 *
 * @author Muthuselvi S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface CommercialTaskDetailsRepository extends JpaRepository<CommercialTaskDetails, Integer> {
	
}
