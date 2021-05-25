package com.tcl.dias.serviceactivation.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.PolicyCriteria;

/**
 * This file contains the PolicyCriteriaRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface PolicyCriteriaRepository extends JpaRepository<PolicyCriteria, Integer> {

	public PolicyCriteria findByPolicyCriteriaId(Integer policyCriteriaId);

	public PolicyCriteria findByPolicyCriteriaIdAndEndDateIsNull(Integer policyCriteriaId);
	
	public List<PolicyCriteria> findByPolicyCriteriaIdInAndEndDateIsNull(List<Integer> policyCriteriaId);
	
	public List<PolicyCriteria> findByPolicyCriteriaIdInAndMatchcriteriaPrefixlist(List<Integer> policyCriteriaId,Byte prefix);

}
