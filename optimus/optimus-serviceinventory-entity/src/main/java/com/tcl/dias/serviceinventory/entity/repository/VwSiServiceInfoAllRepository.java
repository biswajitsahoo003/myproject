package com.tcl.dias.serviceinventory.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.ViewSiServiceInfoAll;

/**
 * 
 * This is the repository class for VwSiServiceInfoAll
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface VwSiServiceInfoAllRepository extends JpaRepository<ViewSiServiceInfoAll, Integer>{
	
	public List<ViewSiServiceInfoAll> findByOrderCustomerId(Integer customerId);
	
	@Query(value="select * from vw_si_service_info_all where order_customer_id=:customerId and order_cust_le_id in(:leIds) and srv_service_classification like '%Dual CPE%' and srv_lat_long is not null and erf_loc_site_address_id is not null",nativeQuery = true)
	public List<ViewSiServiceInfoAll> selectByOrderCustomerId(Integer customerId,List<Integer> leIds);
	
	public List<ViewSiServiceInfoAll> findByOrderCustomerIdAndSourceCountryInAndProductFamilyNameIn(Integer customerId,List<String> countries,List<String> products);
	
	public List<ViewSiServiceInfoAll> findByOrderCustomerIdAndSourceCountryInAndProductFamilyNameInAndOrderCustLeIdIn(Integer customerId,List<String> countries,List<String> products,List<Integer> leIds);
	
	@Query(value="select srv_port_mode from vw_si_service_info_all where srv_service_id=:id",nativeQuery = true)
	public String findPortModeByServiceId(String id);

	List<ViewSiServiceInfoAll> findByServiceIdIn(List<String> serviceIds);
	
	List<ViewSiServiceInfoAll> findByServiceIdInAndServiceStatusNotIn(List<String> serviceIds,List<String> status);
	
	Optional<ViewSiServiceInfoAll> findByServiceId(String serviceId);
	
	@Query(value = "SELECT  sys_id as sysId,order_sys_id as orderSysId, order_customer_id as orderCustomerId ,order_cust_le_id as orderCustLeId,\r\n" +
			"srv_site_alias as siteAlias, srv_service_id as serviceId, izo_sdwan_srvc_id as izoSdwanSrvcId,\r\n" +
			"srv_product_family_id as productFamilyId, srv_product_offering_name as productOfferingName, srv_product_family_name as productFamilyName,\r\n" +
			"srv_product_offering_id as productOfferingId, srv_primary_service_id as primaryServiceId, \r\n" +
			"srv_pri_sec as primaryOrSecondary, srv_pri_sec_link as primarySecondaryLink, srv_lat_long as latLong, \r\n" +
			"srv_service_classification as serviceClassification,srv_vpn_name as vpnName, srv_access_type as accessType,\r\n" +
			"srv_customer_site_address as customerSiteAddress, srv_bandwidth as bandwidth, srv_bandwidth_unit as bandwidthUnit,\r\n" +
			"srv_lastmile_bandwidth as lastMileBandwidth,srv_lastmile_bandwidth_unit as lastMileBandwidthUnit,\r\n" +
			"srv_source_country as sourceCountry,srv_service_status as serviceStatus, srv_lastmile_provider as lastMileProvider,\r\n" +
			"srv_destination_country as destinationCountry, srv_destination_city as destinationCity,srv_source_city as sourceCity, \r\n" +
			"order_is_active as orderIsActive,erf_loc_site_address_id as  locationId, order_term_in_months as orderTermInMonths,\r\n" +
			"is_active as isActive, opportunity_id as opportunityId, order_partner as orderPartner ,erf_cust_partner_le_id as erfCustPartnerLeId\r\n" +
			"FROM vw_si_service_info_all vwinfo \r\n" +
			"LEFT JOIN vw_service_inv_ui_exclusion siuv ON  srv_service_id= siuv.service_id AND siuv.is_cust_prtl_exclusion_flg='Y' \r\n" +
			"where siuv.service_id IS NULL \r\n" +
			"and (order_cust_le_id in (:customerLeIds) or erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or order_customer_id = :customerId) \r\n" +
			"and (:partnerId is null or order_partner=:partnerId)\r\n" +
			"and srv_product_family_id = :productId and srv_service_status NOT IN ('Terminated','Under Provisioning')", nativeQuery = true)
	List<Map<String,Object>> findSdwanSiteDetails(@Param("customerLeIds") List<Integer> customerLeIds, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);


	@Query(value = "SELECT  sys_id as sysId,order_sys_id as orderSysId, order_customer_id as orderCustomerId ,order_cust_le_id as orderCustLeId,\r\n" +
			"srv_site_alias as siteAlias, srv_service_id as serviceId, izo_sdwan_srvc_id as izoSdwanSrvcId,\r\n" +
			"srv_product_family_id as productFamilyId, srv_product_offering_name as productOfferingName, srv_product_family_name as productFamilyName,\r\n" +
			"srv_product_offering_id as productOfferingId, srv_primary_service_id as primaryServiceId, \r\n" +
			"srv_pri_sec as primaryOrSecondary, srv_pri_sec_link as primarySecondaryLink, srv_lat_long as latLong,\r\n" +
			"srv_service_classification as serviceClassification,srv_vpn_name as vpnName, srv_access_type as accessType,\r\n" +
			"srv_customer_site_address as customerSiteAddress, srv_bandwidth as bandwidth, srv_bandwidth_unit as bandwidthUnit,\r\n" +
			"srv_lastmile_bandwidth as lastMileBandwidth,srv_lastmile_bandwidth_unit as lastMileBandwidthUnit,\r\n" +
			"srv_source_country as sourceCountry,srv_service_status as serviceStatus, srv_lastmile_provider as lastMileProvider,\r\n" +
			"srv_destination_country as destinationCountry, srv_destination_city as destinationCity,srv_source_city as sourceCity, \r\n" +
			"order_is_active as orderIsActive,erf_loc_site_address_id as  locationId, order_term_in_months as orderTermInMonths,\r\n" +
			"is_active as isActive, opportunity_id as opportunityId, order_partner as orderPartner ,erf_cust_partner_le_id as erfCustPartnerLeId\r\n" +
			"FROM vw_si_service_info_all \r\n" +
			"LEFT JOIN vw_service_inv_ui_exclusion siuv ON  srv_service_id= siuv.service_id AND siuv.is_cust_prtl_exclusion_flg='Y' \r\n" +
			"where siuv.service_id IS NULL \r\n" +
			"and (order_cust_le_id in (:customerLeIds) or erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or order_customer_id = :customerId) \r\n" +
			"and (:partnerId is null or order_partner=:partnerId)\r\n" +
			"and srv_product_family_id = :productId and srv_service_status NOT IN ('Terminated','Under Provisioning') limit :page , :size ", nativeQuery = true)
	List<Map<String,Object>> findSdwanSiteDetailsWithPageLimit(@Param("customerLeIds") List<Integer> customerLeIds, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId,@Param("page") Integer page,@Param("size") Integer size);
	
	@Query(value="SELECT  sys_id as sysId,order_sys_id as orderSysId, order_customer_id as orderCustomerId ,order_cust_le_id as orderCustLeId,\r\n" + 
			"srv_site_alias as siteAlias, srv_service_id as serviceId, izo_sdwan_srvc_id as izoSdwanSrvcId,\r\n" + 
			"srv_product_family_id as productFamilyId, srv_product_offering_name as productOfferingName,\r\n" + 
			"srv_product_offering_id as productOfferingId, srv_primary_service_id as primaryServiceId, \r\n" + 
			"srv_pri_sec as primaryOrSecondary, srv_pri_sec_link as primarySecondaryLink, srv_lat_long as latLong,\r\n" + 
			"srv_service_classification as serviceClassification,srv_vpn_name as vpnName, srv_access_type as accessType,\r\n" + 
			"srv_customer_site_address as customerSiteAddress, srv_bandwidth as bandwidth, srv_bandwidth_unit as bandwidthUnit,\r\n" + 
			"srv_lastmile_bandwidth as lastMileBandwidth,srv_lastmile_bandwidth_unit as lastMileBandwidthUnit,\r\n" + 
			"srv_source_country as sourceCountry,srv_service_status as serviceStatus, srv_lastmile_provider as lastMileProvider,\r\n" + 
			"srv_destination_country as destinationCountry, srv_destination_city as destinationCity,srv_source_city as sourceCity, \r\n" + 
			"order_is_active as orderIsActive,erf_loc_site_address_id as  locationId, order_term_in_months as orderTermInMonths,\r\n" + 
			"is_active as isActive, opportunity_id as opportunityId, order_partner as orderPartner ,erf_cust_partner_le_id as erfCustPartnerLeId\r\n" + 
			"FROM vw_si_service_info_all vwinfo\r\n" + 
			"where izo_sdwan_srvc_id in (:izoSdwanServiceIds) and srv_service_status != 'Terminated'",nativeQuery= true)
	List<Map<String,Object>> findByIzoSdwanServiceIdsIn(@Param("izoSdwanServiceIds") List<String> izoSdwanServiceIds);
	
	@Query(value = "select count(*) AS totalSiteCount FROM vw_si_service_info_all  \r\n" +
			"LEFT JOIN vw_service_inv_ui_exclusion siuv ON  srv_service_id= siuv.service_id AND siuv.is_cust_prtl_exclusion_flg='Y' \r\n" +
			"where siuv.service_id IS NULL \r\n" +
			"and (order_cust_le_id in (:customerLeIds) or erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or order_customer_id = :customerId) \r\n" +
			"and (:partnerId is null or order_partner=:partnerId)\r\n" +
			"and srv_product_family_id = :productId and srv_service_status NOT IN ('Terminated','Under Provisioning') ", nativeQuery = true)
	public Integer findSdwanSiteCount(@Param("customerLeIds") List<Integer> customerLeIds, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);
	
	Page<ViewSiServiceInfoAll> findAll(Specification<ViewSiServiceInfoAll> specification, Pageable pageable);

	/**
	 *
	 * for finding the underlay service id by SDWAN Service id
	 * 
	 * @param sdwanServiceId
	 * @return
	 */
	@Query(value = "SELECT sys_id as sys_id, srv_service_id as service_id, srv_site_alias as site_alias, srv_source_country as source_country, srv_source_city as source_city, srv_destination_city as destination_city, srv_destination_country as destination_country, izo_sdwan_srvc_id as sdwan_id,srv_product_family_name as underlayProductName, asset.asset_name, asset.asset_sys_id FROM vw_si_service_info_all left join vw_service_asset_info asset on sys_id = asset.srv_sys_id where srv_service_status NOT IN ('Terminated') and izo_sdwan_srvc_id in (:serviceIds) and asset_tag='SDWAN CPE'", nativeQuery = true)
	List<Map<String, Object>> findUnderlayServiceIdbySdwanServiceId(@Param("serviceIds") List<String> sdwanServiceId);

	@Query(value = "SELECT sys_id as sys_id, srv_service_id as service_id, srv_site_alias as site_alias, srv_source_country as source_country, srv_source_city as source_city, srv_destination_city as destination_city, srv_destination_country as destination_country, izo_sdwan_srvc_id as sdwan_id,srv_product_family_name as underlayProductName, asset.asset_name, asset.asset_sys_id FROM vw_si_service_info_all left join vw_service_asset_info asset on sys_id = asset.srv_sys_id where srv_service_status NOT IN ('Terminated') and izo_sdwan_srvc_id in (:serviceIds) and asset_tag='SDWAN CPE' group by asset.asset_name", nativeQuery = true)
	List<Map<String, Object>> findUnderlaysBySdwanServiceIdGroupedByName(
			@Param("serviceIds") List<String> sdwanServiceId);

	@Query(value = "select vw.sys_id as sysId, vw.srv_service_id as serviceId, vw.srv_product_family_name as productFamily,vw.srv_product_offering_name as offeringName, \r\n" + 
			"vw.srv_primary_service_id as primaryServiceId,vw.srv_pri_sec as primaryOrSecondary, vw.srv_pri_sec_link as primarySecondaryLink, \r\n" + 
			"vw.srv_customer_site_address as siteAddress, vw.srv_source_country as sourceCountry,vw.srv_source_city as sourceCity, \r\n"+
			"vw.srv_destination_country as destCountry,vw.srv_destination_city as destCity, \r\n" +
			"vw.order_customer_id as customerId,vw.order_cust_le_id as customerLeId, vw.is_active as  isActive, vw.srv_site_alias as siteAlias,\r\n" + 
			" vw.izo_sdwan_srvc_id as izoSdwanServiceId,va.attribute_name as attributeName, va.attribute_value as attributeValue \r\n" + 
			"from vw_si_service_info_all vw \r\n" + 
			"left join vw_service_additional_info va on va.srv_sys_id = vw.sys_id \r\n" + 
			"where vw.srv_service_id in (:serviceId) and vw.srv_product_family_id =:productId and \r\n" + 
			"va.attribute_name in (:attrNames) and vw.srv_service_status NOT IN ('Terminated','Under Provisioning') ", nativeQuery = true)
	List<Map<String,Object>> getSiServiceDetailsAttrByServiceId(@Param("serviceId") List<String> serviceId, @Param("productId") Integer productId, @Param("attrNames") List<String> attrNames);

	@Query(value = "SELECT sys_id as sys_id, srv_service_id as service_id,srv_primary_service_id as primaryServiceId,srv_pri_sec as primaryOrSecondary,\r\n" + 
			"srv_customer_site_address as siteAddress, srv_source_country as sourceCountry,srv_source_city as sourceCity,srv_destination_country as destCountry,\r\n" + 
			"srv_destination_city as destCity,order_customer_id as customerId,order_cust_le_id as customerLeId, is_active as  isActive,srv_pri_sec_link as primarySecondaryLink, izo_sdwan_srvc_id as sdwanServiceId " +
			"FROM vw_si_service_info_all where (order_cust_le_id in (:customerLeIds) or erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or order_customer_id = :customerId)\r\n" + 
			"and (:partnerId is null or order_partner=:partnerId)\r\n" + 
			"and srv_product_family_id =:productId and srv_service_status != 'Terminated' and sys_id in (:sysIds)", nativeQuery = true)
	List<Map<String, Object>> findBySysIdsIn(@Param("customerLeIds") List<Integer> customerLeIds,
			@Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId,
			@Param("customerId") Integer customerId, @Param("partnerId") Integer partnerId,
			@Param("sysIds") List<Integer> sysIds);
	
	@Query(value = "select group_concat(va.attribute_sys_id) as attributeId,va.attribute_value as attributeValue, group_concat(va.srv_sys_id ) as serviceId, va.disp_val as dispValue \r\n"
			+	"FROM vw_si_service_info_all vw\r\n" + 
	"left join vw_service_additional_info va on va.srv_sys_id = vw.sys_id where \r\n" + 
	"(vw.order_cust_le_id in (:customerLeIds) or vw.erf_cust_partner_le_id in (:partnerLeIds)) \r\n" + 
	"and (:customerId is null or vw.order_customer_id =:customerId) \r\n" + 
	"and (:partnerId is null or vw.order_partner=:partnerId) \r\n" + 
	"and  vw.srv_product_family_id =:productId and vw.srv_service_status != 'Terminated' \r\n" + 
			"and (va.attribute_name =:attrName and (va.attribute_value like %:searchText% or va.disp_val like %:searchText%)) \r\n"
			+	"group by va.attribute_value \r\n" + 
	"order by va.attribute_value desc ", nativeQuery = true)
	List<Map<String, Object>> findDistinctTemplateAttrValueOrderByDesc(@Param("searchText") String searchText, @Param("customerId") Integer customerId, @Param("customerLeIds")  List<Integer> customerLeIds, @Param("partnerId") Integer partnerId, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId, @Param("attrName") String attrName);

	@Query(value = "select group_concat(va.attribute_sys_id) as attributeId,va.attribute_value as attributeValue, group_concat(va.srv_sys_id ) as serviceId, va.disp_val as dispValue \r\n"
			+	"FROM vw_si_service_info_all vw\r\n" +
			"left join vw_service_additional_info va on va.srv_sys_id = vw.sys_id where \r\n" +
			"(vw.order_cust_le_id in (:customerLeIds) or vw.erf_cust_partner_le_id in (:partnerLeIds)) \r\n" +
			"and (:customerId is null or vw.order_customer_id =:customerId) \r\n" +
			"and (:partnerId is null or vw.order_partner=:partnerId) \r\n" +
			"and  vw.srv_product_family_id =:productId and vw.srv_service_status != 'Terminated' \r\n" +
			"and (va.attribute_name =:attrName and (va.attribute_value like %:searchText% or va.disp_val like %:searchText%)) \r\n"
			+	"group by va.attribute_value \r\n" +
			"order by va.disp_val desc ", nativeQuery = true)
	List<Map<String, Object>> findDistinctTemplateAttrValueOrderByAliasDesc(@Param("searchText") String searchText, @Param("customerId") Integer customerId, @Param("customerLeIds")  List<Integer> customerLeIds, @Param("partnerId") Integer partnerId, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId, @Param("attrName") String attrName);

	 List<ViewSiServiceInfoAll> findByIdIn(List<Integer> serviceDetailsIds);
	 
	@Query(value = "select va.attribute_value as attributeValue, group_concat(va.srv_sys_id ) as serviceId, va.disp_val as dispVal \r\n"
			+	"FROM vw_si_service_info_all vw \r\n" + 
	"left join vw_service_additional_info va on va.srv_sys_id = vw.sys_id where \r\n" + 
	"(vw.order_cust_le_id in (:customerLeIds) or vw.erf_cust_partner_le_id in (:partnerLeIds)) \r\n" + 
	"and (:customerId is null or vw.order_customer_id =:customerId) \r\n" + 
	"and (:partnerId is null or vw.order_partner=:partnerId) \r\n" + 
	"and  vw.srv_product_family_id =:productId and vw.srv_service_status != 'Terminated' \r\n" + 
			"and (va.attribute_name =:attrName and (va.attribute_value like %:searchText% or va.disp_val like %:searchText%)) \r\n"
			+	"group by va.attribute_value \r\n" + 
	"order by va.attribute_value asc limit :page , :size", nativeQuery = true)
	List<Map<String, Object>> findDistinctTemplateAttrValueLimit(@Param("searchText") String searchText, @Param("customerId") Integer customerId, @Param("customerLeIds") List<Integer> customerLeIds, @Param("partnerId") Integer partnerId, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId, @Param("page") Integer page, @Param("size") Integer size,  @Param("attrName") String attrName );

	@Query(value = "select va.attribute_value as attributeValue, group_concat(va.srv_sys_id ) as serviceId, va.disp_val as dispVal \r\n"
			+	"FROM vw_si_service_info_all vw \r\n" +
			"left join vw_service_additional_info va on va.srv_sys_id = vw.sys_id where \r\n" +
			"(vw.order_cust_le_id in (:customerLeIds) or vw.erf_cust_partner_le_id in (:partnerLeIds)) \r\n" +
			"and (:customerId is null or vw.order_customer_id =:customerId) \r\n" +
			"and (:partnerId is null or vw.order_partner=:partnerId) \r\n" +
			"and  vw.srv_product_family_id =:productId and vw.srv_service_status != 'Terminated' \r\n" +
			"and (va.attribute_name =:attrName and (va.attribute_value like %:searchText% or va.disp_val like %:searchText%)) \r\n"
			+	"group by va.attribute_value \r\n" +
			"order by va.disp_val asc limit :page , :size", nativeQuery = true)
	List<Map<String, Object>> findDistinctTemplateAttrValueAliasLimit(@Param("searchText") String searchText, @Param("customerId") Integer customerId, @Param("customerLeIds") List<Integer> customerLeIds, @Param("partnerId") Integer partnerId, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId, @Param("page") Integer page, @Param("size") Integer size,  @Param("attrName") String attrName );
	
	@Query(value = "select count(va.attribute_sys_id) FROM vw_si_service_info_all vw\r\n" + 
			"left join vw_service_additional_info va on va.srv_sys_id = vw.sys_id where \r\n" + 
			"(vw.order_cust_le_id in (:customerLeIds) or vw.erf_cust_partner_le_id in (:partnerLeIds)) \r\n" + 
			"and (:customerId is null or vw.order_customer_id =:customerId) \r\n" + 
			"and (:partnerId is null or vw.order_partner=:partnerId) \r\n" + 
			"and  vw.srv_product_family_id =:productId and vw.srv_service_status != 'Terminated' \r\n" + 
			"and (va.attribute_name =:attrName and (va.attribute_value like %:searchText% or va.disp_val like %:searchText%)) ", nativeQuery = true)
		Integer findCountByTemplateAttributeValue(@Param("searchText") String searchText, @Param("customerId") Integer customerId, @Param("customerLeIds")  List<Integer> customerLeIds, @Param("partnerId") Integer partnerId, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId, @Param("attrName") String attrName);

	@Query(value = "select group_concat(va.attribute_sys_id) as attributeId,va.attribute_value as attributeValue, group_concat(va.srv_sys_id ) as serviceId, va.disp_val as dispVal\r\n"
			+			"FROM vw_si_service_info_all vw\r\n" + 
			"left join vw_service_additional_info va on va.srv_sys_id = vw.sys_id where \r\n" + 
			"(vw.order_cust_le_id in (:customerLeIds) or vw.erf_cust_partner_le_id in (:partnerLeIds)) \r\n" + 
			"and (:customerId is null or vw.order_customer_id =:customerId) \r\n" + 
			"and (:partnerId is null or vw.order_partner=:partnerId) \r\n" + 
			"and  vw.srv_product_family_id =:productId and vw.srv_service_status != 'Terminated' \r\n" + 
			"and (va.attribute_name =:attrName and (va.attribute_value like %:searchText% or va.disp_val like %:searchText%)) \r\n"
			+			"group by va.attribute_value \r\n" + 
			"order by va.attribute_value asc ", nativeQuery = true)
			List<Map<String, Object>> findDistinctTemplateAttrValueOrderByAsc(@Param("searchText") String searchText, @Param("customerId") Integer customerId, @Param("customerLeIds")  List<Integer> customerLeIds, @Param("partnerId") Integer partnerId, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId, @Param("attrName") String attrName);

	@Query(value = "select group_concat(va.attribute_sys_id) as attributeId,va.attribute_value as attributeValue, group_concat(va.srv_sys_id ) as serviceId, va.disp_val as dispVal\r\n"
			+			"FROM vw_si_service_info_all vw\r\n" +
			"left join vw_service_additional_info va on va.srv_sys_id = vw.sys_id where \r\n" +
			"(vw.order_cust_le_id in (:customerLeIds) or vw.erf_cust_partner_le_id in (:partnerLeIds)) \r\n" +
			"and (:customerId is null or vw.order_customer_id =:customerId) \r\n" +
			"and (:partnerId is null or vw.order_partner=:partnerId) \r\n" +
			"and  vw.srv_product_family_id =:productId and vw.srv_service_status != 'Terminated' \r\n" +
			"and (va.attribute_name =:attrName and (va.attribute_value like %:searchText% or va.disp_val like %:searchText%)) \r\n"
			+			"group by va.attribute_value \r\n" +
			"order by va.disp_val asc ", nativeQuery = true)
	List<Map<String, Object>> findDistinctTemplateAttrValueOrderByAliasAsc(@Param("searchText") String searchText, @Param("customerId") Integer customerId, @Param("customerLeIds")  List<Integer> customerLeIds, @Param("partnerId") Integer partnerId, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId, @Param("attrName") String attrName);

	@Query(value = "select group_concat(va.attribute_sys_id) as attributeId,va.attribute_value as attributeValue, group_concat(va.srv_sys_id ) as serviceId, va.disp_val as dispVal \r\n"
			+			"FROM vw_si_service_info_all vw\r\n" + 
			"left join vw_service_additional_info va on va.srv_sys_id = vw.sys_id where \r\n" + 
			"(vw.order_cust_le_id in (:customerLeIds) or vw.erf_cust_partner_le_id in (:partnerLeIds)) \r\n" + 
			"and (:customerId is null or vw.order_customer_id =:customerId) \r\n" + 
			"and (:partnerId is null or vw.order_partner=:partnerId) \r\n" + 
			"and  vw.srv_product_family_id =:productId and vw.srv_service_status != 'Terminated' \r\n" + 
			"and (va.attribute_name =:attrName and (va.attribute_value like %:searchText% or va.disp_val like %:searchText%)) \r\n"
			+			"group by va.attribute_value \r\n" + 
			"order by va.attribute_value desc limit :page , :size", nativeQuery = true)
			List<Map<String, Object>> findDistinctTemplateAttrValueLimitDesc(@Param("searchText") String searchText, @Param("customerId") Integer customerId, @Param("customerLeIds") List<Integer> customerLeIds, @Param("partnerId") Integer partnerId, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId, @Param("page") Integer page, @Param("size") Integer size , @Param("attrName") String attrName);

	@Query(value = "select group_concat(va.attribute_sys_id) as attributeId,va.attribute_value as attributeValue, group_concat(va.srv_sys_id ) as serviceId, va.disp_val as dispVal \r\n"
			+			"FROM vw_si_service_info_all vw\r\n" +
			"left join vw_service_additional_info va on va.srv_sys_id = vw.sys_id where \r\n" +
			"(vw.order_cust_le_id in (:customerLeIds) or vw.erf_cust_partner_le_id in (:partnerLeIds)) \r\n" +
			"and (:customerId is null or vw.order_customer_id =:customerId) \r\n" +
			"and (:partnerId is null or vw.order_partner=:partnerId) \r\n" +
			"and  vw.srv_product_family_id =:productId and vw.srv_service_status != 'Terminated' \r\n" +
			"and (va.attribute_name =:attrName and (va.attribute_value like %:searchText% or va.disp_val like %:searchText%)) \r\n"
			+			"group by va.attribute_value \r\n" +
			"order by va.disp_val desc limit :page , :size", nativeQuery = true)
	List<Map<String, Object>> findDistinctTemplateAttrValueLimitAliasDesc(@Param("searchText") String searchText, @Param("customerId") Integer customerId, @Param("customerLeIds") List<Integer> customerLeIds, @Param("partnerId") Integer partnerId, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId, @Param("page") Integer page, @Param("size") Integer size , @Param("attrName") String attrName);
	
	@Query(value="select vw.sys_id as sysId, vw.srv_service_id as serviceId,va.attribute_sys_id as attributeId, va.attribute_name as attributeName, va.attribute_value as attributeValue\r\n" + 
			"FROM vw_si_service_info_all vw\r\n" + 
			"left join vw_service_additional_info va on va.srv_sys_id = vw.sys_id where \r\n" + 
			"(vw.order_cust_le_id in (:customerLeIds) or vw.erf_cust_partner_le_id in (:partnerLeIds)) \r\n" + 
			"and (:customerId is null or vw.order_customer_id =:customerId) and (:partnerId is null or vw.order_partner=:partnerId) \r\n" + 
			"and  vw.srv_product_family_id =:productId and vw.srv_service_status NOT IN ('Terminated','Under Provisioning')  \r\n" + 
			"and va.attribute_name in (:attributeNames) ",nativeQuery = true)
	List<Map<String, Object>> findServiceAttributesForCustomer(@Param("customerLeIds") List<Integer> customerLeIds, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId, @Param("attributeNames") List<String> attributeNames);
	
	@Query(value="select vw.sys_id as sysId, vw.srv_service_id as serviceId,va.attribute_sys_id as attributeId, va.attribute_name as attributeName, va.attribute_value as attributeValue\r\n" + 
			"FROM vw_si_service_info_all vw\r\n" + 
			"left join vw_service_additional_info va on va.srv_sys_id = vw.sys_id where \r\n" + 
			"vw.sys_id in (:sysIds) \r\n" + 
			"and va.attribute_name in (:attributeNames);",nativeQuery = true)
	List<Map<String, Object>> findServiceAttributesByIdsIn( @Param("sysIds") List<Integer> sysIds, @Param("attributeNames") List<String> attributeNames);

	@Query(value = "SELECT sys_id, srv_service_id " + "FROM vw_si_service_info_all "
			+ "LEFT JOIN vw_service_inv_ui_exclusion siuv ON srv_service_id = siuv.service_id AND siuv.is_cust_prtl_exclusion_flg='Y'"
			+ "where siuv.service_id IS NULL and (order_cust_le_id in (:customerLeIds) or erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or order_customer_id = :customerId)"
			+ "and (:partnerId is null or order_partner=:partnerId)"
			+ "and srv_product_family_id = :productId and srv_service_status NOT IN ('Terminated','Under Provisioning');", nativeQuery = true)
	List<Map<String, Object>> findAllSdwanSites(@Param("customerLeIds") List<Integer> customerLeIds,
			@Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId,
			@Param("customerId") Integer customerId, @Param("partnerId") Integer partnerId);
	
	@Query(value ="SELECT  vwinfo.sys_id as sysId,vwinfo.order_sys_id as orderSysId, vwinfo.order_customer_id as orderCustomerId ,vwinfo.order_cust_le_id as orderCustLeId, \r\n" +
			"vwinfo.srv_site_alias as siteAlias, vwinfo.srv_service_id as serviceId, vwinfo.izo_sdwan_srvc_id as izoSdwanSrvcId, \r\n" +
			"vwinfo.srv_product_family_id as productFamilyId, vwinfo.srv_product_offering_name as productOfferingName, \r\n" +
			"vwinfo.srv_product_offering_id as productOfferingId, vwinfo.srv_primary_service_id as primaryServiceId,  \r\n" +
			"vwinfo.srv_pri_sec as primaryOrSecondary, vwinfo.srv_pri_sec_link as primarySecondaryLink, vwinfo.srv_lat_long as latLong, \r\n" +
			"vwinfo.srv_service_classification as serviceClassification,vwinfo.srv_vpn_name as vpnName, vwinfo.srv_access_type as accessType, \r\n" +
			"vwinfo.srv_customer_site_address as customerSiteAddress, vwinfo.srv_bandwidth as bandwidth, vwinfo.srv_bandwidth_unit as bandwidthUnit, \r\n" +
			"vwinfo.srv_lastmile_bandwidth as lastMileBandwidth,vwinfo.srv_lastmile_bandwidth_unit as lastMileBandwidthUnit, \r\n" +
			"vwinfo.srv_source_country as sourceCountry,vwinfo.srv_service_status as serviceStatus, vwinfo.srv_lastmile_provider as lastMileProvider, \r\n" +
			"vwinfo.srv_destination_country as destinationCountry, vwinfo.srv_destination_city as destinationCity,vwinfo.srv_source_city as sourceCity,  \r\n" +
			"vwinfo.order_is_active as orderIsActive,vwinfo.erf_loc_site_address_id as  locationId, vwinfo.order_term_in_months as orderTermInMonths, \r\n" +
			"vwinfo.is_active as isActive, vwinfo.opportunity_id as opportunityId, vwinfo.order_partner as orderPartner , vwinfo.erf_cust_partner_le_id as erfCustPartnerLeId ,\r\n" +
			"asset.asset_sys_id as assetId, asset.srv_sys_id as assetSysId, asset.service_id as assetServiceId,asset.asset_name as assetName,\r\n" +
			"asset.asset_type as assetType, asset.model as model, asset.is_active as assetIsActive,asset.scope_of_management as scopeOfManagement,\r\n" +
			"asset.support_type as supportType,asset.is_shared_ind as isSharedInd,assetAdd.attribute_name as assetAttrName, assetAdd.attribute_value assetAttrValue\r\n" +
			"FROM vw_si_service_info_all vwinfo \r\n" +
			"left join vw_service_asset_info asset on asset.srv_sys_id = vwinfo.sys_id\r\n" +
			"left join vw_service_asset_additional_info assetAdd on assetAdd.asset_sys_id = asset.asset_sys_id\r\n" +
			"left JOIN vw_service_inv_ui_exclusion siuv ON izo_sdwan_srvc_id = siuv.service_id AND siuv.is_cust_prtl_exclusion_flg='Y' \r\n" +
			"where siuv.service_id IS NULL and vwinfo.srv_service_status NOT IN ('Terminated') and \r\n" +
			"vwinfo.izo_sdwan_srvc_id in  (:izoSdwanServiceIds) and asset.asset_tag='SDWAN CPE'", nativeQuery = true)
	List<Map<String,Object>> findByUndeyLayByIzoSdwanServiceIdsIn(@Param("izoSdwanServiceIds") List<String> izoSdwanServiceIds);
	
	/**
	 *
	 * Query to find the underlay service details by SDWAN Service id
	 * 
	 * @param sdwanServiceId
	 * @return
	 */
	@Query(value = "SELECT sys_id as sysId, srv_service_id as serviceId, srv_site_alias as siteAlias, srv_source_country as sourceCountry, srv_source_city as sourceCity, srv_destination_city as destinationCity, srv_destination_country as destinationCountry, izo_sdwan_srvc_id as izoSdwanSrvcId, asset.assetName, asset.asset_sys_id as assetId FROM vw_si_service_info_all left join vw_service_asset_info asset on sys_id = asset.srv_sys_id where srv_service_status != 'Terminated' and izo_sdwan_srvc_id =:serviceIds and asset_tag='SDWAN CPE'", nativeQuery = true)
	List<Map<String, Object>> findServiceDetailsByIzoSdwanServiceId(@Param("serviceIds") String sdwanServiceId);

	/**
	 * To fetch cpe details based on name
	 *
	 * @param customerId
	 * @param partnerId
	 * @param customerLeIds
	 * @param partnerLeIds
	 * @param assetName
	 * @param assetTag
	 * @return
	 */
	@Query(value = "SELECT  vwinfo.sys_id as sysId,vwinfo.order_sys_id as orderSysId, vwinfo.order_customer_id as orderCustomerId ,vwinfo.order_cust_le_id as orderCustLeId,\n"
			+ "vwinfo.srv_site_alias as siteAlias, vwinfo.srv_service_id as serviceId, vwinfo.izo_sdwan_srvc_id as izoSdwanSrvcId,\n"
			+ "vwinfo.srv_product_family_id as productFamilyId, vwinfo.srv_product_offering_name as productOfferingName,\n"
			+ "vwinfo.srv_product_offering_id as productOfferingId, vwinfo.srv_primary_service_id as primaryServiceId,  \n"
			+ "vwinfo.srv_pri_sec as primaryOrSecondary, vwinfo.srv_pri_sec_link as primarySecondaryLink, vwinfo.srv_lat_long as latLong,\n"
			+ "vwinfo.srv_service_classification as serviceClassification,vwinfo.srv_vpn_name as vpnName, vwinfo.srv_access_type as accessType,\n"
			+ "vwinfo.srv_customer_site_address as customerSiteAddress, vwinfo.srv_bandwidth as bandwidth, vwinfo.srv_bandwidth_unit as bandwidthUnit,\n"
			+ "vwinfo.srv_lastmile_bandwidth as lastMileBandwidth,vwinfo.srv_lastmile_bandwidth_unit as lastMileBandwidthUnit,\n"
			+ "vwinfo.srv_source_country as sourceCountry,vwinfo.srv_service_status as serviceStatus, vwinfo.srv_lastmile_provider as lastMileProvider,\n"
			+ "vwinfo.srv_destination_country as destinationCountry, vwinfo.srv_destination_city as destinationCity,vwinfo.srv_source_city as sourceCity,  \n"
			+ "vwinfo.order_is_active as orderIsActive,vwinfo.erf_loc_site_address_id as  locationId, vwinfo.order_term_in_months as orderTermInMonths,\n"
			+ "vwinfo.is_active as isActive, vwinfo.opportunity_id as opportunityId, vwinfo.order_partner as orderPartner , vwinfo.erf_cust_partner_le_id as erfCustPartnerLeId ,\n"
			+ "asset.asset_sys_id as assetId, asset.srv_sys_id as assetSysId, asset.service_id as assetServiceId,asset.asset_name as assetName,\n"
			+ "asset.asset_type as assetType, asset.model as model, asset.serial_no as serialNo, asset.is_active as assetIsActive,asset.scope_of_management as scopeOfManagement,\n"
			+ "asset.support_type as supportType,asset.is_shared_ind as isSharedInd,assetAdd.attribute_name as assetAttrName, assetAdd.attribute_value as assetAttrValue,"
			+ "addInfo.attribute_sys_id as templateId, addInfo.attribute_name as attributeName, addInfo.attribute_value as attributeValue, addInfo.disp_val as dispVal "
			+ "FROM vw_si_service_info_all vwinfo left join vw_service_asset_info asset on asset.srv_sys_id = vwinfo.sys_id "
			+ "left join vw_service_asset_additional_info assetAdd on assetAdd.asset_sys_id = asset.asset_sys_id "
			+ "left join vw_service_additional_info addInfo on addInfo.srv_sys_id = asset.srv_sys_id and addInfo.attribute_name='Sdwan_Template_Name' "
			+ "where (vwinfo.order_cust_le_id in (:customerLeIds) or vwinfo.erf_cust_partner_le_id in (:partnerLeIds)) "
			+ "and (:customerId is null or vwinfo.order_customer_id = :customerId) "
			+ "and (:partnerId is null or vwinfo.order_partner= :partnerId) "
			+ "and vwinfo.srv_service_status NOT IN ('Terminated')\n" + " and  asset.asset_tag= :assetTag and "
			+ "asset.asset_name = :assetName", nativeQuery = true)
	List<Map<String, Object>> findByAssetNameAndAssetTag(@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId,
		 @Param("customerLeIds") List<Integer> customerLeIds,@Param("partnerLeIds") List<Integer> partnerLeIds,@Param("assetName") String assetName,@Param("assetTag") String assetTag);
	
	
	
	@Query(value = "SELECT  sys_id as sysId,order_sys_id as orderSysId, order_customer_id as orderCustomerId ,order_cust_le_id as orderCustLeId,\r\n" + 
			"srv_site_alias as siteAlias, srv_service_id as serviceId, izo_sdwan_srvc_id as izoSdwanSrvcId,\r\n" + 
			"srv_product_family_id as productFamilyId, srv_product_offering_name as productOfferingName, srv_product_family_name as productFamilyName,\r\n" + 
			"srv_product_offering_id as productOfferingId, srv_primary_service_id as primaryServiceId, \r\n" + 
			"srv_pri_sec as primaryOrSecondary, srv_pri_sec_link as primarySecondaryLink, srv_lat_long as latLong, \r\n" + 
			"srv_service_classification as serviceClassification,srv_vpn_name as vpnName, srv_access_type as accessType,\r\n" + 
			"srv_customer_site_address as customerSiteAddress, srv_bandwidth as bandwidth, srv_bandwidth_unit as bandwidthUnit,\r\n" + 
			"srv_lastmile_bandwidth as lastMileBandwidth,srv_lastmile_bandwidth_unit as lastMileBandwidthUnit,\r\n" + 
			"srv_source_country as sourceCountry,srv_service_status as serviceStatus, srv_lastmile_provider as lastMileProvider,\r\n" + 
			"srv_destination_country as destinationCountry, srv_destination_city as destinationCity,srv_source_city as sourceCity, \r\n" + 
			"order_is_active as orderIsActive,erf_loc_site_address_id as  locationId, order_term_in_months as orderTermInMonths,\r\n" + 
			"is_active as isActive, opportunity_id as opportunityId, order_partner as orderPartner ,erf_cust_partner_le_id as erfCustPartnerLeId\r\n" + 
			"FROM vw_si_service_info_all vwinfo where srv_service_status NOT IN ('Terminated','Under Provisioning') and srv_service_id= :siteName", nativeQuery = true)
	Map<String,Object> findSdwanSite(@Param("siteName") String siteName);

	@Query(value = "SELECT vwinfo.sys_id as sysId, vwinfo.srv_service_id as serviceId, vwinfo.izo_sdwan_srvc_id as sdwanServiceId, asset.asset_name as asset_name FROM vw_si_service_info_all vwinfo "
			+ "left join vw_service_asset_info asset on asset.srv_sys_id = vwinfo.sys_id"
			+ " where (order_cust_le_id in (:customerIds) or erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or order_customer_id = :customerId)"
			+ " and (:partnerId is null or order_partner=:partnerId) and srv_service_status NOT IN ('Terminated') and izo_sdwan_srvc_id is not null"
			+ " and asset.asset_tag='SDWAN CPE'", nativeQuery = true)
	List<Map<String, Object>> findUnderlaysByCustomer(Integer customerId, Integer partnerId, List<Integer> customerIds,
			List<Integer> partnerLeIds);


	@Query(value = "SELECT undr.sys_id as underlaySysId, undr.srv_service_id as underlaySrvcId, undr.izo_sdwan_srvc_id as overlaySrvcId, ovr.sys_id as overlaySysId, ad.attribute_sys_id as templateId, ad.attribute_name as attributeName, ad.attribute_value as attributeValue, ad.disp_val as dispVal FROM vw_si_service_info_all undr"
			+ " left join vw_si_service_info_all ovr on undr.izo_sdwan_srvc_id = ovr.srv_service_id"
			+ " left join vw_service_additional_info ad"
			+ " on ad.srv_sys_id = undr.sys_id where ad.attribute_name='Sdwan_Template_name' and ad.attribute_value in (:templateNames) and (undr.order_cust_le_id in (:customerIds) or undr.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or undr.order_customer_id = :customerId) and (:partnerId is null or undr.order_partner=:partnerId) and undr.srv_service_status NOT IN ('Terminated')", nativeQuery = true)
	List<Map<String, Object>> findByTemplateNames(List<String> templateNames, Integer customerId,
			List<Integer> customerIds, Integer partnerId, List<Integer> partnerLeIds);
	@Query(value = "SELECT undr.sys_id as underlaySysId, undr.srv_service_id as underlaySrvcId, undr.izo_sdwan_srvc_id as overlaySrvcId, ovr.sys_id as overlaySysId, ad.attribute_sys_id as templateId, ad.attribute_name as attributeName, ad.attribute_value as attributeValue, ad.disp_val as dispVal FROM vw_si_service_info_all undr"
			+ " left join vw_si_service_info_all ovr on undr.izo_sdwan_srvc_id = ovr.srv_service_id"
			+ " left join vw_service_additional_info ad"
			+ " on ad.srv_sys_id = undr.sys_id where ad.attribute_name='SITE_LIST' and ad.attribute_value in (:templateNames) and (undr.order_cust_le_id in (:customerIds) or undr.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or undr.order_customer_id = :customerId) and (:partnerId is null or undr.order_partner=:partnerId) and undr.srv_service_status NOT IN ('Terminated')", nativeQuery = true)
	List<Map<String, Object>> findBysiteListIdNames(List<String> templateNames, Integer customerId,
			List<Integer> customerIds, Integer partnerId, List<Integer> partnerLeIds);
	@Query(value="select vw.sys_id as sysId, vw.srv_service_id as serviceId,va.attribute_sys_id as attributeId, \r\n" + 
			"va.attribute_name as attributeName,va.attribute_value as attributeValue,\r\n" + 
			"vwunderlay.sys_id as underlaySysId, vwunderlay.izo_sdwan_srvc_id as izoSdwanSrvcId,\r\n" + 
			"vwunderlay.srv_service_id as underlayServiceId,vwunderlayAttr.attribute_sys_id as underlayAttributeId,\r\n" + 
			"vwunderlayAttr.attribute_name as underlayAttributeName,vwunderlayAttr.attribute_value as underlayAttributeValue\r\n" + 
			"FROM vw_si_service_info_all vw \r\n" + 
			"left join vw_service_additional_info va on va.srv_sys_id = vw.sys_id \r\n" + 
			"inner join vw_si_service_info_all vwunderlay on vwunderlay.izo_sdwan_srvc_id = vw.srv_service_id\r\n" + 
			"left join   vw_service_additional_info vwunderlayAttr on vwunderlayAttr.srv_sys_id = vwunderlay.sys_id\r\n" + 
			"where (vw.order_cust_le_id in (:customerLeIds) or vw.erf_cust_partner_le_id in (:partnerLeIds))  \r\n" + 
			"and (:customerId is null or vw.order_customer_id =:customerId) and (:partnerId is null or vw.order_partner=:partnerId)  \r\n" + 
			"and  vw.srv_product_family_id =:productId and vw.srv_service_status NOT IN ('Terminated','Under Provisioning')   \r\n" + 
			"and va.attribute_name in (:overlayAttributeNames)\r\n" + 
			"and vwunderlayAttr.attribute_name in (:underlayAttributeNames)  ",nativeQuery = true)
	List<Map<String, Object>> findSdwanServiceAttributes(@Param("customerLeIds") List<Integer> customerLeIds, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId, @Param("overlayAttributeNames") List<String> overlayAttributeNames, @Param("underlayAttributeNames") List<String> underlayAttributeNames);
	

	/**
	 * To fetch cpe details based on name and attribute name as sitelist
	 *
	 * @param customerId
	 * @param partnerId
	 * @param customerLeIds
	 * @param partnerLeIds
	 * @param assetName
	 * @param assetTag
	 * @return
	 */
	@Query(value = "SELECT  vwinfo.sys_id as sysId,vwinfo.order_sys_id as orderSysId, vwinfo.order_customer_id as orderCustomerId ,vwinfo.order_cust_le_id as orderCustLeId,\n"
			+ "vwinfo.srv_site_alias as siteAlias, vwinfo.srv_service_id as serviceId, vwinfo.izo_sdwan_srvc_id as izoSdwanSrvcId,\n"
			+ "vwinfo.srv_product_family_id as productFamilyId, vwinfo.srv_product_offering_name as productOfferingName,\n"
			+ "vwinfo.srv_product_offering_id as productOfferingId, vwinfo.srv_primary_service_id as primaryServiceId,  \n"
			+ "vwinfo.srv_pri_sec as primaryOrSecondary, vwinfo.srv_pri_sec_link as primarySecondaryLink, vwinfo.srv_lat_long as latLong,\n"
			+ "vwinfo.srv_service_classification as serviceClassification,vwinfo.srv_vpn_name as vpnName, vwinfo.srv_access_type as accessType,\n"
			+ "vwinfo.srv_customer_site_address as customerSiteAddress, vwinfo.srv_bandwidth as bandwidth, vwinfo.srv_bandwidth_unit as bandwidthUnit,\n"
			+ "vwinfo.srv_lastmile_bandwidth as lastMileBandwidth,vwinfo.srv_lastmile_bandwidth_unit as lastMileBandwidthUnit,\n"
			+ "vwinfo.srv_source_country as sourceCountry,vwinfo.srv_service_status as serviceStatus, vwinfo.srv_lastmile_provider as lastMileProvider,\n"
			+ "vwinfo.srv_destination_country as destinationCountry, vwinfo.srv_destination_city as destinationCity,vwinfo.srv_source_city as sourceCity,  \n"
			+ "vwinfo.order_is_active as orderIsActive,vwinfo.erf_loc_site_address_id as  locationId, vwinfo.order_term_in_months as orderTermInMonths,\n"
			+ "vwinfo.is_active as isActive, vwinfo.opportunity_id as opportunityId, vwinfo.order_partner as orderPartner , vwinfo.erf_cust_partner_le_id as erfCustPartnerLeId ,\n"
			+ "asset.asset_sys_id as assetId, asset.srv_sys_id as assetSysId, asset.service_id as assetServiceId,asset.asset_name as assetName,\n"
			+ "asset.asset_type as assetType, asset.model as model, asset.serial_no as serialNo, asset.is_active as assetIsActive,asset.scope_of_management as scopeOfManagement,\n"
			+ "asset.support_type as supportType,asset.is_shared_ind as isSharedInd,assetAdd.attribute_name as assetAttrName, assetAdd.attribute_value as assetAttrValue,"
			+ "addInfo.attribute_sys_id as templateId, addInfo.attribute_name as attributeName, addInfo.attribute_value as attributeValue, addInfo.disp_val as dispVal "
			+ "FROM vw_si_service_info_all vwinfo left join vw_service_asset_info asset on asset.srv_sys_id = vwinfo.sys_id "
			+ "left join vw_service_asset_additional_info assetAdd on assetAdd.asset_sys_id = asset.asset_sys_id "
			+ "left join vw_service_additional_info addInfo on addInfo.srv_sys_id = asset.srv_sys_id and addInfo.attribute_name='SITE_LIST' "
			+ "where (vwinfo.order_cust_le_id in (:customerLeIds) or vwinfo.erf_cust_partner_le_id in (:partnerLeIds)) "
			+ "and (:customerId is null or vwinfo.order_customer_id = :customerId) "
			+ "and (:partnerId is null or vwinfo.order_partner= :partnerId) "
			+ "and vwinfo.srv_service_status NOT IN ('Terminated')\n" + " and  asset.asset_tag= :assetTag and "
			+ "asset.asset_name = :assetName", nativeQuery = true)
	List<Map<String, Object>> findByCiscoAssetNameAndAssetTag(@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId,
		 @Param("customerLeIds") List<Integer> customerLeIds,@Param("partnerLeIds") List<Integer> partnerLeIds,@Param("assetName") String assetName,@Param("assetTag") String assetTag);
	
	List<ViewSiServiceInfoAll> findByServiceIdInAndServiceStatusAndIsActive(List<String> serviceIds,String status, String isActive);
	
	List<ViewSiServiceInfoAll> findByServiceIdInOrderByIdDesc(List<String> serviceIds);
}

