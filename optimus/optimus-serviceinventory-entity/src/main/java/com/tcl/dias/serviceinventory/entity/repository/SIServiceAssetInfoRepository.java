package com.tcl.dias.serviceinventory.entity.repository;

import java.util.List;
import java.util.Map;

import com.tcl.dias.serviceinventory.entity.entities.SIAsset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.SIServiceAssetInfo;

/**
 * Repository for service_asset_info view
 * 
 * @author Srinivasa Raghavan
 */
@Repository
public interface SIServiceAssetInfoRepository extends JpaRepository<SIServiceAssetInfo, Integer> {
	/**
	 * To fetch all SDWAN CPE's
	 *
	 * @param customerId
	 * @param customerIds
	 * @param partnerId
	 * @param partnerLeIds
	 * @param productId
	 * @return
	 */
	@Query(value = "SELECT asset_info.asset_sys_id as asset_id,asset_info.srv_sys_id as srv_sys_id, group_concat(asset_info.service_id) as service_id, asset_info.asset_name as cpe_name, count(asset_info.asset_name) as cpeCount, asset_info.model as model, asset_info.serial_no, asset_info.description, site_info.sys_id as sys_id, site_info.srv_site_alias as alias, site_info.srv_source_country as country, site_info.srv_source_city as city, site_info.izo_sdwan_srvc_id as site_name, site_info.srv_customer_site_address as site_address,"
			+ " attr.attribute_sys_id as attributeId, attr.attribute_name as attributeName, attr.attribute_value as attributeValue, site_info.srv_service_status as status "
			+ "FROM vw_service_asset_info asset_info "
			+ "left join `vw_service_additional_info` attr on asset_info.srv_sys_id = attr.srv_sys_id and attribute_name='Sdwan_Template_Name'"
			+ "right join "
			+ "(SELECT sys_id, srv_site_alias, srv_source_country, srv_source_city, izo_sdwan_srvc_id, srv_customer_site_address, srv_service_status from vw_si_service_info_all where izo_sdwan_srvc_id in "
			+ "(SELECT srv_service_id FROM vw_si_service_info_all "
			+ "where srv_service_status NOT IN ('Terminated','Under Provisioning') and ((order_cust_le_id in (:customerLeIds) or erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or order_customer_id = :customerId) "
			+ "and (:partnerId is null or order_partner=:partnerId) "
			+ "and srv_product_family_id= :productFamilyId ))) as site_info on asset_info.srv_sys_id = site_info.sys_id where srv_service_status NOT IN ('Terminated') and asset_tag='SDWAN CPE' group by asset_info.asset_name", nativeQuery = true)
	List<Map<String, Object>> findSdwanCpe(@Param("customerId") Integer customerId,
			@Param("customerLeIds") List<Integer> customerIds, @Param("partnerId") Integer partnerId,
			@Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productFamilyId") Integer productId);

	/**
	 * To fetch count of active SDWAN CPE's
	 *
	 * @param customerId
	 * @param customerIds
	 * @param partnerId
	 * @param partnerLeIds
	 * @param productId
	 * @return
	 */
	@Query(value = "SELECT count(distinct(asset_info.asset_name)) FROM vw_service_asset_info asset_info right join (select sys_id, srv_service_status from vw_si_service_info_all where izo_sdwan_srvc_id in ("
			+ "SELECT srv_service_id FROM vw_si_service_info_all "
			+ "where srv_service_status NOT IN ('Terminated','Under Provisioning') and ((order_cust_le_id in (:customerLeIds) or erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or order_customer_id = :customerId) "
			+ "and (:partnerId is null or order_partner=:partnerId) "
			+ "and srv_product_family_id= :productFamilyId ))) as site_info on asset_info.srv_sys_id = site_info.sys_id where srv_service_status NOT IN ('Terminated') and asset_tag='SDWAN CPE'", nativeQuery = true)
	Integer findSdwanCpeCount(@Param("customerId") Integer customerId,
			@Param("customerLeIds") List<Integer> customerIds, @Param("partnerId") Integer partnerId,
			@Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productFamilyId") Integer productId);

	/**
	 * To fetch SDWAN CPE's with pagination
	 *
	 * @param customerId
	 * @param customerIds
	 * @param partnerId
	 * @param partnerLeIds
	 * @param page
	 * @param size
	 * @param productId
	 * @return
	 */
	@Query(value = "SELECT asset_info.asset_sys_id as asset_id,asset_info.srv_sys_id as srv_sys_id, group_concat(asset_info.service_id) as service_id, asset_info.asset_name as cpe_name, count(asset_info.asset_name) as cpeCount, asset_info.model as model, asset_info.serial_no, asset_info.description, site_info.sys_id as sys_id, site_info.srv_site_alias as alias, site_info.srv_source_country as country, site_info.srv_source_city as city, site_info.izo_sdwan_srvc_id as site_name, site_info.srv_customer_site_address as site_address,"
			+ " attr.attribute_sys_id as attributeId, attr.attribute_name as attributeName, attr.attribute_value as attributeValue, site_info.srv_service_status as status "
			+ "FROM vw_service_asset_info asset_info "
			+ "left join `vw_service_additional_info` attr on asset_info.srv_sys_id = attr.srv_sys_id and attribute_name='Sdwan_Template_Name'"
			+ "right join "
			+ "(SELECT sys_id, srv_site_alias, srv_source_country, srv_source_city, izo_sdwan_srvc_id, srv_customer_site_address, srv_service_status from vw_si_service_info_all where izo_sdwan_srvc_id in "
			+ "(SELECT srv_service_id FROM vw_si_service_info_all "
			+ "where srv_service_status NOT IN ('Terminated','Under Provisioning') and ((order_cust_le_id in (:customerLeIds) or erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or order_customer_id = :customerId) "
			+ "and (:partnerId is null or order_partner=:partnerId) "
			+ "and srv_product_family_id= :productFamilyId ))) as site_info on asset_info.srv_sys_id = site_info.sys_id where srv_service_status NOT IN ('Terminated') and asset_tag='SDWAN CPE' group by asset_info.asset_name limit :page , :size", nativeQuery = true)
	List<Map<String, Object>> findSdwanCpe(@Param("customerId") Integer customerId,
			@Param("customerLeIds") List<Integer> customerIds, @Param("partnerId") Integer partnerId,
			@Param("partnerLeIds") List<Integer> partnerLeIds, @Param("page") Integer page, @Param("size") Integer size,
			@Param("productFamilyId") Integer productId);

	List<SIServiceAssetInfo> findByServiceIdInAndAssetTag(List<String> serviceIds, String assetTag);

	SIServiceAssetInfo findByServiceId(String serviceId);
	
	/**
	 * To fetch all SDWAN CPE's
	 *
	 * @param customerId
	 * @param customerIds
	 * @param partnerId
	 * @param partnerLeIds
	 * @param productId
	 * @return
	 */
	@Query(value = "SELECT asset_info.asset_sys_id as asset_id,asset_info.srv_sys_id as srv_sys_id, group_concat(asset_info.service_id) as service_id, asset_info.asset_name as cpe_name, count(asset_info.asset_name) as cpeCount, asset_info.model as model, asset_info.serial_no, asset_info.description, site_info.sys_id as sys_id, site_info.srv_site_alias as alias, site_info.srv_source_country as country, site_info.srv_source_city as city, site_info.izo_sdwan_srvc_id as site_name, site_info.srv_customer_site_address as site_address,"
			+ " attr.attribute_sys_id as attributeId, attr.attribute_name as attributeName, attr.attribute_value as attributeValue, site_info.srv_service_status as status "
			+ "FROM vw_service_asset_info asset_info "
			+ "left join `vw_service_additional_info` attr on asset_info.srv_sys_id = attr.srv_sys_id and attribute_name='SITE_LIST'"
			+ "right join "
			+ "(SELECT sys_id, srv_site_alias, srv_source_country, srv_source_city, izo_sdwan_srvc_id, srv_customer_site_address, srv_service_status from vw_si_service_info_all where izo_sdwan_srvc_id in "
			+ "(SELECT srv_service_id FROM vw_si_service_info_all "
			+ "where srv_service_status NOT IN ('Terminated','Under Provisioning') and ((order_cust_le_id in (:customerLeIds) or erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or order_customer_id = :customerId) "
			+ "and (:partnerId is null or order_partner=:partnerId) "
			+ "and srv_product_family_id= :productFamilyId ))) as site_info on asset_info.srv_sys_id = site_info.sys_id where srv_service_status NOT IN ('Terminated') and asset_tag='SDWAN CPE' group by asset_info.asset_name", nativeQuery = true)
	List<Map<String, Object>> findSdwanCpeBySiteListAttribute(@Param("customerId") Integer customerId,
			@Param("customerLeIds") List<Integer> customerIds, @Param("partnerId") Integer partnerId,
			@Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productFamilyId") Integer productId);
	
	/**
	 * To fetch SDWAN CPE's with pagination
	 *
	 * @param customerId
	 * @param customerIds
	 * @param partnerId
	 * @param partnerLeIds
	 * @param page
	 * @param size
	 * @param productId
	 * @return
	 */
	@Query(value = "SELECT asset_info.asset_sys_id as asset_id,asset_info.srv_sys_id as srv_sys_id, group_concat(asset_info.service_id) as service_id, asset_info.asset_name as cpe_name, count(asset_info.asset_name) as cpeCount, asset_info.model as model, asset_info.serial_no, asset_info.description, site_info.sys_id as sys_id, site_info.srv_site_alias as alias, site_info.srv_source_country as country, site_info.srv_source_city as city, site_info.izo_sdwan_srvc_id as site_name, site_info.srv_customer_site_address as site_address,"
			+ " attr.attribute_sys_id as attributeId, attr.attribute_name as attributeName, attr.attribute_value as attributeValue, site_info.srv_service_status as status "
			+ "FROM vw_service_asset_info asset_info "
			+ "left join `vw_service_additional_info` attr on asset_info.srv_sys_id = attr.srv_sys_id and attribute_name='SITE_LIST'"
			+ "right join "
			+ "(SELECT sys_id, srv_site_alias, srv_source_country, srv_source_city, izo_sdwan_srvc_id, srv_customer_site_address, srv_service_status from vw_si_service_info_all where izo_sdwan_srvc_id in "
			+ "(SELECT srv_service_id FROM vw_si_service_info_all "
			+ "where srv_service_status NOT IN ('Terminated','Under Provisioning') and ((order_cust_le_id in (:customerLeIds) or erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or order_customer_id = :customerId) "
			+ "and (:partnerId is null or order_partner=:partnerId) "
			+ "and srv_product_family_id= :productFamilyId ))) as site_info on asset_info.srv_sys_id = site_info.sys_id where srv_service_status NOT IN ('Terminated') and asset_tag='SDWAN CPE' group by asset_info.asset_name limit :page , :size", nativeQuery = true)
	List<Map<String, Object>> findSdwanCpeBySiteListAttribute(@Param("customerId") Integer customerId,
			@Param("customerLeIds") List<Integer> customerIds, @Param("partnerId") Integer partnerId,
			@Param("partnerLeIds") List<Integer> partnerLeIds, @Param("page") Integer page, @Param("size") Integer size,
			@Param("productFamilyId") Integer productId);

}