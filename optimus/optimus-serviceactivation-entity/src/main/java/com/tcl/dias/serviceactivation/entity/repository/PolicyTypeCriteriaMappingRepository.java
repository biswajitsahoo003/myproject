package com.tcl.dias.serviceactivation.entity.repository;

import com.tcl.dias.serviceactivation.entity.entities.PolicyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.PolicyTypeCriteriaMapping;
/**
 * This file contains the PolicyTypeCriteriaMappingRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface PolicyTypeCriteriaMappingRepository extends JpaRepository<PolicyTypeCriteriaMapping, Integer> {

    PolicyTypeCriteriaMapping findByPolicyTypeAndPolicyCriteriaId(PolicyType policyType, Integer policyCriteriaId);

}
