package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.VwOrderServiceAssetInfo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * This file contains the VwOrderServiceAssetInfoRepository.java class. Repository class
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface VwOrderServiceAssetInfoRepository extends JpaRepository<VwOrderServiceAssetInfo, String> {
	
	/**
	 * Find distinct city code based on customerLeIds and productId
	 * @param productId and customerLeIds
	 * @return
	 * 
	 */
	@Query(value="select distinct pop_site_code from vw_order_service_asset_info"+
			" where erf_prd_catalog_product_id=:productId and service_status!='Terminated'" + 
			" and erf_cust_le_id in (:customerLeIds) and pop_site_code is not null",nativeQuery=true)
	List<String> getDistinctCityBasedOnProductIdAndCustomerLeIds(@Param("customerLeIds") List<Integer> customerLeIds, @Param("productId") Integer productId);
	
	/**
	 * Find distinct business unit based on customerLeIds and productId
	 * @param productId and customerLeIds
	 * @return
	 * 
	 */
	@Query(value="select distinct business_unit from vw_order_service_asset_info"+
			" where erf_prd_catalog_product_id=:productId and service_status!='Terminated'" + 
			" and erf_cust_le_id in (:customerLeIds) and business_unit is not null",nativeQuery=true)
	List<String> getDistinctBusinessUnitBasedOnProductIdAndCustomerLeIds(@Param("customerLeIds") List<Integer> customerLeIds, @Param("productId") Integer productId);
	
	/**
	 * Find distinct zone based on customerLeIds and productId
	 * @param productId and customerLeIds
	 * @return
	 * 
	 */
	@Query(value="select distinct zone from vw_order_service_asset_info"+
			" where erf_prd_catalog_product_id=:productId and service_status!='Terminated'" + 
			" and erf_cust_le_id in (:customerLeIds) and zone is not null",nativeQuery=true)
	List<String> getDistinctZoneBasedOnProductIdAndCustomerLeIds(@Param("customerLeIds") List<Integer> customerLeIds, @Param("productId") Integer productId);
	
	/**
	 * Find distinct city code based on partnerleIds and productId
	 * @param productId and partnerleIds
	 * @return
	 * 
	 */
	@Query(value="select distinct pop_site_code from vw_order_service_asset_info"+
			" where erf_prd_catalog_product_id=:productId and service_status!='Terminated'" + 
			" and erf_cust_partner_le_id in (:partnerLeIds) and pop_site_code is not null",nativeQuery=true)
	List<String> getDistinctCityBasedOnProductIdAndPartnerLeIds(@Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId);
	
	/**
	 * Find distinct business unit based on partnerleIds and productId
	 * @param productId and partnerleIds
	 * @return
	 * 
	 */
	@Query(value="select distinct business_unit from vw_order_service_asset_info"+
			" where erf_prd_catalog_product_id=:productId and service_status!='Terminated'" + 
			" and erf_cust_partner_le_id in (:partnerLeIds) and business_unit is not null",nativeQuery=true)
	List<String> getDistinctBusinessUnitBasedOnProductIdAndPartnerLeIds(@Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId);
	
	/**
	 * Find distinct zone based on partnerleIds and productId
	 * @param productId and partnerleIds
	 * @return
	 * 
	 */
	@Query(value="select distinct zone from vw_order_service_asset_info"+
			" where erf_prd_catalog_product_id=:productId and service_status!='Terminated'" + 
			" and erf_cust_partner_le_id in (:partnerLeIds) and zone is not null",nativeQuery=true)
	List<String> getDistinctZoneBasedOnProductIdAndPartnerLeIds(@Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId);
	
	/**
	 * Find distinct partnerle based on partnerleIds and productId
	 * @param productId and partnerleIds
	 * @return
	 * 
	 */
	@Query(value="select distinct erf_cust_partner_name from vw_order_service_asset_info"+
			" where erf_prd_catalog_product_id=:productId and service_status!='Terminated'" + 
			" and erf_cust_partner_le_id in (:partnerLeIds) and erf_cust_partner_name is not null",nativeQuery=true)
	List<String> getDistinctPartnerNamesBasedOnProductIdAndPartnerLeIds(@Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId);

	List<VwOrderServiceAssetInfo> findAll(Specification<VwOrderServiceAssetInfo> specOrderServiceAsset);
	
	Page<VwOrderServiceAssetInfo> findAll(Specification<VwOrderServiceAssetInfo> specOrderServiceAsset,Pageable pageable);
	
	List<VwOrderServiceAssetInfo> findByServiceIdAndStatusNotAndNameIn(String uuid,String serviceStatus,List<String> names);
	
}
