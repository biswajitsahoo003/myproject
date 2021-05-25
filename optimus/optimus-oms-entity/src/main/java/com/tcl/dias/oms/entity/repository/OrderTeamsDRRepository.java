package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderTeamsDR;

/**
 * Repository class for order teams DR table
 * 
 * @author Srinivasa Raghavan
 * 
 */
@Repository
public interface OrderTeamsDRRepository extends JpaRepository<OrderTeamsDR, Integer> {

	List<OrderTeamsDR> findByOrderProductSolutionAndStatus(OrderProductSolution orderProductSolution, Byte status);

	List<OrderTeamsDR> findByOrderToLeId(Integer orderToLeId);
}
