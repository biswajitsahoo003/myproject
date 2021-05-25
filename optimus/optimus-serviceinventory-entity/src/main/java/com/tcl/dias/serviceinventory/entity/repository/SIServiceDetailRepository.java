package com.tcl.dias.serviceinventory.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;
import com.tcl.dias.serviceinventory.entity.queries.NativeQueries;

/**
 * This file contains the repository class for SIServiceDetail entity
 *
 * @author Dinahar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface SIServiceDetailRepository extends JpaRepository<SIServiceDetail, Integer>,JpaSpecificationExecutor<SIServiceDetail> {

	/**
	 * Find Tps Service ID
	 *
	 * @param serviceId
	 * @return
	 */
	Optional<SIServiceDetail> findByTpsServiceId(String serviceId);

	// List<SIServiceDetail> findByTpsServiceIds(String serviceId);

	/**
	 * Find SI details for NPL service. 
	 * 
	 * @param orderId, the order id.
	 * @return
	 */
	@Query(nativeQuery = true, value = "select * from si_service_detail where tps_service_id =:serviceId")
	List<SIServiceDetail> findByTpsServiceIdForNPL(@Param("serviceId") String serviceId);

	
	/**
	 * Find SI details for GSC service.
	 * 
	 * @param orderId, the order id.
	 * @param number, the name(tollfree number) of the GSIP service to be provided
	 * @param outpulse, the number (fqdn) of the GSIP service to be provided
	 * @return
	 */
	@Query(nativeQuery = true, value = "select * from si_service_detail where tps_service_id in \r\n" + 
			"(select SI_service_detail_tps_service_id from si_asset_to_service where SI_Asset_ID = \r\n" + 
			"(SELECT id FROM si_asset where fqdn = :number and name = :outpulse and type = 'Toll-Free') \r\n" + 
			"and is_active = 'Y') \r\n" + 
			"and si_order_id = :orderId")
	Optional<SIServiceDetail> findByTpsServiceIdForGSC(@Param("orderId") String orderId, @Param("number") String number, @Param("outpulse") String outpulse);
	
	@Query(nativeQuery = true, value = "select * from si_service_detail where tps_service_id in \r\n" + 
			"(select SI_service_detail_tps_service_id from si_asset_to_service where SI_Asset_ID = \r\n" + 
			"(SELECT id FROM si_asset where fqdn = :number  and type = 'Toll-Free') \r\n" + 
			"and is_active = 'Y') \r\n" + 
			"and si_order_id = :orderId")
	Optional<SIServiceDetail> findByTpsServiceIdWithoutOutpulseForGSC(@Param("orderId") String orderId, @Param("number") String number);

	/**
	 * Find Product Family wise Asset Count
	 *
	 * @param customerLeIds
	 * @param productFamily
	 * @param assetType
	 * @return
	 */
	@Query(nativeQuery = true, value = NativeQueries.FETCH_PRODUCT_FAMILYWISE_ASSET_COUNT_BY_TYPE)
	List<Map<String, Object>> fetchProductFamilywiseAssetCountByCustomerIdAndByType(
			@Param("customerLeIds") List<Integer> customerLeIds, @Param("productFamily") String productFamily,
			@Param("assetType") String assetType);

	/**
	 * Find Configuration Country By Access Type
	 *
	 * @param accessType
	 * @param customerLeIds
	 * @return
	 */
	@Query(value = "select sd.source_country as origin, sd.destination_country as destination,pr.access_type as access_type, count(*) as " +
			"no_count from si_service_detail sd, si_product_reference pr, "
			+ "si_order o, si_contract_info ci, si_asset asset, si_asset_to_service ats where sd.product_reference_id=pr.id and o.id=ci.SI_order_id "
			+ "and asset.ID=ats.SI_Asset_ID AND ats.si_service_detail_id=sd.id AND pr.access_type =:accessType and ci.erf_cust_le_id IN (:customerLeIds) "
			+ "and asset.type =:assetType group by sd.source_country, sd.destination_country,pr.access_type", nativeQuery = true)
	List<Map<String, Object>> findConfigurationCountriesAccessType(@Param("accessType") String accessType,
																   @Param("customerLeIds") List<Integer> customerLeIds, @Param("assetType") String assetType);


	/**
	 * Find Configuration Country
	 *
	 * @param customerLeIds
	 * @return
	 */
	@Query(value = "select sd.source_country as origin, sd.destination_country as destination, pr.access_type as " +
			"access_type, count(*) as " +
			"no_count from si_service_detail sd, si_product_reference pr, "
			+ "si_order o, si_contract_info ci, si_asset asset, si_asset_to_service ats where sd.product_reference_id=pr.id and o.id=ci.SI_order_id "
			+ "and asset.ID=ats.SI_Asset_ID AND ats.si_service_detail_id=sd.id and ci.erf_cust_le_id IN (:customerLeIds) "
			+ "and asset.type =:assetType group by sd.source_country, sd.destination_country, pr.access_type", nativeQuery = true)
	List<Map<String, Object>> findConfigurationCountries(@Param("customerLeIds") List<Integer> customerLeIds, @Param("assetType") String assetType);


	/**
	 * Find Configuration Country Toll Free Number
	 *
	 * @param customerLeIds
	 * @param tollFreeNumber
	 * @return
	 */
	@Query(value = "select sd.source_country as origin, sd.destination_country as destination,pr.access_type as access_type, count(*) as no_count from " +
			"si_service_detail sd, si_product_reference pr, "
			+ "si_order o, si_contract_info ci, si_asset asset, si_asset_to_service ats where sd.product_reference_id=pr.id and o.id=ci.SI_order_id "
			+ "and asset.ID=ats.SI_Asset_ID AND ats.si_service_detail_id=sd.id and ci.erf_cust_le_id IN (:customerLeIds) "
			+ "and asset.type =:assetType and asset.fqdn like :tollFreeNumber group by sd.source_country, sd" +
			".destination_country, pr.access_type", nativeQuery = true)
	List<Map<String, Object>> findConfigurationCountriesByNumber(@Param("customerLeIds") List<Integer> customerLeIds,
																 @Param("tollFreeNumber") String tollFreeNumber, @Param("assetType") String assetType);

	/**
	 * Find Configuration Country By Access Type and Country Code
	 *
	 * @param accessType
	 * @param customerLeIds
	 * @param countryCode
	 * @return
	 */
	@Query(value = "select sd.source_country as origin, sd.destination_country as destination,pr.access_type as access_type, count(*) as " +
			"no_count from si_service_detail sd, si_product_reference pr, "
			+ "si_order o, si_contract_info ci, si_asset asset, si_asset_to_service ats where sd.product_reference_id=pr.id and o.id=ci.SI_order_id "
			+ "and asset.ID=ats.SI_Asset_ID AND ats.si_service_detail_id=sd.id AND pr.access_type =:accessType and ci.erf_cust_le_id IN (:customerLeIds) "
			+ "and asset.type =:assetType and sd.source_country =:countryCode group by sd.source_country, sd" +
			".destination_country, pr.access_type", nativeQuery = true)
	List<Map<String, Object>> findConfigurationCountriesByCode(@Param("accessType") String accessType,
			@Param("customerLeIds") List<Integer> customerLeIds, @Param("countryCode") String countryCode, @Param("assetType") String assetType);

	/**
	 * Find Configuration Country By Access Type and Country Code and Toll Free
	 * Number
	 *
	 * @param accessType
	 * @param customerLeIds
	 * @param tollFreeNumber
	 * @return
	 */
	@Query(value = "select sd.source_country as origin, sd.destination_country as destination,pr.access_type as access_type, count(*) as " +
			"no_count from si_service_detail sd, si_product_reference pr, "
			+ "si_order o, si_contract_info ci, si_asset asset, si_asset_to_service ats where sd.product_reference_id=pr.id and o.id=ci.SI_order_id "
			+ "and asset.ID=ats.SI_Asset_ID AND ats.si_service_detail_id=sd.id AND pr.access_type =:accessType and ci.erf_cust_le_id IN (:customerLeIds) "
			+ "and asset.type =:assetType and asset.fqdn like:tollFreeNumber " +
			"group " +
			"by sd.source_country, sd.destination_country, pr.access_type",
			nativeQuery = true)
	List<Map<String, Object>> findConfigurationCountriesByAccessTypeAndNumber(@Param("accessType") String accessType,
																			  @Param("customerLeIds") List<Integer> customerLeIds, @Param("tollFreeNumber") String tollFreeNumber, @Param("assetType") String assetType);

	/**
	 * Find Asset Numbers for given configuration details
	 *
	 * @param sourceCountry
	 * @param destinationCountry
	 * @param productName
	 * @return
	 */
