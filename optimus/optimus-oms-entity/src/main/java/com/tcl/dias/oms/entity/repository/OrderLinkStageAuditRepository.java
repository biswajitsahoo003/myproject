package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstOrderLinkStage;
import com.tcl.dias.oms.entity.entities.OrderLinkStageAudit;
import com.tcl.dias.oms.entity.entities.OrderLinkStatusAudit;

/** 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 */
@Repository
public interface OrderLinkStageAuditRepository extends JpaRepository<OrderLinkStageAudit, Integer>  {
	
	List<OrderLinkStageAudit> findByMstOrderLinkStageAndOrderLinkStatusAuditAndIsActive(MstOrderLinkStage mstOrderLinkStage, OrderLinkStatusAudit orderSiteStatusAsudit, Byte active);

	List<OrderLinkStageAudit> findByOrderLinkStatusAudit(OrderLinkStatusAudit orderSiteStatusAsudit);
}
