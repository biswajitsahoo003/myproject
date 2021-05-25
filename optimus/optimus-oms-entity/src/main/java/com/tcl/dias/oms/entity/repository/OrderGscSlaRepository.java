package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderGscSla;

/**
 * Jpa Repository class of Order Gsc Sla Table and its entity
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderGscSlaRepository extends JpaRepository<OrderGscSla, Integer> {
	
}