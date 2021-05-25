package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.MstClrVpnSolution;

/**
 * This file contains the MstClrVpnSolutionRepository.java Repository class.
 *
 * @author Dimple
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface MstClrVpnSolutionRepository extends JpaRepository<MstClrVpnSolution, Integer> {

	MstClrVpnSolution findFirstByServiceCodeAndVpnTopologyIgnoreCase(String serviceCode,String vpnTopology);
}
