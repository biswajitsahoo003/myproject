package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.MstStageDef;

/**
 * This file contains the MstStageDefRepository.java class.
 * 
 *
 * @author MAYANSHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface MstStageDefRepository extends JpaRepository<MstStageDef, Integer> {

	MstStageDef findByKey(String key);

	List<MstStageDef> findByCustomerView(String customerView);

}
