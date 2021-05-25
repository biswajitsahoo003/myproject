package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.VwVproxyProductComponent;
import com.tcl.dias.productcatelog.entity.entities.VwVproxyProductComponentId;

/**
 * 
 * @author vpachava
 *
 */
@Repository
@Transactional
public interface VwVproxyProductComponentRepository extends JpaRepository<VwVproxyProductComponent, VwVproxyProductComponentId>{
	
	
	List<VwVproxyProductComponent> findByLicenseNameAndAddonName(String profileName,String addon);
	
	@Query(value = "SELECT * FROM vw_product_offr_comp_attr_dtl_vproxy  where product_offering_nm=:productName",nativeQuery = true)
	List<VwVproxyProductComponent> selectProfileQuestionnarie(String productName);

}