//	@Query(value = "select distinct o.id                    as order_id,\n" +
//			"                o.op_order_code         as tigerOrderId,\n" +
//			"                o.erf_cust_le_id        as customer_le_id,\n" +
//			"                o.erf_cust_le_name      as customer_le_name,\n" +
//			"                sd.access_type          as access_type,\n" +
//			"                sd.id                   as service_id,\n" +
//			"                (select distinct fqdn\n" +
//			"                 from si_asset\n" +
//			"                 where si_service_detail_id = sd.id\n" +
//			"                   and type = 'Toll-Free'\n" +
//			"                   and origin_ntwrk = asset.origin_ntwrk\n" +
//			"                   and is_active = 'Y') as tollFreeNumber,\n" +
//			"                (select distinct name\n" +
//			"                 from si_asset\n" +
//			"                 where si_service_detail_id = sd.id\n" +
//			"                   and type = 'Outpulse'\n" +
//			"                   and origin_ntwrk = asset.origin_ntwrk\n" +
//			"                   and is_active = 'Y') as outpulse,\n" +
//			"                asset.origin_ntwrk      as origin_network,\n" +
//			"                sd.source_country       as origin,\n" +
//			"                sd.destination_country  as destination,\n" +
//			"                o.tps_secs_id           as secs_id\n" +
//			"from si_order o\n" +
//			"         inner join si_service_detail sd on sd.SI_order_id = o.id\n" +
//			"         inner join si_product_reference pr on sd.product_reference_id = pr.id\n" +
//			"         inner join si_asset_to_service ats on sd.id = ats.si_service_detail_id\n" +
//			"         inner join si_asset asset on asset.id = ats.SI_Asset_ID\n" +
//			"where pr.sub_variant = :productName\n" +
//			"  and o.erf_cust_customer_id = :customerId\n" +
//			"  and o.tps_secs_id is not null\n" +
//			"  and sd.source_country IN (:sourceCountry)\n" +
//			"  and sd.destination_country IN (:destinationCountry)\n" +
//			"  and asset.type in ('Outpulse', 'Toll-Free')\n" +
//			"  and o.is_active = 'Y'\n" +
//			"  and sd.is_active = 'Y'\n" +
//			"  and pr.is_active = 'Y'\n" +
//			"  and ats.is_active = 'Y'\n" +
//			"  and asset.is_active = 'Y'", nativeQuery = true)
	@Query(value = "select sd.gsc_order_sequence_id as order_sequence_id,o.id as order_id, o.op_order_code as tigerOrderId, o.erf_cust_le_id as customer_le_id, o.erf_cust_le_name as customer_le_name, " +
			"sd.access_type as access_type, asset.id as asset_id, asset.type as asset_type, asset.fqdn as tollFreeNumber, " +
			"asset.name as outpulse, asset.origin_ntwrk as origin_network, sd.source_country as origin, sd.destination_country as destination, o.tps_secs_id as secs_id " +
			"from si_order o " +
			"inner join si_service_detail sd on sd.SI_order_id = o.id " +
			"inner join si_product_reference pr on sd .product_reference_id = pr.id " +
			"inner join si_asset_to_service ats on sd.id = ats.si_service_detail_id and ats.SI_service_detail_tps_service_id=sd.tps_service_id " +
			"inner join si_asset asset on asset.id = ats.SI_Asset_ID " +
			"where pr.sub_variant = :productName  and o.erf_cust_customer_id = :customerId and o.tps_secs_id is not null " +
			"and sd.source_country IN (:sourceCountry) and sd.destination_country IN (:destinationCountry) " +
			"and asset.type = :assetType " +
			"and o.is_active='Y' and sd.is_active='Y' and pr.is_active='Y' and ats.is_active='Y' and asset.is_active='Y'", nativeQuery = true)
	List<Map<String, Object>> findAssetNumberBasedOnConfigurations(@Param("customerId") Integer customerId, @Param("sourceCountry") List<String> sourceCountry,
			@Param("destinationCountry") List<String> destinationCountry, @Param("productName") String productName, @Param("assetType") String assetType);

	/**
	 * Find Asset Numbers for given configuration details
	 *
	 * @param sourceCountry
	 * @param productName
	 * @return
	 */
