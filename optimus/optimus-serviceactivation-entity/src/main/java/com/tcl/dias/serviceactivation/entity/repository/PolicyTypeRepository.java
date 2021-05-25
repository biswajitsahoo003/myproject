package com.tcl.dias.serviceactivation.entity.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.Bgp;
import com.tcl.dias.serviceactivation.entity.entities.PolicyType;
/**
 * This file contains the PolicyTypeRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface PolicyTypeRepository extends JpaRepository<PolicyType, Integer> {
	
	Set<PolicyType> findByBgp(Bgp bgp);
}
