package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.SIAsset;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * This file contains the repository class for SIAssetAttribute entity
 * 
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface SIAssetAttributeRepository extends JpaRepository<SIAssetAttribute, Integer> {
	
	List<SIAssetAttribute> findBySiAssetAndCategoryIn(SIAsset siAsset,List<String> categoryList);

    List<SIAssetAttribute> findBySiAssetInAndAttributeName(List<SIAsset> assetIds, List<String> attributeNames);
}
