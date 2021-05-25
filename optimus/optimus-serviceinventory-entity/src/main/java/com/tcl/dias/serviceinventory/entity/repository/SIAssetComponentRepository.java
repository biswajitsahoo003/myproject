package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.SIAssetComponent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
public interface SIAssetComponentRepository extends JpaRepository<SIAssetComponent, Integer> {

	@Query(value="select distinct sacm.item from si_asset_commercial sacmm join si_asset_component sacm on sacmm.id=sacm.si_asset_commercial_id\r\n" +
			"where  sacmm.si_asset_id = :assetId",nativeQuery=true)
	List<String> getAssetDetailsBasedOnServiceDetailIds(@Param("assetId") Integer assetId);
}
