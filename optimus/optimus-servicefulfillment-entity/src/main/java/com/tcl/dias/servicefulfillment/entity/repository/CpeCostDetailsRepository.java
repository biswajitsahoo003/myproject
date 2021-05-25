package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.CpeCostDetails;

/**
 * 
 * Repository Class
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface CpeCostDetailsRepository extends JpaRepository<CpeCostDetails, Integer> {
	
	List<CpeCostDetails> findByServiceIdAndServiceCodeAndComponentId(Integer serviceId,String serviceCode,Integer componentId);
	
	List<CpeCostDetails> findByServiceIdAndServiceCodeAndComponentIdAndMaterialCodeAndVendorCode(Integer serviceId,String serviceCode,Integer componentId,String materialCode,String vendorCode);
	
	List<CpeCostDetails> findByServiceIdAndServiceCodeAndComponentIdAndVendorCode(Integer serviceId,String serviceCode,Integer componentId,String vendorCode);

	List<CpeCostDetails> findByServiceIdAndComponentId(Integer serviceId,Integer componentId);
	
	List<CpeCostDetails> findByMaterialCodeAndBundledBomAndComponentIdAndVendorCode(String materialCode,String bomName,Integer componentId,String vendorCode);
	
	List<CpeCostDetails> findByServiceNumberAndBundledBomAndProductCode(String serviceNumber,String bundledBom,String productCode);

	List<CpeCostDetails> findByBundledBomAndComponentIdAndVendorCodeAndCategoryNotIn(String bomName,Integer componentId,String vendorCode,List<String> categoryList);

	List<CpeCostDetails> findByBundledBomAndComponentIdAndVendorCodeAndCategoryIn(String bomName,Integer componentId,String vendorCode,List<String> categoryList);

	List<CpeCostDetails> findByBundledBomAndComponentIdAndVendorCode(String bomName,Integer componentId,String vendorCode);
	
	List<CpeCostDetails> findByServiceIdAndServiceCodeAndComponentIdAndVendorCodeAndCategoryNotIn(Integer serviceId,String serviceCode,Integer componentId,String vendorCode,List<String> categoryList);

	List<CpeCostDetails> findByServiceIdAndServiceCodeAndComponentIdAndVendorCodeAndCategoryIn(Integer serviceId,String serviceCode,Integer componentId,String vendorCode,List<String> categoryList);

}
