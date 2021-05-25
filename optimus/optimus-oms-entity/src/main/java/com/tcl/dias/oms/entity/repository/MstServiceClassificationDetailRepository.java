package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstServiceClassificationDetail;

/**
 * 
 * This file contains the MstServiceClassificationDetailRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstServiceClassificationDetailRepository extends JpaRepository<MstServiceClassificationDetail, Integer>{
	
}
