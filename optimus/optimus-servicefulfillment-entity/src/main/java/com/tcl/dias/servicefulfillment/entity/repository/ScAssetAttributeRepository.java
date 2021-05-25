package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ScAssetAttribute;

/**
 *
 * This file contains the repository class for ScAssetAttribute
 * 
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@Repository
public interface ScAssetAttributeRepository extends JpaRepository<ScAssetAttribute, Integer> {

	ScAssetAttribute findByScAsset_IdAndAttributeName(Integer assetId, String attributeName);
    
    List<ScAssetAttribute> findByScAsset_IdAndAttributeNameIn(Integer assetId, List<String> attributeName);
	
}
