package com.tcl.dias.serviceinventory.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.SIServiceInfo;

/**
 * This file contains the SIServiceInfoRepository.java class.
 * 
 *
 * @author DSIVALIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface SIServiceInfoRepository extends JpaRepository<SIServiceInfo, Integer> {

	/**
	 * this query is used to get service detail info based on service detail Id and productName
	 * @param sysid
	 * @param productName
	 * @return
	 */
    @Query(value="select ssi.sys_id as sys_id, ssi.srv_service_id as serviceid, ssi.srv_product_family_name as product_name,ssi.srv_service_management_option as service_option, " +
    		"ssi.srv_product_offering_name as offering_name,ssi.srv_service_id as service_id,ssi.srv_product_family_id as product_id,ssi.IP_address_provided_by as IP_address_provided_by,ssi.additional_ips_req as additional_ip," +
    		"ssi.srv_customer_site_address as site_address, ssi.srv_access_type as access_type, ssi.srv_pri_sec as pri_sec, " + 
    		"ssi.srv_service_status as service_status, ssi.srv_lastmile_provider as lastmile_provider, " +
    		"ssi.srv_lat_long as lat_long, ssi.srv_site_alias as site_alias " + 
    		"from vw_si_service_info_all ssi where ssi.srv_product_family_name = :productName and ssi.srv_service_id = :sysid and ssi.is_active='Y'", nativeQuery=true)
    public List<Map<String,Object>> getServiceDetailByServiceId(@Param("sysid") String sysid, @Param("productName") String productName);

	/**
	 * This query is used to get asset information based on service detail Id
	 * @param sysid
	 * @return
	 */
	@Query(value="select saa.component as component, saa.attribute_name as attribute_name, saa.attribute_value as attribute_value "
    		+ "from vw_service_asset_additional_info saa where saa.asset_sys_id in\r\n" + 
    		" (select sa.asset_sys_id as asset_sys_id from vw_service_asset_info sa where is_active = 'Y' and sa.srv_sys_id = :sysid)\r\n" +
    		" group by saa.component, saa.attribute_name, saa.attribute_value", nativeQuery=true)
  	List<Map<String,Object>> getAssetWithAttributes(@Param("sysid") Integer sysid);
    
    Optional<SIServiceInfo> findByServiceId(String serviceId);

    /**
     * To get the sys_id's for a given ServiceId and a product.
     * 
     * @param serviceid
     * @param productName
     * @return {@List} of sysId's.
     */
    @Query(value="SELECT si.sys_id FROM vw_si_service_info si where si.srv_product_family_name=:productName and\r\n" + 
    		"si.srv_service_id =:serviceid ", nativeQuery=true)
    public List<Integer> findSysIdsForAServiceId(@Param("serviceid") String serviceid, @Param("productName") String productName);
    
    
	/**
	 * This Query is used to get service detail attributes based on service Id and product name from service info view
	 * @param sysid
	 * @param productName
	 * @return
	 */
	@Query(value="select ssi.sys_id as sys_id, ssi.srv_service_id as serviceid, ssi.srv_product_family_name as product_name,ssi.srv_service_management_option as service_option, " +
			"ssi.srv_product_offering_name as offering_name,ssi.srv_service_id as service_id,ssi.srv_product_family_id as product_id,ssi.IP_address_provided_by as IP_address_provided_by,ssi.additional_ips_req as additional_ip," +
			"ssi.srv_customer_site_address as site_address, ssi.srv_access_type as access_type, ssi.srv_pri_sec as pri_sec, ssi.srv_pri_sec_link as pri_sec_link, " +
			"ssi.srv_service_status as service_status, ssi.srv_lastmile_provider as lastmile_provider,ssi.srv_site_end_interface as interface,ssi.srv_bandwidth as port_bandwidth,ssi.srv_bandwidth_unit as port_bandwidth_unit,ssi.srv_burstable_bw as burstable_bandwidth,ssi.usage_model as usage_model,ssi.ip_address_arrangement_type as ipAddress_arrangement_type,ssi.ipv4_address_pool_size as ipv4_size,ssi.ipv6_address_pool_size as ipv6_size,ssi.routing_protocol as routing_protocol,ssi.srv_lastmile_bandwidth as lastmile_bandwidth,ssi.srv_lastmile_bandwidth_unit as lastmile_bandwidth_unit,ssi.service_varient as service_varient, ssi.service_type as service_type,ssi.resiliency_ind as resiliency_ind,ssi.backup_config_mode as backup_configuration, " +
			"ssi.srv_lat_long as lat_long,ssi.srv_gvpn_site_topology as siteType,ssi.srv_access_topology as accessTopology,ssi.srv_vpn_topology as vpnTopology, ssi.srv_site_alias as site_alias,ssi.erf_loc_site_address_id as erf_locationId,ssai.componet as component,ssai.attribute_name as attribute_name,ssai.attribute_value as attribute_value, ssi.order_demo_flag as demo_flag, ssi.billing_type as demo_type,ssi.mrc as mrc, ssi.nrc as nrc,ssi.arc as arc, ssi.demarcation_floor as demarcation_floor, ssi.denarcation_room as denarcation_room, ssi.demarcation_apartment as demarcation_apartment, ssi.demarcation_rack as demarcation_rack "  +
			"from vw_si_service_info_all ssi left join vw_service_additional_info ssai on ssai.srv_sys_id = ssi.sys_id "+
			" where ssi.srv_product_family_name =:productName and ssi.srv_service_id =:sysid and (ssi.srv_service_status='Active' or ssi.srv_service_status='Under Provisioning')", nativeQuery=true)
	public List<Map<String,Object>> getServiceDetailAttributesByServiceId(@Param("sysid") String sysid, @Param("productName") String productName);

	/**
	 * This Query is used to get service detail attributes based on service Id and product name from service info view.
	 * 
	 * @param serviceId
	 * @param sysid
	 * @return
	 */
	@Query(value="select ssi.sys_id as sys_id, ssi.srv_service_id as serviceid, ssi.srv_product_family_name as product_name,ssi.srv_service_management_option as service_option, " +
			"ssi.srv_product_offering_name as offering_name,ssi.srv_service_id as service_id,ssi.srv_product_family_id as product_id,"
			+ "ssi.IP_address_provided_by as IP_address_provided_by,ssi.additional_ips_req as additional_ip," +
			"ssi.srv_customer_site_address as site_address, ssi.srv_access_type as access_type, ssi.srv_pri_sec as pri_sec, ssi.srv_pri_sec_link as pri_sec_link, " +
			"ssi.srv_service_status as service_status, ssi.srv_lastmile_provider as lastmile_provider,ssi.srv_site_end_interface as interface,ssi.srv_bandwidth as port_bandwidth,"
			+ "ssi.srv_bandwidth_unit as port_bandwidth_unit,ssi.srv_burstable_bw as burstable_bandwidth,ssi.usage_model as usage_model,"
			+ "ssi.ip_address_arrangement_type as ipAddress_arrangement_type,ssi.ipv4_address_pool_size as ipv4_size,ssi.ipv6_address_pool_size as ipv6_size,"
			+ "ssi.routing_protocol as routing_protocol,ssi.srv_lastmile_bandwidth as lastmile_bandwidth,ssi.srv_lastmile_bandwidth_unit as lastmile_bandwidth_unit,"
			+ "ssi.service_varient as service_varient, ssi.service_type as service_type,ssi.resiliency_ind as resiliency_ind,ssi.backup_config_mode as backup_configuration, " +
			"ssi.srv_lat_long as lat_long, ssi.srv_site_alias as site_alias,ssai.componet as component,ssai.attribute_name as attribute_name,ssai.attribute_value as attribute_value, " +
			"ssi.demarcation_floor as demarcation_floor, ssi.demarcation_rack as demarcation_rack, ssi.denarcation_room as demarcation_room, ssi.demarcation_apartment as demarcation_apartment, "+
			"ssi.site_classification as site_classification, ssi.opportunity_type as opportunity_type, ssi.mrc as mrc,ssi.nrc as nrc,ssi.arc as arc from vw_si_service_info ssi left join vw_service_additional_info ssai on ssai.srv_sys_id = ssi.sys_id and ssai.is_active = 'Y' "+
			"where ssi.srv_product_family_name = 'NPL' and ssi.srv_service_id =:serviceId and ssi.sys_id =:sysid", nativeQuery=true)
	public List<Map<String,Object>> getServiceDetailAttributesForNPL(@Param("serviceId") String serviceId, @Param("sysid") Integer sysid);

	/**
	 * This Query is used to get asset information based on service_detail Id
	 * @param sysid
	 * @return
	 */
	@Query(value="select sa.asset_sys_id as asset_sys_id,sa.model as cpe_model,sa.scope_of_management as scope_management,sa.owner as ip_address_provided,sa.support_type as cpe_support_type,sa.serial_no as cpe_serial_no, sa.oem_vendor as oem_vendor, saa.component as component, saa.attribute_name as attribute_name, saa.attribute_value as attribute_value from  vw_service_asset_info sa left join vw_service_asset_additional_info saa on saa.asset_sys_id=sa.asset_sys_id where sa.srv_sys_id =:sysid ", nativeQuery=true)
	List<Map<String,Object>> getAssetDetailWithAttributes(@Param("sysid") Integer sysid);
	
	 List<SIServiceInfo> findByServiceIdIn(List<String> serviceIds);
		
	@Query(value="select sa.asset_sys_id as asset_sys_id,sa.model as cpe_model,sa.scope_of_management as scope_management,sa.owner as ip_address_provided,sa.support_type as cpe_support_type,sa.serial_no as cpe_serial_no, sa.oem_vendor as oem_vendor, saa.component as component, saa.attribute_name as attribute_name, saa.attribute_value as attribute_value from  vw_service_asset_info sa left join vw_service_asset_additional_info saa on saa.asset_sys_id=sa.asset_sys_id where sa.srv_sys_id =:sysid and sa.asset_type=:type ", nativeQuery=true)
	List<Map<String,Object>> getAssetTypeDetailWithAttributes(@Param("sysid") Integer sysid,@Param("type") String type);
	
	@Query(value="select vsi.* from vw_si_service_info vsi where vsi.srv_service_id = :serviceId", nativeQuery=true)
	List<SIServiceInfo> findByServiceIdNPL(@Param("serviceId") String serviceId);

	@Query(value="select sa.srv_sys_id as srv_sys_id, sa.attribute_name as attribute_name, sa.attribute_value as attribute_value from vw_service_additional_info sa where sa.srv_sys_id =:sysid and sa.is_active='Y'", nativeQuery=true)
	List<Map<String,Object>> getServiceAttributesBasedOnServiceDetailId(@Param("sysid") Integer sysid);

	@Query(value="select asset_sys_id as assestSysId, srv_sys_id as sysId, service_id as serviceId,asset_name as assetName,asset_type as assetType, model as model, is_active as isActive,scope_of_management as scopeOfManagement,support_type as supportType,is_shared_ind as isSharedInd from vw_service_asset_info where is_active = 'Y' and srv_sys_id in (:sysIds)", nativeQuery=true)
	List<Map<String,Object>> getAssetsBySysIdInAndAssetName(@Param("sysIds") List<Integer> sysIds);
