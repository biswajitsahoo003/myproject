package com.tcl.dias.serviceactivation.entity.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.PolicyCriteria;
import com.tcl.dias.serviceactivation.entity.entities.RegexAspathConfig;
/**
 * This file contains the RegexAspathConfigRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface RegexAspathConfigRepository extends JpaRepository<RegexAspathConfig, Integer> {
	
	Set<RegexAspathConfig> findByPolicyCriteria(PolicyCriteria policyCriteria);

}
