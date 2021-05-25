package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;

/**
 * This class is order confirmation audit repository
 * 
 * @author SEKHAR ER
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderConfirmationAuditRepository extends JpaRepository<OrderConfirmationAudit, Integer> {

	public OrderConfirmationAudit findByOrderRefUuid(String orderRefId);

}
