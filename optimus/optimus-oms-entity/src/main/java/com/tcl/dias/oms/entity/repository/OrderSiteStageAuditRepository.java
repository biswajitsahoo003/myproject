package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstOrderSiteStage;
import com.tcl.dias.oms.entity.entities.OrderSiteStageAudit;
import com.tcl.dias.oms.entity.entities.OrderSiteStatusAudit;
/**
 * 
 * This file contains the OrderSiteStageAuditRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderSiteStageAuditRepository extends JpaRepository<OrderSiteStageAudit, Integer> {

	List<OrderSiteStageAudit> findByMstOrderSiteStageAndOrderSiteStatusAuditAndIsActive(
			MstOrderSiteStage mstOrderSiteStage, OrderSiteStatusAudit orderSiteStatusAsudit, Byte active);

	List<OrderSiteStageAudit> findByOrderSiteStatusAudit(OrderSiteStatusAudit orderSiteStatusAudit);
}
