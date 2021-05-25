package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrAdditionalServiceParam;

/**
 * 
 * Repository class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limiteds
 */
@Repository
public interface OdrAdditionalServiceParamRepository extends JpaRepository<OdrAdditionalServiceParam, Integer> {

	 OdrAdditionalServiceParam findByReferenceId(String referenceId);
}
