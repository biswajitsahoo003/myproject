package com.tcl.dias.serviceactivation.entity.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.NeighbourCommunityConfig;
import com.tcl.dias.serviceactivation.entity.entities.PolicyCriteria;
/**
 * This file contains the NeighbourCommunityConfigRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface NeighbourCommunityConfigRepository extends JpaRepository<NeighbourCommunityConfig, Integer> {

	Set<NeighbourCommunityConfig> findByPolicyCriteria(PolicyCriteria policyCriteria);
	
}
