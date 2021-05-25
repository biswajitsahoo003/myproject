package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrOrder;
import com.tcl.dias.oms.entity.entities.OdrSolutionComponent;
/**
 * 
 * This file contains the OdrSolutionComponentRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OdrSolutionComponentRepository extends JpaRepository<OdrSolutionComponent, Integer> {
	
	List<OdrSolutionComponent> findByOdrOrder(OdrOrder odrOrder);
}
