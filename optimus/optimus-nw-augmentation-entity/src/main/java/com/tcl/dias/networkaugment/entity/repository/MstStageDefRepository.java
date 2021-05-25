package com.tcl.dias.networkaugment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.networkaugment.entity.entities.MstStageDef;

import java.util.List;

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

	MstStageDef findByKey(String defKey);

	List<MstStageDef> findByCustomerView(String customerView);

}
