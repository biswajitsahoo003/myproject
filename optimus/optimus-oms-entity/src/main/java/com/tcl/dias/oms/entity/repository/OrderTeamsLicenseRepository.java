package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderTeamsDR;
import com.tcl.dias.oms.entity.entities.OrderTeamsLicense;

/**
 * Repository class for order teams license table
 * 
 * @author Srinivasa Raghavan
 * 
 */
@Repository
public interface OrderTeamsLicenseRepository extends JpaRepository<OrderTeamsLicense, Integer> {

	List<OrderTeamsLicense> findByOrderTeamsDR(OrderTeamsDR orderTeamsDR);
}