//	@Query(value = "select distinct o.id                    as order_id,\n" +
//			"                o.op_order_code         as tigerOrderId,\n" +
//			"                o.erf_cust_le_id        as customer_le_id,\n" +
//			"                o.erf_cust_le_name      as customer_le_name,\n" +
//			"                sd.access_type          as access_type,\n" +
//			"                sd.id                   as service_id,\n" +
//			"                (select distinct fqdn\n" +
//			"                 from si_asset\n" +
//			"                 where si_service_detail_id = sd.id\n" +
//			"                   and type = 'Toll-Free'\n" +
//			"                   and origin_ntwrk = asset.origin_ntwrk\n" +
//			"                   and is_active = 'Y') as tollFreeNumber,\n" +
//			"                (select distinct name\n" +
//			"                 from si_asset\n" +
//			"                 where si_service_detail_id = sd.id\n" +
//			"                   and type = 'Outpulse'\n" +
//			"                   and origin_ntwrk = asset.origin_ntwrk\n" +
//			"                   and is_active = 'Y') as outpulse,\n" +
//			"                asset.origin_ntwrk      as origin_network,\n" +
//			"                sd.source_country       as origin,\n" +
//			"                sd.destination_country  as destination,\n" +
//			"                o.tps_secs_id           as secs_id\n" +
//			"from si_order o\n" +
//			"         inner join si_service_detail sd on sd.SI_order_id = o.id\n" +
//			"         inner join si_product_reference pr on sd.product_reference_id = pr.id\n" +
//			"         inner join si_asset_to_service ats on sd.id = ats.si_service_detail_id\n" +
//			"         inner join si_asset asset on asset.id = ats.SI_Asset_ID\n" +
//			"where pr.sub_variant = :productName\n" +
//			"  and o.erf_cust_customer_id = :customerId\n" +
//			"  and o.tps_secs_id is not null\n" +
//			"  and sd.source_country IN (:sourceCountry)\n" +
//			"  and asset.type in ('Outpulse', 'Toll-Free')\n" +
//			"  and o.is_active = 'Y'\n" +
//			"  and sd.is_active = 'Y'\n" +
//			"  and pr.is_active = 'Y'\n" +
//			"  and ats.is_active = 'Y'\n" +
//			"  and asset.is_active = 'Y'", nativeQuery = true)
	@Query(value = "select sd.gsc_order_sequence_id as order_sequence_id,o.id as order_id, o.op_order_code as tigerOrderId, o.erf_cust_le_id as customer_le_id, o.erf_cust_le_name as customer_le_name, " +
			"sd.access_type as access_type, asset.id as asset_id, asset.type as asset_type, asset.fqdn as tollFreeNumber, " +
			"asset.name as outpulse, asset.origin_ntwrk as origin_network, sd.source_country as origin, sd.destination_country as destination, o.tps_secs_id as secs_id " +
			"from si_order o " +
			"inner join si_service_detail sd on sd.SI_order_id = o.id " +
			"inner join si_product_reference pr on sd.product_reference_id = pr.id " +
			"inner join si_asset_to_service ats on sd.id = ats.si_service_detail_id and ats.SI_service_detail_tps_service_id=sd.tps_service_id " +
			"inner join si_asset asset on asset.id = ats.SI_Asset_ID " +
			"where pr.sub_variant = :productName and o.erf_cust_customer_id = :customerId and o.tps_secs_id is not null " +
			"and sd.source_country IN (:sourceCountry) " +
			"and asset.type = :assetType " +
			"and o.is_active='Y' and sd.is_active='Y' and pr.is_active='Y' and ats.is_active='Y' and asset.is_active='Y'", nativeQuery = true)
	List<Map<String, Object>> findAssetNumberBasedOnConfigurationsByOrigin(@Param("customerId") Integer customerId, @Param("sourceCountry") List<String> sourceCountry,
																   @Param("productName") String productName, @Param("assetType") String assetType);

	/**
	 * Find Configurations By Product Name and Order ID
	 *
	 * @param orderId
	 * @param productName
	 * @param customerLeIds
	 * @param assetType
	 * @return
	 */
	@Query(value = "select sd.source_country as origin, sd.destination_country as destination, pr.access_type" +
			" as access_type, count(*) as no_count from si_contract_info contract inner join si_order o on o.id = " +
			"contract.SI_order_id inner join si_service_detail sd on sd.SI_order_id = o.id inner join " +
			"si_product_reference pr on sd.product_reference_id = pr.id inner join si_asset_to_service ats on sd.id =" +
			" ats.si_service_detail_id inner join si_asset asset on asset.id = ats.SI_Asset_ID where o.id=:orderId " +
			"and " +
			"contract" +
			".erf_cust_le_id IN (:customerLeIds) and asset.type =:assetType and pr.sub_variant =:productName group by sd" +
			".source_country, sd.destination_country, pr.access_type", nativeQuery = true)
	List<Map<String, Object>> findConfigurationsByOrderAndProductNameAndOrderId(@Param("orderId") String orderId,
			@Param("productName") String productName, @Param("customerLeIds") List<Integer> customerLeIds, @Param("assetType") String assetType);

	/**
	 * Find Configurations By Product Name
	 *
	 * @param productName
	 * @param customerLeIds
	 * @param assetType
	 * @return
	 */
	@Query(value = "select sd.source_country as origin, sd.destination_country as destination, pr.access_type as " +
			"access_type ,count(*) as no_count, o.id as order_id from si_order o inner join si_service_detail sd on " +
			"sd" +
			".SI_order_id = o.id inner join si_product_reference pr on sd.product_reference_id = pr.id inner join " +
			"si_asset_to_service ats on sd.id = ats.si_service_detail_id inner join si_asset asset on asset.id = ats" +
			".SI_Asset_ID where o.erf_cust_customer_id IN (:customerId) and o.erf_cust_le_id IN (:customerLeIds) and o.tps_secs_id is not null and asset.type = :assetType and pr.sub_variant =" +
			" :productName group by sd.source_country, sd.destination_country, pr.access_type", nativeQuery =
			true)
	List<Map<String, Object>> findConfigurationsByProductName(@Param("productName") String productName, @Param("customerLeIds") List<Integer> customerLeIds,@Param("assetType") String assetType,@Param("customerId") Integer customerId);

	@Query(value = "select count(*) as count from si_order ordTbl\r\n" + 
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id where serviceTbl.service_status!='Terminated' and serviceTbl.erf_prd_catalog_product_id=:productId and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%')) and serviceTbl.IZO_SDWAN_SRVC_ID is null",nativeQuery=true)
	Integer getServiceCountByProduct(@Param("productId") Integer productId,@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);

	@Query(value = "select distinct serviceTbl.source_city as city from si_order ordTbl\r\n" + 
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id where serviceTbl.is_active='Y' and serviceTbl.service_status!='Terminated' and serviceTbl.erf_prd_catalog_product_id=:productId and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId)",nativeQuery=true)
	List<String> getDistictCityByProduct(@Param("productId") Integer productId,@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);
	
	@Query(value = "select distinct serviceTbl.source_city as city from si_order ordTbl\r\n" + 
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id where serviceTbl.is_active='Y' and serviceTbl.service_status!='Terminated' and ordTbl.erf_cust_le_id in (:leIds)",nativeQuery=true)
	List<String> getDistictCityByLeId(@Param("leIds") List<Integer> leIds);

	
	@Query(value = "select distinct serviceTbl.site_alias as city from si_order ordTbl\r\n" + 
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id where serviceTbl.is_active='Y' and serviceTbl.service_status!='Terminated' and serviceTbl.erf_prd_catalog_product_id=:productId and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%'))",nativeQuery=true)
	List<String> getDistictAliasByProduct(@Param("productId") Integer productId,@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);
	
	@Query(value = "select distinct serviceTbl.site_alias as city from si_order ordTbl\r\n" + 
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id where serviceTbl.is_active='Y' and serviceTbl.service_status!='Terminated' and ordTbl.erf_cust_le_id in (:leIds)",nativeQuery=true)
	List<String> getDistictAliasByLeId(@Param("leIds") List<Integer> leIds);

	@Query(value = "select distinct ordTbl.erf_cust_le_id as legalEntityId, ordTbl.erf_cust_le_name as legalEntityName from si_order ordTbl\r\n" +
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id where serviceTbl.is_active='Y' and serviceTbl.service_status!='Terminated' and serviceTbl.erf_prd_catalog_product_id=:productId and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%'))",nativeQuery=true)
	List<Map<String, Object>> getDistinctLeDetailsByProduct(@Param("productId") Integer productId,@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);
	
	@Query(value = "select distinct ordTbl.erf_cust_le_id as legalEntityId, ordTbl.erf_cust_le_name as legalEntityName from si_order ordTbl\r\n" +
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id where serviceTbl.service_status!='Terminated' and serviceTbl.erf_prd_catalog_product_id=:productId and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%'))",nativeQuery=true)
	List<Map<String, Object>> getDistinctLeDetailsByProductForInactiveCircuits(@Param("productId") Integer productId,@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);
	
	
	@org.springframework.data.jpa.repository.Query(value = NativeQueries.FETCH_GVPN_SERVICE_DATA_QUERY,
			countQuery = NativeQueries.FETCH_GVPN_SERVICE_DATA_COUNT_QUERY, nativeQuery = true)
	Page<Map<String, Object>> returnGVPNServiceData(@Param("productName") String productName, Pageable pageable);
	
	Page<SIServiceDetail> findAll(Specification<SIServiceDetail> specification, Pageable pageable);

	List<SIServiceDetail> findAll(Specification<SIServiceDetail> specification);
	
	@Query(value = "SELECT sa.attribute_name ,sa.attribute_value \r\n" + 
			"FROM si_service_detail ssd \r\n" + 
			"inner join si_service_attributes sa \r\n" + 
			"where ssd.tps_service_id=:tps_service_id \r\n" + 
			"and ssd.id=sa.SI_service_detail_id and trim(sa.attribute_name) in ('A_End_Port','B_End_Port','A_End_IP','B_End_IP');", nativeQuery = true)
	List<Map<String, Object>> findIPAttributeByServiceId(@Param("tps_service_id") String serviceId);
	
	Optional<SIServiceDetail> findByTpsServiceIdAndSiteType(String serviceId,String siteType);

	List<SIServiceDetail> findByBillingAccountId(String vpnId);

// Query calls for generating productFamily Details Excel sheet	starts
	
	/**
	 * Querying si_service_detail table to get productID for passed productName
	 * @param productName
	 * @return List of map
	 */
	@Query(value = "select DISTINCT erf_prd_catalog_product_id from si_service_detail where erf_prd_catalog_product_name =:productName", nativeQuery = true)
	List<Integer> getProductIdByProductName(@Param("productName") String productName);
	
	/**
	 * Querying si_service_detail to get set of values for specific attributeNames
	 * @param tpsID
	 * @return list of map
	 */
	@Query(value = "SELECT sa.attribute_name ,sa.attribute_value, ssd.tps_service_id as tpsServiceId \r\n" + 
			"FROM si_service_detail ssd \r\n" + 
			"inner join si_service_attributes sa \r\n" + 
			"where ssd.tps_service_id in (:tpsID) \r\n" + 
			"and ssd.id=sa.SI_service_detail_id and trim(sa.attribute_name) IN ('SERVICE_OPTION_TYPE','ROUTING_PROTOCOL','A_END_INTERFACE','B_END_INTERFACE') ;",
			nativeQuery = true)
	List<Map<String, Object>> fetchAttrValByServiceId(@Param("tpsID") List<String> tpsID);
	
	
	/**
	 * Querying si_service_detail to get set of values for specific attributeName
	 * @param tpsID
	 * @return list of map
	 */
	@Query(value = "SELECT ssd.site_end_interface as A_END_INTERFACE, ssd.tps_service_id as tpsService  \r\n" + 
			"FROM si_service_detail ssd  where ssd.tps_service_id in (:tpsID) ;",
			nativeQuery = true)
	List<Map<String, Object>> fetchAEndInterface(@Param("tpsID") List<String> tpsID);
	
	/**
	 * querying vw_si_service_info view to get specific attrs based on input serviceID
	 * @param servID
	 * @return list of map
	 */
	@Query(value = "select  srv_pri_sec_link as serviceLink ,order_sys_id as orderSysID, srv_source_city as originCity, srv_destination_country_code as destCntryCode,\r\n" + 
			" srv_access_type as accessType , parent_bundle_service_id as parentID,\r\n" +  
			" parent_bundle_service_name as parentService,  \r\n"+
			"order_customer as accountName,order_cust_le_name as legalEntity,\r\n" + 
			"srv_product_family_name as serviceType,srv_service_id as customerServiceID,\r\n" + 
			"srv_site_alias as  alias,srv_service_status as finalStatus,\r\n" + 
			"srv_source_country as sourceCountry, service_type as accessNumberType \r\n" + 
			"from vw_si_service_info_all where srv_service_id=:servID ", nativeQuery = true)
	List<Map<String, Object>> querySIServiceViewForAttr(@Param ("servID") String servID); 
	
	/**
	 * Querying vw_si_service_info view to get lastmile bandwidth
	 * @param servID
	 * @param family
	 * @return  list of map
	 */
	@Query(value = "select srv_site_type ,concat(srv_lastmile_bandwidth,' ',srv_lastmile_bandwidth_unit) as srv_lastmile_bandwidth, srv_service_id as mileServiceId \r\n"+ 
			"from vw_si_service_info where srv_site_type IN ('SiteA','SiteB') and \r\n"
			+ " srv_product_family_name= :family and srv_service_id in (:servIDs)", nativeQuery = true)
	List<Map<String, Object>> querySIServiceViewForLastMile(@Param ("servIDs") List<String> servIDs, @Param ("family") String family);
	
	
	/**
	 * Querying serviceAssetInfoView to get asset attrs for given serviceID
	 * @param serviceID
	 * @return list of map
	 */
	@Query(value = "SELECT asset_name,asset_type,origin_ntwrk as originNetwork, SCOPE_OF_MANAGEMENT as scopeOfManagement FROM vw_service_asset_info\r\n" + 
			" where  service_id = :serviceID ;", nativeQuery = true)
	List<Map<String,Object>> queryServiceAssetInfoView(@Param("serviceID") String serviceID);
	
	
	/**
	 * Querying orderAdditional info view to get values of passed attrNames
	 * @param order_sys_id
	 * @param attrName
	 * @return list of map
	 */
	@Query(value = "SELECT attribute_value as endCustName FROM vw_order_addtional_info\r\n" + 
			" where attribute_name = :attrName and order_sys_id = :order_sys_id ;", nativeQuery = true)
	List<Map<String,Object>> queryOrderAdditionalInfoView(@Param ("order_sys_id")String order_sys_id,@Param("attrName") String attrName);
	
	// Query calls for generating productFamily Details Excel sheet	ends

	/**
	 * Find order count based on productId and LeIds
	 * 
	 * @param productId, LeIds
	 * @return
	 */
	@Query(value = "select count(*) as count from si_order ordTbl\r\n" + 
            "inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id where serviceTbl.service_status!='Terminated' and serviceTbl.erf_prd_catalog_product_id=:productId and ordTbl.erf_cust_le_id in (:leIds)",nativeQuery=true)
	Integer getServiceCountByProductIdAndLeId(@Param("productId") Integer productId,@Param("leIds") List<Integer> leIds);

	/**
	 * Find order,service details based on serviceId and productId
	 * 
	 * @param serviceId, productId
	 * @return
	 */
	@Query(value = "select ssd.id as siServiceDetailId,ssd.erf_loc_source_city_id as city,ssd.site_type as siteType,"
			+ " ssd.service_status as serviceStatus,ssd.service_commissioned_date as commissionedDate,ssd.created_date as provisionedDate,ssd.billing_account_id as billingAccountId, "
			+ " ssd.billing_gst_number as billingGstNumber,sci.billing_address as billingAddress, sci.erf_cust_le_id as leId, sci.erf_cust_le_name as leName, "
			+ " sci.erf_cust_sp_le_id as supplierLeId,sci.billing_contact_id as billingContactId,sci.erf_cust_sp_le_name as supplierLeName, so.id as siOrderId, so.op_order_code as orderCode, "
			+ " ssd.tax_exemption_flag as taxExemptionFlag,sci.contract_start_date as contractStartDate, sci.contract_end_date as contractEndDate, "
			+ " sci.order_term_in_months as termInMonths,ssd.uuid as serviceId, ssd.erf_prd_catalog_product_id as productId,"
			+ " so.erf_cust_customer_id as customerId,ssc.contact_type as contactType, ssc.business_email as localItEmail,"
			+ " ssc.business_mobile as localItPhoneno,ssc.contact_name as localItName,so.created_date as orderDate,ssd.pop_site_code as dcLocation,ssd.IP_address_provided_by as ipAddressProvidedBy,group_concat(sa.id) as assetId"
			+ " from si_service_detail ssd"
			+ " join si_contract_info sci on sci.id = ssd.si_contract_info_id join si_order so on so.id = ssd.si_order_id"
			+ " left join si_service_contacts ssc on ssd.id=ssc.SI_service_detail_id join si_asset sa on sa.SI_service_detail_id = ssd.id where ssd.service_status!='Terminated' and ssd.uuid = :serviceId and ssd.erf_prd_catalog_product_id =:productId", nativeQuery = true)
	Map<String,Object> getServiceInfoBasedOnServiceIdAndProductId(@Param ("serviceId")String serviceId,@Param("productId") Integer productId);
	
	@Modifying
	@Query(value = "update si_service_detail ssd SET ssd.service_status = :status,ssd.is_active = :isActive WHERE ssd.id in(:serviceDetailIds)", nativeQuery = true)
	void updateServiceStatus(@Param ("status")String status,@Param("isActive") String isActive,@Param("serviceDetailIds") List<Integer> serviceDetailIds);

	SIServiceDetail findFirstByUuidAndServiceStatusIgnoreCaseAndIsActiveOrderByIdDesc(String uuid, String serviceStatus, String active);

	List<SIServiceDetail> findByTpsServiceIdAndServiceStatus(String serviceId, String status);
	
	List<SIServiceDetail> findByTpsServiceIdAndServiceStatusIn(String serviceId, List<String> status);

	Optional<SIServiceDetail> findTopByTpsServiceIdAndServiceStatusOrderByIdDesc(String serviceId, String serviceStatus);

	List<SIServiceDetail> findByUuidAndServiceStatusIgnoreCaseAndIsActive(String serviceId, String active, String status);
	
	@Query(value = "select distinct serviceTbl.source_city as city from si_order ordTbl\r\n" + 
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id where serviceTbl.is_active='Y' and serviceTbl.service_status!='Terminated' and serviceTbl.erf_prd_catalog_product_id=:productId and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId) and serviceTbl.site_type = :siteType",nativeQuery=true)
	List<String> getDistictCityByProductAndSiteType(@Param("productId") Integer productId,@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId, @Param("siteType") String siteType);
	
	@Query(value = "select sci.billing_currency as billingCurrency, ssi.site_type as siteType, ssi.bw_portspeed as baseBandwidth, ssi.bw_unit as bandwidthUnit from si_service_detail ssi left join si_contract_info sci on sci.id = ssi.si_contract_info_id where ssi.tps_service_id =:serviceId \r\n" + 
			"and ssi.erf_prd_catalog_product_name =:productName", nativeQuery = true)
	List<Map<String,Object>> findByTpsServiceIdAndProduct(String serviceId, String productName);
	
	List<SIServiceDetail> findByTpsServiceIdOrUuidAndServiceStatusIgnoreCaseAndIsActive(String tpsServiceId,String serviceId, String active, String status);
	
	SIServiceDetail findFirstByTpsServiceIdOrUuidAndServiceStatusIgnoreCaseAndIsActive(String tpsServiceId,String serviceId, String active, String status);
	
	@Query(value = "select site_address from si_service_detail where si_order_id = :siOrderId", nativeQuery = true)
	List<String> findBySiOrderId(@Param("siOrderId") String siOrderId);	

	@Query(value = "select o.erf_cust_le_name as customer_le_name, o.erf_cust_le_id as customer_le_id, pr.access_type as access_type, count(*) as no_count, " +
			"count(distinct sd.site_address) as site_count " +
			"from si_order o " +
			"inner join si_service_detail sd on sd.SI_order_id = o.id " +
			"inner join si_product_reference pr on sd.product_reference_id = pr.id " +
			"inner join si_asset_to_service ats on sd.id = ats.si_service_detail_id " +
			"inner join si_asset asset on asset.id = ats.SI_Asset_ID " +
			"where o.erf_cust_le_id IN (:customerLeIds) and o.tps_secs_id is not null and asset.type = :assetType and pr.sub_variant = :productName " +
			"and o.is_active='Y' and sd.is_active='Y' and pr.is_active='Y' and ats.is_active='Y' and asset.is_active='Y' group by o.erf_cust_le_id", nativeQuery = true)
	List<Map<String, Object>>  findSiteAddressConfigurationsByProductName(@Param("productName") String productName, @Param("customerLeIds") List<Integer> customerLeIds,@Param("assetType") String assetType);

	@Query(value = "select o.id as order_id,sd.site_address as site_address,asset.fqdn as number " +
			"from si_order o " +
			"inner join si_service_detail sd on sd.SI_order_id = o.id " +
			"inner join si_product_reference pr on sd.product_reference_id = pr.id " +
			"inner join si_asset_to_service ats on sd.id = ats.si_service_detail_id " +
			"inner join si_asset asset on asset.id = ats.SI_Asset_ID " +
			"where pr.sub_variant = :productName and pr.access_type = :accessType and o.erf_cust_le_id = :customerLeId and asset.type = :assetTypeTfn " +
			"and o.is_active='Y' and sd.is_active='Y' and pr.is_active='Y' and ats.is_active='Y' and asset.is_active='Y' group by sd.site_address", nativeQuery = true)
	List<Map<String, Object>> findSitesByCustomerLe(@Param("accessType") String accessType, @Param("customerLeId") Integer customerLeId, @Param("productName") String productName, @Param("assetTypeTfn") String assetTypeTfn);

	@Query(value = "select asset.fqdn as number " +
			"from si_order o " +
			"inner join si_service_detail sd on sd.SI_order_id = o.id " +
			"inner join si_product_reference pr on sd.product_reference_id = pr.id " +
			"inner join si_asset_to_service ats on sd.id = ats.si_service_detail_id " +
			"inner join si_asset asset on asset.id = ats.SI_Asset_ID " +
			"where pr.sub_variant = :productName and pr.access_type = :accessType and o.erf_cust_le_id = :customerLeId and asset.type = :assetTypeTfn " +
			"and o.is_active='Y' and sd.is_active='Y' and pr.is_active='Y' and ats.is_active='Y' and asset.is_active='Y' and sd.site_address = :siteAddress", nativeQuery = true)
	List<Map<String, Object>> findNumbersListBySiteAddress(@Param("accessType") String accessType, @Param("customerLeId") Integer customerLeId, @Param("productName") String productName, @Param("siteAddress") String siteAddress, @Param("assetTypeTfn") String assetTypeTfn);

	/**
	 * Method to fetch service details based on customer, le, source_cityA,source_cityA & siteType
	 * Method to 
	 * @param customerLeIds
	 * @param customerId
	 * @param sourceCityA
	 * @param sourceCityB
	 * @return
	 */
	@Query(value = "SELECT ssi.tps_service_id FROM si_service_detail ssi INNER JOIN si_order sio ON sio.id = ssi.si_order_id \r\n" + 
			"WHERE   sio.erf_cust_le_id IN (:customerLeIds) AND sio.erf_cust_customer_id =:customerId  AND \r\n" + 
			"ssi.service_Status != 'Terminated' AND ssi.erf_prd_catalog_product_name='GDE'\r\n" + 
			"AND (source_city=:sourceCityB AND site_type='SiteB' ) AND ssi.tps_service_id  IN\r\n" + 
			"(SELECT ssi.tps_service_id FROM si_service_detail ssi INNER JOIN si_order sio ON sio.id = ssi.si_order_id \r\n" + 
			"WHERE   sio.erf_cust_le_id IN (:customerLeIds) AND sio.erf_cust_customer_id =:customerId  AND \r\n" + 
			"ssi.service_Status != 'Terminated' AND ssi.erf_prd_catalog_product_name='GDE'\r\n" + 
			"AND (source_city=:sourceCityA and site_type='SiteA' )) ", nativeQuery = true)
	List<String> findBySiteACityAndSiteBCity(@Param("customerLeIds") List<Integer> customerLeIds, @Param("customerId") Integer customerId, @Param("sourceCityA") String sourceCityA,@Param("sourceCityB") String sourceCityB);
	
	/**
	 * Method to fetch service details based on customer, le, source_city & siteType
	 * @param customerLeIds
	 * @param customerId
	 * @param sourceCity
	 * @param siteType
	 * @return
	 */
	@Query(value = "SELECT ssi.tps_service_id FROM si_service_detail ssi INNER JOIN si_order sio ON sio.id = ssi.si_order_id \r\n" + 
			"WHERE   sio.erf_cust_le_id IN (:customerLeIds) AND sio.erf_cust_customer_id =:customerId  AND \r\n" + 
			"ssi.service_Status != 'Terminated' AND ssi.erf_prd_catalog_product_name='GDE'\r\n" + 
			"AND (source_city=:sourceCity and site_type=:siteType )", nativeQuery = true)
	List<String> findBySourceCityAndSiteType(@Param("customerLeIds") List<Integer> customerLeIds, @Param("customerId") Integer customerId, @Param("sourceCity") String sourceCity,@Param("siteType") String siteType);
	
	@Query(value = "select distinct serviceTbl.source_city\n" +
			"from\n" +
			"si_order ordTbl\n" +
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\n" +
			"where serviceTbl.service_status!='Terminated' and serviceTbl.is_active = 'Y' and ordTbl.is_active ='Y'\n" +
			"and ordTbl.erf_cust_customer_id= :customerId and serviceTbl.erf_prd_catalog_product_name='GSIP' and serviceTbl.access_type=:accessType\n" +
			"order by serviceTbl.vpn_name,serviceTbl.tps_service_id, serviceTbl.primary_tps_service_id, serviceTbl.service_sequence,serviceTbl.service_commissioned_date", nativeQuery = true)
	List<String> getDistinctCityOfGscServices(Integer customerId, String accessType);

	@Query(value = "select distinct serviceTbl.site_alias\n" +
			"from\n" +
			"si_order ordTbl\n" +
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\n" +
			"where serviceTbl.service_status!='Terminated' and serviceTbl.is_active = 'Y' and ordTbl.is_active ='Y'\n" +
			"and ordTbl.erf_cust_customer_id= :customerId and serviceTbl.erf_prd_catalog_product_name='GSIP' and serviceTbl.access_type=:accessType\n" +
			"and serviceTbl.site_alias is not null\n" +
			"order by serviceTbl.vpn_name,serviceTbl.tps_service_id, serviceTbl.primary_tps_service_id, serviceTbl.service_sequence,serviceTbl.service_commissioned_date", nativeQuery = true)
	List<String> getDistinctAliasOfGscServices(Integer customerId, String accessType);

	@Query(value = "select distinct ordTbl.erf_cust_le_id as legalEntityId, ordTbl.erf_cust_le_name as legalEntityName\n" +
			"from\n" +
			"si_order ordTbl\n" +
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\n" +
			"where serviceTbl.service_status!='Terminated' and serviceTbl.is_active = 'Y' and ordTbl.is_active ='Y'\n" +
			"and ordTbl.erf_cust_customer_id= :customerId and serviceTbl.erf_prd_catalog_product_name='GSIP' and serviceTbl.access_type=:accessType\n" +
			"order by serviceTbl.vpn_name,serviceTbl.tps_service_id, serviceTbl.primary_tps_service_id, serviceTbl.service_sequence,serviceTbl.service_commissioned_date", nativeQuery = true)
	List<Map<String, Object>> getDistinctLeOfGscServices(Integer customerId, String accessType);
	
	@Query(value = "select count(*) as count from si_order ordTbl\r\n" + 
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id where serviceTbl.service_status!='Terminated' and serviceTbl.erf_prd_catalog_product_id=:productId and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%')) and serviceTbl.IZO_SDWAN_SRVC_ID is null",nativeQuery=true)
	Integer getServiceCountByStandaloneProduct(@Param("productId") Integer productId,@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);


	@Query(value = "select count(*) as count from si_order ordTbl \r\n" +
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id \r\n" +
			"LEFT JOIN vw_service_inv_ui_exclusion siuv ON serviceTbl.tps_service_id = siuv.service_id AND siuv.is_cust_prtl_exclusion_flg='Y' \r\n" +
			"where siuv.service_id IS NULL and serviceTbl.service_status!='Terminated' \r\n" +
			"and serviceTbl.erf_prd_catalog_product_name in (:productNames) and \r\n" +
			"(ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) \r\n" +
			"and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId\r\n" +
			"and (ordTbl.opportunity_classification like '%sell with%' \r\n" +
			"or ordTbl.opportunity_classification like '%sell through%')) and serviceTbl.IZO_SDWAN_SRVC_ID in (\r\n" +
			"select tps_service_id from si_order ordTbl inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id \r\n" +
			"LEFT JOIN vw_service_inv_ui_exclusion siuv ON serviceTbl.tps_service_id = siuv.service_id AND siuv.is_cust_prtl_exclusion_flg='Y' \r\n" +
			"where siuv.service_id IS NULL and serviceTbl.service_status NOT IN ('Terminated','Under Provisioning')   and serviceTbl.erf_prd_catalog_product_id=:productId \r\n" +
			"and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null \r\n" +
			"or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId \r\n" +
			"and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%')))",nativeQuery=true)
	Integer getNetworkServiceCount(@Param("productNames") List<String> productNames,@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId,@Param("productId") Integer productId);

	@Query(value = "select distinct serviceTbl.site_alias as city from si_order ordTbl " +
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id where serviceTbl.is_active='Y' and serviceTbl.service_status!='Terminated' and serviceTbl.erf_prd_catalog_product_id=:productId and ((:leIds) is null or ordTbl.erf_cust_le_id in (:leIds)) and ((:partnerLeIds) is null or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%'))",nativeQuery=true)
	List<String> getDistictAliasByLe(@Param("productId") Integer productId,@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);

	@Query(value = "select distinct serviceTbl.source_city as city from si_order ordTbl " +
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id where serviceTbl.is_active='Y' and serviceTbl.service_status!='Terminated' and serviceTbl.erf_prd_catalog_product_id=:productId and (:leIds is null or ordTbl.erf_cust_le_id in (:leIds)) and (:partnerLeIds is null or ordTbl.erf_cust_partner_le_id in (:partnerLeIds))  and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId)",nativeQuery=true)
	List<String> getDistictCityByLe(@Param("productId") Integer productId,@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);

	@Query(value = "select count(*) as count from si_order ordTbl\r\n" +
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id \r\n" +
			"LEFT JOIN vw_service_inv_ui_exclusion siuv ON serviceTbl.tps_service_id = siuv.service_id AND siuv.is_cust_prtl_exclusion_flg='Y' \r\n" +
			"where siuv.service_id IS NULL and serviceTbl.service_status NOT IN ('Terminated','Under Provisioning') \r\n" +
			"and serviceTbl.erf_prd_catalog_product_id=:productId \r\n" +
			"and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) \r\n" +
			"and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) \r\n" +
			"and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId \r\n" +
			"and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%')) \r\n" +
			"and serviceTbl.IZO_SDWAN_SRVC_ID is null",nativeQuery=true)
	Integer getServiceCountByStandaloneIzoSdwan(@Param("productId") Integer productId,@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);

	@Query(value = "select o.erf_cust_le_id as customer_le_id, o.erf_cust_le_name as customer_le_name,pr.access_type as access_type,o.tps_secs_id as secs_id,asset.fqdn as tollFreeNumber " +
			"from si_order o " +
			"inner join si_service_detail sd on sd.SI_order_id = o.id " +
			"inner join si_product_reference pr on sd.product_reference_id = pr.id " +
			"inner join si_asset_to_service ats on sd.id = ats.si_service_detail_id " +
			"inner join si_asset asset on asset.id = ats.SI_Asset_ID " +
			"where o.erf_cust_customer_id = :customerId and asset.type = :assetType and pr.sub_variant = :productName and " +
			"((pr.sub_variant != 'Domestic Voice' and (sd.site_address is null or sd.site_address is not null )) or (pr.sub_variant = 'Domestic Voice' and sd.site_address is not null)) " +
			"and o.is_active='Y' and sd.is_active='Y' and pr.is_active='Y' and ats.is_active='Y' and asset.is_active='Y'", nativeQuery = true)
	List<Map<String, Object>>  findAllConfigurationDetails(@Param("productName") String productName, @Param("customerId") Integer customerId,@Param("assetType") String assetType);

	@Query(nativeQuery = true, value = "select * from si_service_detail where tps_service_id =:serviceId and is_active =:status")
	List<SIServiceDetail> findByTpsServiceIdAndActiveStatus(@Param("serviceId") String serviceId, @Param("status") String activeStatus);

	SIServiceDetail findFirstByTpsServiceIdAndServiceStatusAndIsActive(String serviceCode, String active, String activeStatus);

	Optional<SIServiceDetail> findByTpsServiceIdAndIsActive(String tpsServiceId, String active);
	
	SIServiceDetail findFirstByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveOrUuidAndServiceStatusIgnoreCaseAndIsActive(String tpsServiceId,String tpsActive, String tpsStatus,String serviceId, String uuidActive, String uuidStatus);
	
	/** Replaced for perm-fix **/
	List<SIServiceDetail> findByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActive(String serviceId, String active, String status);
	List<SIServiceDetail> findByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveAndErfLocSiteAddressIdIsNotNull(String serviceId, String active, String status);
	List<SIServiceDetail> findByPrimaryTpsSrviceIdAndServiceStatusIgnoreCaseAndIsActiveAndErfLocSiteAddressIdIsNotNullOrderByIdAsc(String serviceId, String active, String status);
	
	List<SIServiceDetail> findByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveOrUuidAndServiceStatusIgnoreCaseAndIsActive(String tpsServiceId,String tpsActive, String tpsStatus,String serviceId, String uuidActive, String uuidStatus);
	
	@Query(nativeQuery = true, value = "select * from si_service_detail where tps_service_id =:serviceId")
	List<SIServiceDetail> findByTpsServiceIdForValidation(@Param("serviceId") String serviceId);
	List<SIServiceDetail> findByPrimaryTpsSrviceId(String serviceId);
	List<SIServiceDetail> findByPrimaryTpsSrviceIdAndServiceStatusIgnoreCaseAndIsActiveAndErfLocSiteAddressIdIsNotNull(String serviceId, String active, String status);
	
	List<SIServiceDetail> findByTpsServiceIdAndCircuitStatusIgnoreCaseAndIsActiveAndErfLocSiteAddressIdIsNotNull(String serviceId, String active, String status);
	List<SIServiceDetail> findByTpsServiceIdAndCircuitStatusIgnoreCaseAndIsActive(String serviceId, String active, String status);
	List<SIServiceDetail> findByPrimaryTpsSrviceIdAndCircuitStatusIgnoreCaseAndIsActiveAndErfLocSiteAddressIdIsNotNullOrderByIdAsc(String serviceId, String active, String status);
	@Query(value="SELECT a.service_status as serviceStatus,a.primary_tps_service_id as primaryTpsServiceId , a.remarks as remarks,a.circuit_status as circuitStatus,a.is_active as isActive,a.erf_loc_site_address_id as erfLocSiteAddressId ,a.tps_service_id as tpsServiceId,b.erf_cust_customer_id as erfCustCustomerId,a.erf_prd_catalog_product_name as erfPrdCatalogProductName,b.erf_cust_le_id as erfCustLeId,a.erf_prd_catalog_offering_name as erfPrdCatalogOfferingName FROM si_service_detail a,si_order b WHERE a.si_order_id = b.id and (a.primary_tps_service_id in (:serviceIds) or a.tps_service_id in (:serviceIds))",nativeQuery = true)
	List<Map<String,Object>> findByTpsServiceId(@Param("serviceIds") List<String> serviceIds);
//	
//	@Query(value="SELECT b.id as siOrderid,b.sfdc_account_id as sfdcAccountId,c.payment_currency as paymentCurrency,c.billing_currency as billingCurrency,c.billing_contact_id as billingContactId,c.payment_term as paymentTerm,c.billing_method as billingMethod,c.billing_frequency as billingFrequency,c.customer_contact_email as customerContactEmail,c.customer_contact as customerContact,c.account_manager_email as accountManagerEmail,c.account_manager as accountManager,c.tps_sfdc_cuid as tpsSfdcCuid,c.erf_cust_sp_le_name as erfCustSpLeName,c.erf_cust_le_name as erfCustLeName,a.arc as arc,a.mrc as mrc,a.nrc as nrc,a.access_type as accessType,a.id as serviceId,a.bw_portspeed as bwPortSpeed, a.bw_unit as bwPortSpeedUnit, a.burstable_bw_portspeed as burstableBwPortSpeed,a.burstable_bw_unit as burstableBwPortSpeedUnit, a.service_type as serviceType,a.service_status as serviceStatus,a.primary_tps_service_id as primaryTpsServiceId , a.remarks as remarks,a.circuit_status as circuitStatus,a.is_active as isActive,a.erf_loc_site_address_id as erfLocSiteAddressId ,a.tps_service_id as tpsServiceId,b.erf_cust_customer_id as erfCustCustomerId,a.erf_prd_catalog_product_name as erfPrdCatalogProductName,b.erf_cust_le_id as erfCustLeId,a.erf_prd_catalog_offering_name as erfPrdCatalogOfferingName, a.primary_secondary as primaryOrSecondary FROM si_service_detail a,si_order b,si_contract_info c WHERE c.SI_order_id=b.id and a.si_order_id = b.id and (a.primary_tps_service_id in (:serviceIds) or a.tps_service_id in (:serviceIds)) and a.is_active=:isActive and erf_loc_site_address_id is not null and a.service_status=:serviceStatus",nativeQuery = true)
//	List<Map<String,Object>> findByPrimaryTpsSrviceIdOrTpsServiceId(@Param("serviceIds") List<String> serviceIds,@Param("serviceStatus") String serviceStatus,@Param("isActive") String isActive);
	
	
	@Query(value="SELECT b.id as siOrderid,b.sfdc_account_id as sfdcAccountId,c.payment_currency as paymentCurrency,c.billing_currency as billingCurrency,c.billing_contact_id as billingContactId,c.payment_term as paymentTerm,c.billing_method as billingMethod,c.billing_frequency as billingFrequency,c.customer_contact_email as customerContactEmail,c.customer_contact as customerContact,c.account_manager_email as accountManagerEmail,c.account_manager as accountManager,c.tps_sfdc_cuid as tpsSfdcCuid,c.erf_cust_sp_le_name as erfCustSpLeName,c.erf_cust_le_name as erfCustLeName,a.arc as arc,a.mrc as mrc,a.nrc as nrc,a.access_type as accessType,a.id as serviceId,a.bw_portspeed as bwPortSpeed, a.bw_unit as bwPortSpeedUnit, a.burstable_bw_portspeed as burstableBwPortSpeed,a.burstable_bw_unit as burstableBwPortSpeedUnit, a.service_type as serviceType,a.service_status as serviceStatus,a.primary_tps_service_id as primaryTpsServiceId , a.remarks as remarks,a.circuit_status as circuitStatus,a.is_active as isActive,a.erf_loc_site_address_id as erfLocSiteAddressId ,a.tps_service_id as tpsServiceId,b.erf_cust_customer_id as erfCustCustomerId,a.erf_prd_catalog_product_name as erfPrdCatalogProductName,b.erf_cust_le_id as erfCustLeId,a.erf_prd_catalog_offering_name as erfPrdCatalogOfferingName, a.primary_secondary as primaryOrSecondary, a.lastmile_bw as lastmileBw, a.lastmile_bw_unit as lastmileUnit , a.tps_copf_id, a.tax_exemption_flag FROM si_service_detail a,si_order b,si_contract_info c WHERE c.SI_order_id=b.id and a.si_order_id = b.id and ( a.tps_service_id in (:serviceIds)) and a.is_active=:isActive and erf_loc_site_address_id is not null and a.service_status=:serviceStatus",nativeQuery = true)
	List<Map<String,Object>> findByPrimaryTpsSrviceIdOrTpsServiceId(@Param("serviceIds") List<String> serviceIds,@Param("serviceStatus") String serviceStatus,@Param("isActive") String isActive);
	
	@Query(value="SELECT a.primary_tps_service_id as primaryTpsServiceId , a.remarks as remarks,a.circuit_status as circuitStatus,a.is_active as isActive,a.erf_loc_site_address_id as erfLocSiteAddressId ,a.tps_service_id as tpsServiceId,b.erf_cust_customer_id as erfCustCustomerId,a.erf_prd_catalog_product_name as erfPrdCatalogProductName,b.erf_cust_le_id as erfCustLeId,a.erf_prd_catalog_offering_name as erfPrdCatalogOfferingName, a.service_status as serviceStatus FROM si_service_detail a,si_order b WHERE a.si_order_id = b.id and (a.tps_service_id in (:serviceIds)) ",nativeQuery = true)
    List<Map<String,Object>> findSIByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveAndErfLocSiteAddressIdIsNotNull(@Param("serviceIds") List<String> serviceIds);
    
	@Query(value="SELECT a.primary_tps_service_id as primaryTpsServiceId , a.remarks as remarks,a.circuit_status as circuitStatus,a.is_active as isActive,a.erf_loc_site_address_id as erfLocSiteAddressId ,a.tps_service_id as tpsServiceId,b.erf_cust_customer_id as erfCustCustomerId,a.erf_prd_catalog_product_name as erfPrdCatalogProductName,b.erf_cust_le_id as erfCustLeId,a.erf_prd_catalog_offering_name as erfPrdCatalogOfferingName, a.service_status as serviceStatus FROM si_service_detail a,si_order b WHERE a.si_order_id = b.id and (a.tps_service_id in (:serviceIds)) and a.is_active='Y' and a.erf_loc_site_address_id is not null and a.service_status='Active'",nativeQuery = true)
    List<Map<String,Object>> findSIByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveAndErfLocSiteAddressId(@Param("serviceIds") List<String> serviceIds);

	SIServiceDetail findFirstByTpsServiceIdAndSiOrderUuidOrderByIdDesc(String serviceCode, String orderCode);

	SIServiceDetail findByUuidAndSiOrderUuid(String uuid, String opOrderCode);
}
