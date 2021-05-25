package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderDirectRoutingMediaGateways;

/**
 * Repository for order_dr_mediagateways table
 *
 * @author Srinivasa Raghavan
 */
@Repository
public interface OrderDirectRoutingMgRepository extends JpaRepository<OrderDirectRoutingMediaGateways, Integer> {
	List<OrderDirectRoutingMediaGateways> findByOrderDirectRoutingCityId(Integer orderDirectRoutingCityId);
}
