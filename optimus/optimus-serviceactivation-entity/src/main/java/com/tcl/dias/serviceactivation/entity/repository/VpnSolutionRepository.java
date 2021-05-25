package com.tcl.dias.serviceactivation.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.VpnSolution;

/**
 * This file contains the VpnSolutionRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface VpnSolutionRepository extends JpaRepository<VpnSolution, Integer> {

	List<VpnSolution> findByServiceDetail_IdAndEndDateIsNull(Integer serviceDetailId);
}
