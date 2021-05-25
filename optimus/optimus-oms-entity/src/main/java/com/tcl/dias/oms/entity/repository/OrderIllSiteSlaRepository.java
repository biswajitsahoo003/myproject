package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteSla;

/**
 * This file contains the OrderIllSiteSla.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface OrderIllSiteSlaRepository extends JpaRepository<OrderIllSiteSla, Integer> {

	List<OrderIllSiteSla> findByOrderIllSite(OrderIllSite orderIllSite);
	
	@Query(value="SELECT sm.sla_name ,osa.sla_value FROM order_ill_sites_sla osa ,sla_master sm where sm.id=osa.sla_master_id and osa.ill_site_id=:siteId",nativeQuery=true)
	List<Map<String,String>> findByOrderIllSiteId(@Param("siteId") Integer siteId);

}
