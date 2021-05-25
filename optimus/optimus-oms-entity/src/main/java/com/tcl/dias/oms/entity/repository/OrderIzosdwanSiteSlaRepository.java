package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteSla;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSite;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSiteSla;

/**
 * 
 * This file contains the OrderIzosdwanSiteSlaRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderIzosdwanSiteSlaRepository extends JpaRepository<OrderIzosdwanSiteSla, Integer>{

	List<OrderIzosdwanSiteSla> findByOrderIzosdwanSite(OrderIzosdwanSite orderIllSite);
	
	@Query(value="SELECT sm.sla_name ,osa.sla_value FROM order_izosdwan_site_sla osa ,sla_master sm where sm.id=osa.sla_master_id and osa.izosdwan_site_id=:siteId",nativeQuery=true)
	List<Map<String,String>> findByOrderIzosdwanSiteId(@Param("siteId") Integer siteId);

}
