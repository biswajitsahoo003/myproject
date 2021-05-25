package com.tcl.dias.oms.entity.repository;

import java.util.List;

import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteGscDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;

/**
 * Jpa Repository class of Order Gsc Details Table and its entity
 *
 * @author Vishesh Awasthi
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface OrderGscDetailRepository extends JpaRepository<OrderGscDetail, Integer> {
	
	/**
	 * Get all OrderGscDetail by gscId
	 * 
	 * @param gscId
	 * @return {@link List<OrderGscDetail>}
	 */
	List<OrderGscDetail> findByorderGsc(OrderGsc gscId);

	List<OrderGscDetail> findByOrderGscIn(List<OrderGsc> orderGscs);
}
