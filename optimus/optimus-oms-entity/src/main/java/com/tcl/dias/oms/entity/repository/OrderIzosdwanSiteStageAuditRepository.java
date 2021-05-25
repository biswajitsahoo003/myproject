package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstOrderSiteStage;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSiteStageAudit;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSiteStatusAudit;
import com.tcl.dias.oms.entity.entities.OrderSiteStageAudit;
import com.tcl.dias.oms.entity.entities.OrderSiteStatusAudit;

/**
 * 
 * This file contains the OrderIzosdwanSiteStageAuditRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderIzosdwanSiteStageAuditRepository extends JpaRepository<OrderIzosdwanSiteStageAudit, Integer>{

	List<OrderIzosdwanSiteStageAudit> findByMstOrderSiteStageAndOrderIzosdwanSiteStatusAuditAndIsActive(
			MstOrderSiteStage mstOrderSiteStage, OrderIzosdwanSiteStatusAudit orderSiteStatusAsudit, Byte active);

	List<OrderIzosdwanSiteStageAudit> findByOrderIzosdwanSiteStatusAudit(OrderIzosdwanSiteStatusAudit orderSiteStatusAudit);
}
