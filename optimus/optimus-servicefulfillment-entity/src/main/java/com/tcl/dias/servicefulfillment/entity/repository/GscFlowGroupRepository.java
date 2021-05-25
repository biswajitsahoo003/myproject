package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroup;

import java.util.List;

/**
*
* This file contains the repository class for GscFlowGroup
* 
* @author Selvakumar Palaniandy
* @link http://www.tatacommunications.com/
* @copyright 2019 Tata Communications Limited
*/

@Repository
public interface GscFlowGroupRepository extends JpaRepository<GscFlowGroup, Integer> {

	GscFlowGroup findFirstByRefIdAndRefTypeOrderByIdDesc(String refId, String refType);

	List<GscFlowGroup> findByRefIdAndRefType(String refId, String refType);
}