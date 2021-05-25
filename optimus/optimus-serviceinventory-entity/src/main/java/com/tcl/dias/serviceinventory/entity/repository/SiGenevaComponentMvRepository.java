package com.tcl.dias.serviceinventory.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.SiGenevaComponentMv;

/**
 * SiGenevaComponentMv interface 
 * @author archchan
 *
 */
@Repository
public interface SiGenevaComponentMvRepository extends JpaRepository<SiGenevaComponentMv,Integer>{
	
	@Query(value = "select comp.SERVICE_ID as serviceId,comp.ARC as arc , comp.ONNET_ARC as onnetArc, comp.CURRENCY as currency \r\n" + 
			"from si_geneva_component_mv comp\r\n" + 
			"where comp.STATUS = 'Active' and  comp.SERVICE_TYPE ='NDE' and comp.PRODUCT_FLAVOUR='GDE' \r\n" + 
			"and comp.PRODUCT_NAME = 'Global Dedicated Ethernet' AND comp.SERVICE_ID = :serviceId ", nativeQuery = true)
	List<Map<String,Object>> findByServiceIdAndStatusAndProduct(String serviceId);

}
