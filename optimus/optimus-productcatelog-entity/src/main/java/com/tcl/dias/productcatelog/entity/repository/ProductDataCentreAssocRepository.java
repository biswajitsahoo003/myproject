package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.ProductDataCentreAssoc;
import com.tcl.dias.productcatelog.entity.entities.ProductDataCentreViewId;

@Repository
public interface ProductDataCentreAssocRepository
		extends JpaRepository<ProductDataCentreAssoc, ProductDataCentreViewId> {

	List<ProductDataCentreAssoc> findByCloudProviderName(String cloudProviderName);

	@Query(value = "SELECT DISTINCT Cloud_Prvdr_Name FROM vw_product_datacntr_provider_assoc", nativeQuery = true)
	List<String> findDistinctCloudProviderName();
	
	List<ProductDataCentreAssoc> findByDataCenterCd(String dcCode);
	
	ProductDataCentreAssoc findByDataCenterCdAndCloudProviderName(String dcCode,String cloudProviderName);

}
