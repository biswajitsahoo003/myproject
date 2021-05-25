package com.tcl.dias.l2oworkflow.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.MstProcessDef;

/**
 * This file contains the MstProcessDefRepository.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface MstProcessDefRepository extends JpaRepository<MstProcessDef, Integer> {

	MstProcessDef findByKey(String key);

}
