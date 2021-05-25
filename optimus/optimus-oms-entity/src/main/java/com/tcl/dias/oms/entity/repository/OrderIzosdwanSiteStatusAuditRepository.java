package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstOrderSiteStatus;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSite;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSiteStatusAudit;

/**
 * 
 * This file contains the OrderIzosdwanSiteStatusAuditRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderIzosdwanSiteStatusAuditRepository extends JpaRepository<OrderIzosdwanSiteStatusAudit, Integer>{

	List<OrderIzosdwanSiteStatusAudit> findByOrderIzosdwanSiteAndMstOrderSiteStatusAndIsActive(OrderIzosdwanSite orderIllSite,
			MstOrderSiteStatus mstOrderSiteStatus, Byte active);
	
	List<OrderIzosdwanSiteStatusAudit> findByOrderIzosdwanSiteAndIsActive(OrderIzosdwanSite orderIllsite, Byte active);

	List<OrderIzosdwanSiteStatusAudit> findByOrderIzosdwanSite(OrderIzosdwanSite orderIllSite);
}
