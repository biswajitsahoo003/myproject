package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderDirectRouting;
import com.tcl.dias.oms.entity.entities.OrderTeamsDR;

/**
 * Repository class for Order direct routing
 * 
 * @author Srinivasa Raghavan
 * 
 */
@Repository
public interface OrderDirectRoutingRepository extends JpaRepository<OrderDirectRouting, Integer> {

	List<OrderDirectRouting> findByOrderTeamsDR(OrderTeamsDR orderTeamsDR);
}
