package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrComponent;
import com.tcl.dias.oms.entity.entities.OdrServiceDetail;

/**
 * 
 * This file contains the OdrComponentRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OdrComponentRepository extends JpaRepository<OdrComponent, Integer>{
	
	public List<OdrComponent> findByOdrServiceDetailAndComponentNameAndSiteType(OdrServiceDetail odrServiceDetail,String name,String siteType);
	
	public List<OdrComponent> findByOdrServiceDetail(OdrServiceDetail odrServiceDetail);
}
