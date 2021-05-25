package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderTeamsDR;
import com.tcl.dias.oms.entity.entities.OrderTeamsDRDetails;

/**
 * Repository class for order teams DR details table
 * 
 * @author Srinivasa Raghavan
 * 
 * 
 */
@Repository
public interface OrderTeamsDRDetailsRepository extends JpaRepository<OrderTeamsDRDetails, Integer> {

	List<OrderTeamsDRDetails> findByOrderTeamsDR(OrderTeamsDR quoteTeamsDR);

}
