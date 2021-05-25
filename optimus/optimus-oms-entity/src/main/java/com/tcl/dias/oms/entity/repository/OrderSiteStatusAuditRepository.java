package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstOrderSiteStatus;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderSiteStatusAudit;

@Repository
public interface OrderSiteStatusAuditRepository extends JpaRepository<OrderSiteStatusAudit, Integer> {

	List<OrderSiteStatusAudit> findByOrderIllSiteAndMstOrderSiteStatusAndIsActive(OrderIllSite orderIllSite,
			MstOrderSiteStatus mstOrderSiteStatus, Byte active);
	
	List<OrderSiteStatusAudit> findByOrderIllSiteAndIsActive(OrderIllSite orderIllsite, Byte active);

	List<OrderSiteStatusAudit> findByOrderIllSite(OrderIllSite orderIllSite);
}
