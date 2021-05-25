package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.VwProductSlaSpecIAS;
/**
 * 
 * This file contains the VwProductSlaSpecIASRepository.java class.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface VwProductSlaSpecIASRepository extends JpaRepository<VwProductSlaSpecIAS, Integer>{
	
	List<VwProductSlaSpecIAS> findBySltVarientAndSlaMetricId(String slaVariant,Integer metricId);

}
