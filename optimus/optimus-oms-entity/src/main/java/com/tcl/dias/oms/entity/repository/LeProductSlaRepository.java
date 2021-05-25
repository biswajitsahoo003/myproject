package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.oms.entity.entities.LeProductSla;
import com.tcl.dias.oms.entity.entities.MstProductFamily;

/**
 * This file contains the LeProductSlaRepository.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface LeProductSlaRepository extends JpaRepository<LeProductSla, Integer> {

	List<LeProductSla> findByErfCustomerLeIdAndMstProductFamily(Integer erfCustomerLeId, MstProductFamily mstProductFamily);

	List<LeProductSla> findByErfCustomerIdAndMstProductFamily(Integer erfCustomerId, MstProductFamily mstProductFamily);

}
