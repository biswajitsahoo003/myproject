package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.VwVproxyProductOffering;

/**
 * 
 * @author vpachava
 *
 */
@Repository
@Transactional
public interface VwVproxyProductOfferingRepository extends JpaRepository<VwVproxyProductOffering, Integer>{

	@Query(value="select * from vw_bundled_product_offr_vproxy where product_offering_nm=:productName",nativeQuery=true)
	List<VwVproxyProductOffering> findByProductOfferingName(String productName);
	
	
	@Query(value="select * from vw_product_profile_vutm",nativeQuery=true)
	List<Map<String, Object>> findVutmProfiles();
	
	@Query(value="select * from vw_product_comp_attr_dtl_vutm where attribute_nm=:attributeName",nativeQuery = true)
	List<Map<String, Object>> findAttributeForVutm(String attributeName);
	
	@Query(value="select * from vw_cgw_service_area_matrix where city_nm=:city",nativeQuery = true)
	Map<String, Object> findCGWServiceAreaMatrixByCity(String city);
	
	@Query(value="select * from vw_cgw_service_area_matrix",nativeQuery = true)
	List<Map<String, Object>> findCGWServiceAreaMatrix();
	
}
