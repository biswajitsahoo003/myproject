package com.tcl.dias.oms.entity.repository;


import com.tcl.dias.oms.entity.entities.IntracityExceptionRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for intracity exception rules
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * @Author Chetan Chaudhary
 */

@Repository
public interface IntracityExceptionRulesRepository extends JpaRepository<IntracityExceptionRules, Integer> {
}
