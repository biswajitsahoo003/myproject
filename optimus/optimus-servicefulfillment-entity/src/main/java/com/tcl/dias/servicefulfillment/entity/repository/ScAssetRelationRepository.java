package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ScAssetRelation;

/**
*
* This file contains the repository class for ScAssetRelation
* 
* @author Selvakumar Palaniandy
* @link http://www.tatacommunications.com/
* @copyright 2019 Tata Communications Limited
*/

@Repository
public interface ScAssetRelationRepository extends JpaRepository<ScAssetRelation, Integer> {

	ScAssetRelation findByScAssetId(Integer id);

	List<ScAssetRelation> findAllByScAssetId(Integer id);

	ScAssetRelation findByScAssetIdAndRelationType(Integer id, String relationType);
	
}
