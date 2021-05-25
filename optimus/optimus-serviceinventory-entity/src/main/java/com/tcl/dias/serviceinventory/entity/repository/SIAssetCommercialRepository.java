package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.SIAsset;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetCommercial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * This file contains the repository class for SIAsset entity
 * 
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface SIAssetCommercialRepository extends JpaRepository<SIAssetCommercial, Integer> {

	public SIAssetCommercial findBySiAsset(SIAsset siAsset);
}
