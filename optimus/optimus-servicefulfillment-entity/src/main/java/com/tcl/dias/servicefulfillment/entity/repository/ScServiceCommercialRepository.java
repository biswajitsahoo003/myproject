package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ScServiceCommercial;

/**
 * 
 * This file contains the ScServiceCommercialRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface ScServiceCommercialRepository extends JpaRepository<ScServiceCommercial, Integer> {
	
	ScServiceCommercial findByServiceIdAndReferenceName(String serviceCode,String referenceName);
	ScServiceCommercial findFirstByServiceIdAndReferenceNameOrderByIdDesc(String serviceCode,String referenceName);
	
	List<ScServiceCommercial> findByServiceIdAndComponentReferenceName(String serviceCode,String componentReferenceName);
	
	List<ScServiceCommercial> findByServiceIdAndReferenceType(String serviceCode,String refType);
	
	List<ScServiceCommercial> findByScServiceId(Integer scServiceId);
	
	List<ScServiceCommercial> findByScServiceIdAndComponentId(Integer serviceId, Integer scComponentId);
}
