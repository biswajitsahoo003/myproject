package com.tcl.dias.serviceactivation.entity.repository;

import com.tcl.dias.serviceactivation.entity.entities.PolicyCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.PrefixlistConfig;

import java.util.Set;

/**
 * This file contains the PrefixlistConfigRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface PrefixlistConfigRepository extends JpaRepository<PrefixlistConfig, Integer> {

    Set<PrefixlistConfig> findByPolicyCriteria(PolicyCriteria policyCriteria);

}
