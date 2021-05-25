package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.SIAsset;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;
import com.tcl.dias.serviceinventory.entity.entities.VwServiceAssetInfo;
import com.tcl.dias.serviceinventory.entity.queries.NativeQueries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * This file contains the repository class for SIAsset entity
 * 
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface SIAssetRepository extends JpaRepository<SIAsset, Integer> {

	/**
	 * Find all asset ids belonging to a specified service detail record using asset type and asset relation types
	 * @param serviceDetailId
	 * @param assetTypes
	 * @param relationTypes
	 * @return
	 */
	@Query(value = NativeQueries.FETCH_ASSET_BY_TYPE_AND_SERVICE_DETAIL_AND_RELATIONSHIP_TYPE, nativeQuery = true)
	List<Integer> findAllAssetIdsByServiceDetailAndAssetTypes(@Param("serviceDetailId") Integer serviceDetailId,
			@Param("assetTypes") List<String> assetTypes, @Param("relationTypes") List<String> relationTypes);
	
	/**
	 * Find asset details based on serviceDetailIdList
	 * @param serviceDetailIdList
	 * @return
	 * 
	 */
	@Query(value="select distinct sa.id as assetId, sa.name as name, sa.erf_loc_location_id as locationId,sa.si_service_detail_id as serviceDetailId,saa.attribute_name as aname, saa.attribute_value as avalue from si_asset sa join si_asset_attributes saa on sa.id=saa.si_asset_id\r\n" +
			"where SI_service_detail_id in (:serviceDetailIdList) group by assetId;",nativeQuery=true)
	List<Map<String,Object>> getAssetDetailsBasedOnServiceDetailIds(@Param("serviceDetailIdList") List<Integer> serviceDetailIdList);
	
	/**
	 * Find asset details based on assetIdList
	 * @param assetIdList
	 * @return
	 * 
	 */
	@Query(value="select sa.id as asset_id, sa.name as asset_name, saa.category as category,"
			+ " group_concat(saa.attribute_name,':',IF(saa.attribute_value='','--',saa.attribute_value)) as attrValues "
			+ " from si_asset sa join si_asset_attributes saa on sa.id = saa.SI_Asset_ID "
			+ " where sa.id in(:assetIdList) group by asset_id, category;",nativeQuery=true)
	List<Map<String,Object>> getAssetDetailsBasedOnAssetIds(@Param("assetIdList") List<Integer> assetIdList);
	
	List<SIAsset> findByIdIn(List<Integer> assetIdList);

	/**
	 * Find asset count based on productId and leIds
	 * @param productId and leIds
	 * @return
	 * 
	 */
	@Query(value="select count(*) from (select distinct sa.id from si_order so join si_service_detail ssd on so.id = ssd.si_order_id join si_asset sa on sa.SI_service_detail_id=ssd.id"
			+ " where ssd.erf_prd_catalog_product_id=:productId and ssd.service_status!='Terminated' and so.erf_cust_le_id in (:leIds) and sa.type not in('ACCESS','ADDON')) as assetTotalCount",nativeQuery=true)
	Integer getTotalAssetCountBasedOnProductIdAndCustomerLeIds(@Param("leIds") List<Integer> leIds,@Param("productId") Integer productId);

    SIAsset findFirstBySiServiceDetailAndTypeIgnoreCase(SIServiceDetail siServiceDetail, String type);

	Page<SIAsset> findAll(Specification spec, Pageable pageable);

	List<SIAsset> findAll(Specification spec);

	@Query(value = "select sa.* from si_asset sa left outer join si_service_detail ssd on ssd.id = sa.SI_service_detail_id " +
			"left join si_order so on ssd.si_order_id = so.id where so.id = :siOrderId and " +
			"ssd.gsc_order_sequence_id = :gscOrderSequenceId and sa.type = 'Toll-Free'", nativeQuery = true)
	List<SIAsset> findByGscOrderSequenceId(@Param("siOrderId") String siOrderId, @Param("gscOrderSequenceId") String gscOrderSequenceId);
	
	@Query(value="select sa.asset_name as assetName , sa.asset_sys_id as asset_sys_id,sa.model as cpe_model,sa.scope_of_management as scope_management,sa.owner as ip_address_provided,sa.support_type as cpe_support_type,sa.serial_no as cpe_serial_no, sa.oem_vendor as oem_vendor from  vw_service_asset_info sa where sa.srv_sys_id =:sysid and sa.asset_type=:type order by sa.asset_sys_id desc limit 1", nativeQuery=true)
	SIAsset getAssetTypeDetails(@Param("sysid") Integer sysid,@Param("type") String type);
	
	SIAsset findFirstBySiServiceDetail_IdAndTypeIgnoreCaseOrderByIdDesc(Integer siServiceDetail, String type);

	List<SIAsset> findByCloudCodeInOrderByIdDesc(Set<String> cloudCodeL, Pageable pageable);

	List<SIAsset> findBySiServiceDetail_SiOrderUuid(String orderCode);
}
