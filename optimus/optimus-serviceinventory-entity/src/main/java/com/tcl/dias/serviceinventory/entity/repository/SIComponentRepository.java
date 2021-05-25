package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.SIComponent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 *
 * This file contains the SIComponentRepository.java class for persistence in SIComponent
 *
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface SIComponentRepository extends JpaRepository<SIComponent, Integer> {
	
	public List<SIComponent> findBySiServiceDetailId(Integer serviceId);
	
	public SIComponent findFirstBySiServiceDetailId(Integer serviceId);
	
	public SIComponent findFirstBySiServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(Integer serviceId, String componentName,String siteType);

}
