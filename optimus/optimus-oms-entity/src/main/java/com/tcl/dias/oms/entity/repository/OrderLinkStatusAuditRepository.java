package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstOrderLinkStatus;
import com.tcl.dias.oms.entity.entities.OrderLinkStatusAudit;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
/** 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 */

@Repository
public interface OrderLinkStatusAuditRepository extends JpaRepository<OrderLinkStatusAudit, Integer> {
	List<OrderLinkStatusAudit> findByOrderNplLinkAndMstOrderLinkStatusAndIsActive(OrderNplLink orderNplLink,
			MstOrderLinkStatus mstOrderSiteStatus, Byte active);
	
	List<OrderLinkStatusAudit> findByOrderNplLinkAndIsActive(OrderNplLink orderNplLink, Byte active);
	
	List<OrderLinkStatusAudit> findByOrderNplLink(OrderNplLink orderNplLink);
}
