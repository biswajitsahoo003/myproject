package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.Stage;

/**
 * This file contains the StageRepository.java class.
 * 
 *
 * @author MAYANSHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface StageRepository extends JpaRepository<Stage, Integer> {
	
	public List<Stage> findBySiteDetailId(Integer siteDetailId);
	
	Stage findByServiceCodeAndMstStatus_code(String serviceCode,String status);
	
	Stage findFirstByServiceIdAndMstStageDef_keyOrderByIdDesc(Integer serviceId,String stageKey);
	
	public List<Stage> findByServiceId(Integer serviceId);
	
	
}
