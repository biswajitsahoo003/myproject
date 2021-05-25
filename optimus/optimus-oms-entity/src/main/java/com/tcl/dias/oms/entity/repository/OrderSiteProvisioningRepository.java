package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderSiteProvisionAudit;

/**
 * This class is order confirmation audit repository
 * 
 * @author SEKHAR ER
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderSiteProvisioningRepository extends JpaRepository<OrderSiteProvisionAudit, Integer> {
	
	List<OrderSiteProvisionAudit> findBySiteUuid(String siteId);

}
