package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.BomInventoryCatalogAssoc;

/**
 * 
 * Repository for BomInventoryCatalogAssoc
 *
 */
@Repository
public interface BomInventoryCatalogAssocRepository extends JpaRepository<BomInventoryCatalogAssoc, Integer> {
	
	public List<BomInventoryCatalogAssoc> findByInventoryBomName(String inventoryBomName);
	
	public List<BomInventoryCatalogAssoc> findByProductCatalogBomName(String productCatalogBomName);
	
	

}
