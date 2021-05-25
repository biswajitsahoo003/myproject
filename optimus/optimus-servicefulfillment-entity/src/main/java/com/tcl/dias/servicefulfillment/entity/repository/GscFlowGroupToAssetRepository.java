package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroupToAsset;

/**
*
* This file contains the repository class for GscFlowGroupToAsset
* 
* @author Selvakumar Palaniandy
* @link http://www.tatacommunications.com/
* @copyright 2019 Tata Communications Limited
*/

@Repository
public interface GscFlowGroupToAssetRepository extends JpaRepository<GscFlowGroupToAsset, Integer> {
    
	List<GscFlowGroupToAsset> findByGscFlowGroupId(Integer gscFlowGroupId);
}