//	List<Map<String,Object>> getAssetsBySysIdInAndAssetName(@Param("sysIds") List<Integer> sysIds, @Param("assetTag") String assetTag);
	
	/**
	 * This Query is used to get service detail attributes based on service Id and product name from service info view.
	 * 
	 * @param serviceId
	 * @param sysid
	 * @return
	 */
	@Query(value="select ssi.sys_id as sys_id, ssi.srv_service_id as serviceid, ssi.srv_product_family_name as product_name,ssi.srv_service_management_option as service_option, " +
			"ssi.srv_product_offering_name as offering_name,ssi.srv_service_id as service_id,ssi.srv_product_family_id as product_id,"
			+ "ssi.IP_address_provided_by as IP_address_provided_by,ssi.additional_ips_req as additional_ip," +
			"ssi.srv_customer_site_address as site_address, ssi.srv_access_type as access_type, ssi.srv_pri_sec as pri_sec, ssi.srv_pri_sec_link as pri_sec_link, " +
			"ssi.srv_service_status as service_status, ssi.srv_lastmile_provider as lastmile_provider,ssi.srv_site_end_interface as interface,ssi.srv_bandwidth as port_bandwidth,"
			+ "ssi.srv_bandwidth_unit as port_bandwidth_unit,ssi.srv_burstable_bw as burstable_bandwidth,ssi.usage_model as usage_model,"
			+ "ssi.ip_address_arrangement_type as ipAddress_arrangement_type,ssi.ipv4_address_pool_size as ipv4_size,ssi.ipv6_address_pool_size as ipv6_size,"
			+ "ssi.routing_protocol as routing_protocol,ssi.srv_lastmile_bandwidth as lastmile_bandwidth,ssi.srv_lastmile_bandwidth_unit as lastmile_bandwidth_unit,"
			+ "ssi.service_varient as service_varient, ssi.service_type as service_type,ssi.resiliency_ind as resiliency_ind,ssi.backup_config_mode as backup_configuration, " +
			"ssi.srv_lat_long as lat_long, ssi.srv_site_alias as site_alias,ssai.componet as component,ssai.attribute_name as attribute_name,ssai.attribute_value as attribute_value, " +
			"ssi.site_classification as site_classification, ssi.srv_site_type as site_type from vw_si_service_info ssi left join vw_service_additional_info ssai on ssai.srv_sys_id = ssi.sys_id and ssai.is_active = 'Y' "+
			"where ssi.srv_product_family_name =:productName and ssi.srv_service_id =:serviceId and ssi.sys_id =:sysid", nativeQuery=true)
	public List<Map<String,Object>> getServiceDetailAttributesForGDE(@Param("serviceId") String serviceId, @Param("sysid") Integer sysid, @Param("productName") String productName);
	
	
	/**
	 * This Query is used to get service detail attributes based on service Id and product name from service info view.
	 * 
	 * @param serviceId
	 * @param sysid
	 * @return
	 */
	@Query(value="select ssi.sys_id as sys_id, ssi.srv_service_id as serviceid, ssi.srv_product_family_name as product_name,ssi.srv_service_management_option as service_option, " +
			"ssi.srv_product_offering_name as offering_name,ssi.srv_service_id as service_id,ssi.srv_product_family_id as product_id,"
			+ "ssi.IP_address_provided_by as IP_address_provided_by,ssi.additional_ips_req as additional_ip," +
			"ssi.srv_customer_site_address as site_address, ssi.srv_access_type as access_type, ssi.srv_pri_sec as pri_sec, ssi.srv_pri_sec_link as pri_sec_link, " +
			"ssi.srv_service_status as service_status, ssi.srv_lastmile_provider as lastmile_provider,ssi.srv_site_end_interface as interface,ssi.srv_bandwidth as port_bandwidth,"
			+ "ssi.srv_bandwidth_unit as port_bandwidth_unit,ssi.srv_burstable_bw as burstable_bandwidth,ssi.usage_model as usage_model,"
			+ "ssi.ip_address_arrangement_type as ipAddress_arrangement_type,ssi.ipv4_address_pool_size as ipv4_size,ssi.ipv6_address_pool_size as ipv6_size,"
			+ "ssi.routing_protocol as routing_protocol,ssi.srv_lastmile_bandwidth as lastmile_bandwidth,ssi.srv_lastmile_bandwidth_unit as lastmile_bandwidth_unit,"
			+ "ssi.service_varient as service_varient, ssi.service_type as service_type,ssi.resiliency_ind as resiliency_ind,ssi.backup_config_mode as backup_configuration, " +
			"ssi.srv_lat_long as lat_long, ssi.srv_site_alias as site_alias,ssai.componet as component,ssai.attribute_name as attribute_name,ssai.attribute_value as attribute_value, " +
			"ssi.site_classification as site_classification, ssi.mrc as mrc, ssi.nrc as nrc, ssi.arc as arc from vw_si_service_info ssi left join vw_service_additional_info ssai on ssai.srv_sys_id = ssi.sys_id and ssai.is_active = 'Y' "+
			"where ssi.srv_product_family_name = 'NDE' and ssi.srv_service_id =:serviceId and ssi.sys_id =:sysid", nativeQuery=true)
	public List<Map<String,Object>> getServiceDetailAttributesForNDE(@Param("serviceId") String serviceId, @Param("sysid") Integer sysid);
	
	@Query(value="select sc.id as id,sc.uuid as service_id, sc.si_service_detail_id as service_detail_id, sc.si_component_id as component_id, sc.attribute_name as attribute_name, sc.attribute_value as attribute_value from vw_si_component_attributes sc where sc.si_service_detail_id =:sysid and sc.is_active='Y'", nativeQuery=true)
	List<Map<String,Object>> getServiceComponentAttributesBasedOnServiceDetailId(@Param("sysid") Integer sysid);
	
	@Query(value="select vsi.* from vw_si_service_info_all vsi where vsi.sys_id in (select max(vssia.sys_id) from vw_si_service_info_all vssia where vssia.srv_service_id in (:serviceId) group by vssia.srv_service_id)", nativeQuery = true)
	 List<SIServiceInfo> findServiceIdIn(@Param("serviceId") List<String> serviceIds);

	/**
	 * This Query is used to get service detail attributes based on service Id and product name from service info view
	 * @param sysid
	 * @param productName
	 * @return
	 */
	@Query(value="select ssi.sys_id as sys_id, ssi.srv_service_id as serviceid, ssi.srv_product_family_name as product_name,ssi.srv_service_management_option as service_option, " +
			"ssi.srv_product_offering_name as offering_name,ssi.srv_service_id as service_id,ssi.srv_product_family_id as product_id,ssi.IP_address_provided_by as IP_address_provided_by,ssi.additional_ips_req as additional_ip," +
			"ssi.srv_customer_site_address as site_address, ssi.srv_access_type as access_type, ssi.srv_pri_sec as pri_sec, ssi.srv_pri_sec_link as pri_sec_link, " +
			"ssi.srv_service_status as service_status, ssi.srv_lastmile_provider as lastmile_provider,ssi.srv_site_end_interface as interface,ssi.srv_bandwidth as port_bandwidth,ssi.srv_bandwidth_unit as port_bandwidth_unit,ssi.srv_burstable_bw as burstable_bandwidth,ssi.usage_model as usage_model,ssi.ip_address_arrangement_type as ipAddress_arrangement_type,ssi.ipv4_address_pool_size as ipv4_size,ssi.ipv6_address_pool_size as ipv6_size,ssi.routing_protocol as routing_protocol,ssi.srv_lastmile_bandwidth as lastmile_bandwidth,ssi.srv_lastmile_bandwidth_unit as lastmile_bandwidth_unit,ssi.service_varient as service_varient, ssi.service_type as service_type,ssi.resiliency_ind as resiliency_ind,ssi.backup_config_mode as backup_configuration, " +
			"ssi.srv_lat_long as lat_long,ssi.srv_gvpn_site_topology as siteType,ssi.srv_access_topology as accessTopology,ssi.srv_vpn_topology as vpnTopology, ssi.srv_site_alias as site_alias,ssi.erf_loc_site_address_id as erf_locationId,ssai.componet as component,ssai.attribute_name as attribute_name,ssai.attribute_value as attribute_value, ssi.order_demo_flag as demo_flag, ssi.billing_type as demo_type,ssi.mrc as mrc, ssi.nrc as nrc,ssi.arc as arc " +
			"from vw_si_service_info_all ssi left join vw_service_additional_info ssai on ssai.srv_sys_id = ssi.sys_id "+
			" where ssi.srv_product_family_name =:productName and ssi.srv_service_id =:sysid and (ssi.srv_service_status='Active' or ssi.srv_service_status='Under Provisioning') order by sys_id desc", nativeQuery=true)
	public List<Map<String,Object>> getServiceDetailAttributesByServiceIdLatest(@Param("sysid") String sysid, @Param("productName") String productName);
	

	@Query(value="select * from vw_si_service_info_all where sys_id in(select max(sys_id) from vw_si_service_info_all vsi where vsi.srv_service_id = :serviceId and vsi.srv_site_type is not null group by vsi.srv_site_type, vsi.srv_service_id)", nativeQuery=true)
	List<SIServiceInfo> findServiceIdNPL(@Param("serviceId") String serviceId);
	
	  /**
     * To get the sys_id's for a given ServiceId and a product.
     * 
     * @param serviceid
     * @param productName
     * @return {@List} of sysId's.
     */
    @Query(value="SELECT max(si.sys_id) FROM vw_si_service_info_all si where si.srv_product_family_name=:productName and\r\n" + 
    		"si.srv_service_id =:serviceid and si.srv_site_type is not null group by si.srv_site_type, si.srv_service_id", nativeQuery=true)
    public List<Integer> findLatestSysIdsForAServiceId(@Param("serviceid") String serviceid, @Param("productName") String productName);
    
    
    /**
	 * This Query is used to get service detail attributes based on service Id and product name from service info view.
	 * 
	 * @param serviceId
	 * @param sysid
	 * @return
	 */
	@Query(value="select ssi.sys_id as sys_id, ssi.srv_service_id as serviceid, ssi.srv_product_family_name as product_name,ssi.srv_service_management_option as service_option, " +
			"ssi.srv_product_offering_name as offering_name,ssi.srv_service_id as service_id,ssi.srv_product_family_id as product_id,"
			+ "ssi.IP_address_provided_by as IP_address_provided_by,ssi.additional_ips_req as additional_ip," +
			"ssi.srv_customer_site_address as site_address, ssi.srv_access_type as access_type, ssi.srv_pri_sec as pri_sec, ssi.srv_pri_sec_link as pri_sec_link, " +
			"ssi.srv_service_status as service_status, ssi.srv_lastmile_provider as lastmile_provider,ssi.srv_site_end_interface as interface,ssi.srv_bandwidth as port_bandwidth,"
			+ "ssi.srv_bandwidth_unit as port_bandwidth_unit,ssi.srv_burstable_bw as burstable_bandwidth,ssi.usage_model as usage_model,"
			+ "ssi.ip_address_arrangement_type as ipAddress_arrangement_type,ssi.ipv4_address_pool_size as ipv4_size,ssi.ipv6_address_pool_size as ipv6_size,"
			+ "ssi.routing_protocol as routing_protocol,ssi.srv_lastmile_bandwidth as lastmile_bandwidth,ssi.srv_lastmile_bandwidth_unit as lastmile_bandwidth_unit,"
			+ "ssi.service_varient as service_varient, ssi.service_type as service_type,ssi.resiliency_ind as resiliency_ind,ssi.backup_config_mode as backup_configuration, " +
			"ssi.srv_lat_long as lat_long, ssi.srv_site_alias as site_alias,ssai.componet as component,ssai.attribute_name as attribute_name,ssai.attribute_value as attribute_value, " +
			"ssi.site_classification as site_classification,ssi.mrc as mrc,ssi.nrc as nrc,ssi.arc as arc from vw_si_service_info_all ssi left join vw_service_additional_info ssai on ssai.srv_sys_id = ssi.sys_id and ssai.is_active = 'Y' "+
			"where ssi.srv_product_family_name = 'NPL' and ssi.srv_service_id =:serviceId and ssi.sys_id =:sysid", nativeQuery=true)
	public List<Map<String,Object>> getLatestServiceDetailAttributesForNPL(@Param("serviceId") String serviceId, @Param("sysid") Integer sysid);
	
	
	/**
	 * This Query is used to get service detail attributes based on service Id and product name from service info view.
	 * 
	 * @param serviceId
	 * @param sysid
	 * @return
	 */
	@Query(value="select ssi.sys_id as sys_id, ssi.srv_service_id as serviceid, ssi.srv_product_family_name as product_name,ssi.srv_service_management_option as service_option, " +
			"ssi.srv_product_offering_name as offering_name,ssi.srv_service_id as service_id,ssi.srv_product_family_id as product_id,"
			+ "ssi.IP_address_provided_by as IP_address_provided_by,ssi.additional_ips_req as additional_ip," +
			"ssi.srv_customer_site_address as site_address, ssi.srv_access_type as access_type, ssi.srv_pri_sec as pri_sec, ssi.srv_pri_sec_link as pri_sec_link, " +
			"ssi.srv_service_status as service_status, ssi.srv_lastmile_provider as lastmile_provider,ssi.srv_site_end_interface as interface,ssi.srv_bandwidth as port_bandwidth,"
			+ "ssi.srv_bandwidth_unit as port_bandwidth_unit,ssi.srv_burstable_bw as burstable_bandwidth,ssi.usage_model as usage_model,"
			+ "ssi.ip_address_arrangement_type as ipAddress_arrangement_type,ssi.ipv4_address_pool_size as ipv4_size,ssi.ipv6_address_pool_size as ipv6_size,"
			+ "ssi.routing_protocol as routing_protocol,ssi.srv_lastmile_bandwidth as lastmile_bandwidth,ssi.srv_lastmile_bandwidth_unit as lastmile_bandwidth_unit,"
			+ "ssi.service_varient as service_varient, ssi.service_type as service_type,ssi.resiliency_ind as resiliency_ind,ssi.backup_config_mode as backup_configuration, " +
			"ssi.srv_lat_long as lat_long, ssi.srv_site_alias as site_alias,ssai.componet as component,ssai.attribute_name as attribute_name,ssai.attribute_value as attribute_value, " +
			"ssi.site_classification as site_classification, ssi.mrc as mrc, ssi.nrc as nrc, ssi.arc as arc from vw_si_service_info_all ssi left join vw_service_additional_info ssai on ssai.srv_sys_id = ssi.sys_id and ssai.is_active = 'Y' "+
			"where ssi.srv_product_family_name = 'NDE' and ssi.srv_service_id =:serviceId and ssi.sys_id =:sysid", nativeQuery=true)
	public List<Map<String,Object>> getLatestServiceDetailAttributesForNDE(@Param("serviceId") String serviceId, @Param("sysid") Integer sysid);
	
	@Query(value="SELECT * FROM vw_si_service_info_all si where sys_id in (select max(sys_id) from vw_si_service_info_all where srv_service_id = :serviceId)", nativeQuery=true)
	 Optional<SIServiceInfo> findServiceIdFromViewInfoAll(@Param("serviceId") String serviceId);
    
}
