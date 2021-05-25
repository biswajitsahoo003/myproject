package com.tcl.dias.networkaugment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.networkaugment.entity.entities.ScAdditionalServiceParam;

/**
 * 
 * This file contains the ActivityPlanRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ScAdditionalServiceParamRepository extends JpaRepository<ScAdditionalServiceParam, Integer> {


}
