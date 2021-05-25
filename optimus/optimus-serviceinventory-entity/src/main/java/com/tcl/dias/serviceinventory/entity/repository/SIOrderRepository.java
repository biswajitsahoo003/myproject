package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.SIOrder;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 
 * This file contains the repository class for SIOrder entity
 * 
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface SIOrderRepository extends JpaRepository<SIOrder, Integer> {

	List<SIOrder> findByErfCustCustomerIdAndOrderStatusNotIgnoreCase(String erfCustId, String status);
	
	@Query(value="select ordTbl.op_order_code as orderCode,serviceTbl.id as siServiceDetailId,contractInfoTbl.billing_address as billingAddress,serviceTbl.tax_exemption_flag as taxExemptionFlag,serviceTbl.billing_gst_number as billingGstNumber,serviceTbl.erf_loc_site_address_id as siteLocationId,serviceTbl.billing_account_id as billingAccountId, ordTbl.erf_cust_customer_id as customerId,ordTbl.erf_cust_customer_name as customerName,ordTbl.sfdc_account_id as sfdcAccountId,\r\n" +
			"ordTbl.tps_sfdc_cuid as sfdcCuid, ordTbl.erf_cust_le_id as leId, ordTbl.erf_cust_le_name as leName, ordTbl.customer_segment as customerSegment,\r\n" + 
			"ordTbl.opportunity_classification as opportunityClassification,ordTbl.erf_cust_sp_le_id as supplierLeId, ordTbl.erf_cust_sp_le_name as supplierLeName,\r\n" + 
			"contractInfoTbl.der_price_rev_date as circuitExpiryDate, serviceTbl.tps_service_id as serviceId,serviceTbl.tps_service_id as primaryServiceId, serviceTbl.erf_prd_catalog_product_id as productId, serviceTbl.erf_prd_catalog_offering_id as offeringId\r\n" + 
			",serviceTbl.erf_prd_catalog_product_name as productName, serviceTbl.erf_prd_catalog_offering_name as offeringName, \r\n" +
			"serviceTbl.access_type as accessType, serviceTbl.site_link_label as siteLinkLabel, serviceTbl.lat_long as latLong, serviceTbl.site_address as siteAddress,\r\n" + 
			"serviceTbl.site_topology as siteTopology,serviceTbl.si_order_id as siOrderId, serviceTbl.service_topology as serviceTopology, serviceTbl.service_class as serviceClass, serviceTbl.sm_name as smName,\r\n" + 
			"serviceTbl.sm_email as smEmail, serviceTbl.service_classification as serviceClassification, serviceTbl.site_type as siteType, serviceTbl.service_commissioned_date as commissionedDate,\r\n" +
			"serviceTbl.source_city as sourceCity,serviceTbl.IP_address_provided_by as ipAddressProvidedBy,serviceTbl.primary_secondary as linkType,serviceTbl.pri_sec_service_link as secondaryServiceId,serviceTbl.vpn_name as vpnName, serviceTbl.destination_city as destinationCity,serviceTbl.bw_portspeed as portSpeed,serviceTbl.bw_unit as portSpeedUnit,serviceTbl.site_alias as alias,serviceTbl.remarks as remarks,serviceTbl.is_active as isActive,serviceTbl.source_country as countryName ,\r\n" +
			" serviceTbl.billing_type as billingType, serviceTbl.associate_billable_id as associateBillableId, serviceTbl.service_status as serviceStatus from\r\n" + 
			"si_order ordTbl\r\n" + 
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\r\n" + 
			"left join si_contract_info contractInfoTbl on contractInfoTbl.SI_order_id=ordTbl.id\r\n" +
			"where serviceTbl.erf_prd_catalog_product_id=:productId and serviceTbl.service_status!='Terminated'\r\n" + 
			"and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%')) and serviceTbl.IZO_SDWAN_SRVC_ID is null order by  serviceTbl.vpn_name,serviceTbl.tps_service_id, serviceTbl.primary_tps_service_id, serviceTbl.service_sequence,serviceTbl.service_commissioned_date limit :page , :size ;",nativeQuery=true)
	List<Map<String,Object>> getServiceDetail(@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId,@Param("page") Integer page,@Param("size") Integer size,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);
	
	@Query(value="select ordTbl.op_order_code as orderCode,serviceTbl.id as siServiceDetailId,contractInfoTbl.billing_address as billingAddress,serviceTbl.tax_exemption_flag as taxExemptionFlag,serviceTbl.billing_gst_number as billingGstNumber,serviceTbl.erf_loc_site_address_id as siteLocationId,serviceTbl.billing_account_id as billingAccountId, ordTbl.erf_cust_customer_id as customerId,ordTbl.erf_cust_customer_name as customerName,ordTbl.sfdc_account_id as sfdcAccountId,\r\n" +
			"ordTbl.tps_sfdc_cuid as sfdcCuid, ordTbl.erf_cust_le_id as leId, ordTbl.erf_cust_le_name as leName, ordTbl.customer_segment as customerSegment,\r\n" + 
			"ordTbl.opportunity_classification as opportunityClassification,ordTbl.erf_cust_sp_le_id as supplierLeId, ordTbl.erf_cust_sp_le_name as supplierLeName,\r\n" + 
			"serviceTbl.tps_service_id as serviceId, serviceTbl.tps_service_id as primaryServiceId,serviceTbl.erf_prd_catalog_product_id as productId, serviceTbl.erf_prd_catalog_offering_id as offeringId\r\n" +
			",serviceTbl.erf_prd_catalog_product_name as productName, serviceTbl.erf_prd_catalog_offering_name as offeringName,\r\n" + 
			"serviceTbl.access_type as accessType, serviceTbl.site_link_label as siteLinkLabel, serviceTbl.lat_long as latLong, serviceTbl.site_address as siteAddress,\r\n" + 
			"serviceTbl.site_topology as siteTopology,serviceTbl.si_order_id as siOrderId, serviceTbl.service_topology as serviceTopology, serviceTbl.service_class as serviceClass, serviceTbl.sm_name as smName,\r\n" + 
			"serviceTbl.sm_email as smEmail, serviceTbl.service_classification as serviceClassification, serviceTbl.site_type as siteType, serviceTbl.service_commissioned_date as commissionedDate,\r\n" + 
			"serviceTbl.source_city as sourceCity,serviceTbl.IP_address_provided_by as ipAddressProvidedBy,serviceTbl.primary_secondary as linkType,serviceTbl.pri_sec_service_link as secondaryServiceId,serviceTbl.vpn_name as vpnName, serviceTbl.destination_city as destinationCity,serviceTbl.bw_portspeed as portSpeed,serviceTbl.bw_unit as portSpeedUnit,serviceTbl.site_alias as alias,serviceTbl.remarks as remarks,serviceTbl.is_active as isActive,serviceTbl.source_country as countryName,\r\n" +
			"ordTbl.erf_cust_partner_id AS erfCustPartnerId,\r\n" +
			"ordTbl.erf_cust_partner_le_id AS erfCustPartnerLeId,\r\n" +
			"contractInfoTbl.der_price_rev_date as circuitExpiryDate ,ordTbl.erf_cust_partner_name AS erfCustPartnerName, \r\n" +
			"ordTbl.partner_cuid AS partnerCuId, serviceTbl.billing_type as billingType, serviceTbl.associate_billable_id as associateBillableId, serviceTbl.service_status as serviceStatus \r\n"+
			"from\r\n" + 
			"si_order ordTbl\r\n" + 
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\r\n" + 
			"left join si_contract_info contractInfoTbl on contractInfoTbl.SI_order_id=ordTbl.id\r\n" + 
			"where serviceTbl.erf_prd_catalog_product_id=:productId and serviceTbl.service_status!='Terminated'\r\n" + 
			"and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%')) and serviceTbl.IZO_SDWAN_SRVC_ID is null order by serviceTbl.vpn_name,serviceTbl.tps_service_id, serviceTbl.primary_tps_service_id, serviceTbl.service_sequence,serviceTbl.service_commissioned_date",nativeQuery=true)
    List<Map<String,Object>> getServiceDetail(@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);
	
	@Query(value="select ordTbl.op_order_code as orderCode,serviceTbl.id as siServiceDetailId,contractInfoTbl.billing_address as billingAddress,serviceTbl.tax_exemption_flag as taxExemptionFlag,serviceTbl.billing_gst_number as billingGstNumber,serviceTbl.erf_loc_site_address_id as siteLocationId,serviceTbl.billing_account_id as billingAccountId, ordTbl.erf_cust_customer_id as customerId,ordTbl.erf_cust_customer_name as customerName,ordTbl.sfdc_account_id as sfdcAccountId,\r\n" +
			"ordTbl.tps_sfdc_cuid as sfdcCuid, ordTbl.erf_cust_le_id as leId, ordTbl.erf_cust_le_name as leName, ordTbl.customer_segment as customerSegment,\r\n" + 
			"ordTbl.opportunity_classification as opportunityClassification,ordTbl.erf_cust_sp_le_id as supplierLeId, ordTbl.erf_cust_sp_le_name as supplierLeName,\r\n" + 
			"serviceTbl.tps_service_id as serviceId,serviceTbl.tps_service_id as primaryServiceId, serviceTbl.erf_prd_catalog_product_id as productId, serviceTbl.erf_prd_catalog_offering_id as offeringId\r\n" + 
			",serviceTbl.erf_prd_catalog_product_name as productName, serviceTbl.erf_prd_catalog_offering_name as offeringName,\r\n" + 
			"serviceTbl.access_type as accessType, serviceTbl.site_link_label as siteLinkLabel, serviceTbl.lat_long as latLong, serviceTbl.site_address as siteAddress,\r\n" + 
			"serviceTbl.site_topology as siteTopology,serviceTbl.si_order_id as siOrderId, serviceTbl.service_topology as serviceTopology, serviceTbl.service_class as serviceClass, serviceTbl.sm_name as smName,\r\n" + 
			"serviceTbl.sm_email as smEmail, serviceTbl.service_classification as serviceClassification, serviceTbl.site_type as siteType, serviceTbl.service_commissioned_date as commissionedDate,\r\n" +
			"serviceTbl.source_city as sourceCity,serviceTbl.IP_address_provided_by as ipAddressProvidedBy,serviceTbl.primary_secondary as linkType,serviceTbl.pri_sec_service_link as secondaryServiceId,serviceTbl.vpn_name as vpnName, serviceTbl.destination_city as destinationCity,serviceTbl.bw_portspeed as portSpeed,serviceTbl.bw_unit as portSpeedUnit,serviceTbl.site_alias as alias,serviceTbl.remarks as remarks,serviceTbl.is_active as isActive,serviceTbl.source_country as countryName ,\r\n" +
			"serviceTbl.service_status as serviceStatus, serviceTbl.Multi_vrf_solution as multiVrfSolution from\r\n" + 
			"si_order ordTbl\r\n" + 
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\r\n" +
			"left join si_contract_info contractInfoTbl on contractInfoTbl.SI_order_id=ordTbl.id\r\n" +
			"where (serviceTbl.MULTI_VRF_SOLUTION = :vrfFlag) and serviceTbl.erf_prd_catalog_product_id=:productId and serviceTbl.service_status!='Terminated'\r\n" + 
			"and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and "
			+ "(:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%')"
			+ ") order by  serviceTbl.vpn_name,serviceTbl.tps_service_id, serviceTbl.primary_tps_service_id, serviceTbl.service_sequence,serviceTbl.service_commissioned_date limit :page , :size ;",nativeQuery=true)
	List<Map<String,Object>> getServiceDetailWithMvrf(@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId,@Param("page") Integer page,@Param("size") Integer size,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId,
			@Param("vrfFlag") String vrfFlag);
	
	@Query(value="select ordTbl.op_order_code as orderCode,serviceTbl.id as siServiceDetailId,contractInfoTbl.billing_address as billingAddress,serviceTbl.tax_exemption_flag as taxExemptionFlag,serviceTbl.billing_gst_number as billingGstNumber,serviceTbl.erf_loc_site_address_id as siteLocationId,serviceTbl.billing_account_id as billingAccountId, ordTbl.erf_cust_customer_id as customerId,ordTbl.erf_cust_customer_name as customerName,ordTbl.sfdc_account_id as sfdcAccountId,\r\n" +
			"ordTbl.tps_sfdc_cuid as sfdcCuid, ordTbl.erf_cust_le_id as leId, ordTbl.erf_cust_le_name as leName, ordTbl.customer_segment as customerSegment,\r\n" + 
			"ordTbl.opportunity_classification as opportunityClassification,ordTbl.erf_cust_sp_le_id as supplierLeId, ordTbl.erf_cust_sp_le_name as supplierLeName,\r\n" + 
			"serviceTbl.tps_service_id as serviceId, serviceTbl.tps_service_id as primaryServiceId,serviceTbl.erf_prd_catalog_product_id as productId, serviceTbl.erf_prd_catalog_offering_id as offeringId\r\n" +
			",serviceTbl.erf_prd_catalog_product_name as productName, serviceTbl.erf_prd_catalog_offering_name as offeringName,\r\n" + 
			"serviceTbl.access_type as accessType, serviceTbl.site_link_label as siteLinkLabel, serviceTbl.lat_long as latLong, serviceTbl.site_address as siteAddress,\r\n" + 
			"serviceTbl.site_topology as siteTopology,serviceTbl.si_order_id as siOrderId, serviceTbl.service_topology as serviceTopology, serviceTbl.service_class as serviceClass, serviceTbl.sm_name as smName,\r\n" + 
			"serviceTbl.sm_email as smEmail, serviceTbl.service_classification as serviceClassification, serviceTbl.site_type as siteType, serviceTbl.service_commissioned_date as commissionedDate,\r\n" + 
			"serviceTbl.source_city as sourceCity,serviceTbl.IP_address_provided_by as ipAddressProvidedBy,serviceTbl.primary_secondary as linkType,serviceTbl.pri_sec_service_link as secondaryServiceId,serviceTbl.vpn_name as vpnName, serviceTbl.destination_city as destinationCity,serviceTbl.bw_portspeed as portSpeed,serviceTbl.bw_unit as portSpeedUnit,serviceTbl.site_alias as alias,serviceTbl.remarks as remarks,serviceTbl.is_active as isActive,serviceTbl.source_country as countryName,\r\n" +
			"ordTbl.erf_cust_partner_id AS erfCustPartnerId,\r\n" +
			"ordTbl.erf_cust_partner_le_id AS erfCustPartnerLeId,\r\n" +
			"ordTbl.erf_cust_partner_name AS erfCustPartnerName,\r\n" +
			"ordTbl.partner_cuid AS partnerCuId, serviceTbl.service_status as serviceStatus,serviceTbl.Multi_vrf_solution as multiVrfSolution \r\n"+
			"from\r\n" + 
			"si_order ordTbl\r\n" + 
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\r\n" + 
			"left join si_contract_info contractInfoTbl on contractInfoTbl.SI_order_id=ordTbl.id\r\n" + 
			"where (serviceTbl.MULTI_VRF_SOLUTION = :vrfFlag) and serviceTbl.erf_prd_catalog_product_id=:productId and serviceTbl.service_status!='Terminated'\r\n" + 
			"and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%')) and serviceTbl.IZO_SDWAN_SRVC_ID is null order by serviceTbl.vpn_name,serviceTbl.tps_service_id, serviceTbl.primary_tps_service_id, serviceTbl.service_sequence,serviceTbl.service_commissioned_date",nativeQuery=true)
	List<Map<String,Object>> getServiceDetailWithMvrf(@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId,
			@Param("vrfFlag") String vrfFlag);
	
	@Query(value="select ordTbl.op_order_code as orderCode, serviceTbl.id as siServiceDetailId,contractInfoTbl.billing_address as billingAddress,serviceTbl.tax_exemption_flag as taxExemptionFlag,serviceTbl.billing_gst_number as billingGstNumber,serviceTbl.erf_loc_site_address_id as siteLocationId,serviceTbl.billing_account_id as billingAccountId,ordTbl.erf_cust_customer_id as customerId,ordTbl.erf_cust_customer_name as customerName,ordTbl.sfdc_account_id as sfdcAccountId,\r\n" +
			"ordTbl.tps_sfdc_cuid as sfdcCuid, ordTbl.erf_cust_le_id as leId, ordTbl.erf_cust_le_name as leName, ordTbl.customer_segment as customerSegment,\r\n" + 
			"ordTbl.opportunity_classification as opportunityClassification,ordTbl.erf_cust_sp_le_id as supplierLeId, ordTbl.erf_cust_sp_le_name as supplierLeName,\r\n" + 
			"serviceTbl.tps_service_id as serviceId,serviceTbl.tps_service_id as primaryServiceId, serviceTbl.erf_prd_catalog_product_id as productId, serviceTbl.erf_prd_catalog_offering_id as offeringId\r\n" +
			",serviceTbl.erf_prd_catalog_product_name as productName, serviceTbl.erf_prd_catalog_offering_name as offeringName,\r\n" + 
			"serviceTbl.access_type as accessType, serviceTbl.site_link_label as siteLinkLabel, serviceTbl.lat_long as latLong, serviceTbl.site_address as siteAddress,\r\n" + 
			"serviceTbl.site_topology as siteTopology,serviceTbl.si_order_id as siOrderId, serviceTbl.service_topology as serviceTopology, serviceTbl.service_class as serviceClass, serviceTbl.sm_name as smName,\r\n" + 
			"serviceTbl.sm_email as smEmail, serviceTbl.service_classification as serviceClassification, serviceTbl.site_type as siteType, serviceTbl.service_commissioned_date as commissionedDate,\r\n" + 
			"serviceTbl.source_city as sourceCity,serviceTbl.IP_address_provided_by as ipAddressProvidedBy,serviceTbl.primary_secondary as linkType,serviceTbl.pri_sec_service_link as secondaryServiceId,serviceTbl.vpn_name as vpnName, serviceTbl.destination_city as destinationCity,serviceTbl.bw_portspeed as portSpeed,serviceTbl.bw_unit as portSpeedUnit,serviceTbl.site_alias as alias\r\n" +
			"from\r\n" + 
			"si_order ordTbl\r\n" + 
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\r\n" + 
			"left join si_contract_info contractInfoTbl on contractInfoTbl.SI_order_id=ordTbl.id\r\n" + 
			"where serviceTbl.service_status!='Terminated'\r\n" + 
			"and serviceTbl.is_active='Y' and ordTbl.erf_cust_le_id in (:leIds) order by  serviceTbl.vpn_name,serviceTbl.tps_service_id, serviceTbl.primary_tps_service_id, serviceTbl.service_sequence,serviceTbl.service_commissioned_date",nativeQuery=true)
	List<Map<String,Object>> getServiceDetail(@Param("leIds") List<Integer> leIds);
	
	@Query(value="SELECT ordTbl.erf_cust_le_name AS customerleName,ordTbl.erf_cust_le_id AS customerleId,serviceTbl.tps_service_id AS serviceId,serviceTbl.site_address AS siteAddress,serviceTbl.erf_prd_catalog_product_name AS productName,serviceTbl.source_city AS city,serviceTbl.lat_long AS latLong,serviceTbl.service_status as status,serviceTbl.site_alias AS siteAlias FROM si_order ordTbl INNER JOIN si_service_detail serviceTbl ON serviceTbl.si_order_id = ordTbl.id LEFT JOIN vw_service_inv_ui_exclusion siuv ON serviceTbl.tps_service_id = siuv.service_id AND siuv.is_cust_prtl_exclusion_flg='Y'  \r\n" + 
			"where siuv.service_id IS NULL and serviceTbl.service_status!=:status AND ordTbl.erf_cust_le_id IN (:cusLeIds) AND (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) AND serviceTbl.IZO_SDWAN_SRVC_ID IS NULL AND serviceTbl.lat_long IS NOT NULL AND serviceTbl.erf_prd_catalog_product_name IS NOT NULL",nativeQuery=true)
	List<Map<String,Object>> findByCustLeIdInAndOrderStatus(@Param("cusLeIds") List<Integer> erfCustIds,@Param("status")  String status,@Param("customerId") Integer customerId);
	
	List<SIOrder> findByErfCustLeIdInAndOrderStatus(List<Integer> erfCustIds, String status);
	
	List<SIOrder> findByErfCustLeIdAndOrderStatusNotIgnoreCase(String erfCustId, String status);

	List<SIOrder> findByErfCustCustomerId(String erfCustId);
	
	long countByErfCustLeIdInAndOrderStatusInAndOrderTypeNotIn(List<Integer> erfCustIds,List<String> orderStatus,List<String> orderType);
	
	long countByErfCustLeIdInAndOrderStatusInAndOrderTypeIn(List<Integer> erfCustIds ,List<String> orderStatus,List<String> orderType);
	
	long countByErfCustLeIdAndOrderStatusInAndOrderTypeNotIn(String erfCustId,List<String> orderStatus,List<String> orderType);
	
	long countByErfCustLeIdAndOrderStatusInAndOrderTypeIn(String erfCustId,List<String> orderStatus,List<String> orderType);
	
	SIOrder findByOpOrderCode(String orderCode);
	
	  /**
     * This method is to get the SI information for products other than GSC.
     * 
     * @param leIds
     * @param orderIds
     * @param number
     * @param outpulse
     * @return {@link List<Map<String,Object>>} contains the data.
     */
    @Query(value="select ordTbl.op_order_code as orderCode,serviceTbl.id as siServiceDetailId,contractInfoTbl.billing_address as billingAddress,serviceTbl.tax_exemption_flag as taxExemptionFlag,serviceTbl.billing_gst_number as billingGstNumber,serviceTbl.erf_loc_site_address_id as siteLocationId,serviceTbl.billing_account_id as billingAccountId,ordTbl.erf_cust_customer_id as customerId,ordTbl.erf_cust_customer_name as customerName,ordTbl.sfdc_account_id as sfdcAccountId,\r\n" +
            "ordTbl.tps_sfdc_cuid as sfdcCuid, ordTbl.erf_cust_le_id as leId, ordTbl.erf_cust_le_name as leName, ordTbl.customer_segment as customerSegment,contractInfoTbl.contract_start_date as contractStartDate,contractInfoTbl.contract_end_date as contractEndDate,contractInfoTbl.order_term_in_months as termInMonths,\r\n" +
            "ordTbl.opportunity_classification as opportunityClassification,ordTbl.erf_cust_sp_le_id as supplierLeId, ordTbl.erf_cust_sp_le_name as supplierLeName,\r\n" + 
            "serviceTbl.tps_service_id as serviceId,serviceTbl.tps_service_id as primaryServiceId, serviceTbl.erf_prd_catalog_product_id as productId, serviceTbl.erf_prd_catalog_offering_id as offeringId\r\n" +
            ",serviceTbl.erf_prd_catalog_product_name as productName, serviceTbl.erf_prd_catalog_offering_name as offeringName,\r\n" + 
            "serviceTbl.access_type as accessType, serviceTbl.site_link_label as siteLinkLabel, serviceTbl.lat_long as latLong, serviceTbl.site_address as siteAddress,\r\n" + 
            "serviceTbl.site_topology as siteTopology,serviceTbl.si_order_id as siOrderId, serviceTbl.service_topology as serviceTopology, serviceTbl.service_class as serviceClass, serviceTbl.sm_name as smName,\r\n" + 
            "serviceTbl.sm_email as smEmail, serviceTbl.service_classification as serviceClassification, serviceTbl.site_type as siteType, serviceTbl.service_commissioned_date as commissionedDate,\r\n" + 
            "serviceTbl.source_city as sourceCity,serviceTbl.IP_address_provided_by as ipAddressProvidedBy,serviceTbl.primary_secondary as linkType,serviceTbl.pri_sec_service_link as secondaryServiceId,serviceTbl.vpn_name as vpnName, serviceTbl.destination_city as destinationCity,serviceTbl.bw_portspeed as portSpeed,serviceTbl.bw_unit as portSpeedUnit\r\n" +
            "from\r\n" + 
            "si_order ordTbl\r\n" + 
            "inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\r\n" + 
            "left join si_contract_info contractInfoTbl on contractInfoTbl.SI_order_id=ordTbl.id\r\n" + 
            "where serviceTbl.service_status!='Terminated'\r\n" + 
            "and (ordTbl.erf_cust_le_id in (:leIds) or (ordTbl.erf_cust_partner_le_id in (:partnerLeIds) and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%'))) and serviceTbl.tps_service_id in (:serviceIds) order by  serviceTbl.vpn_name,serviceTbl.tps_service_id, serviceTbl.primary_tps_service_id, serviceTbl.service_sequence,serviceTbl.service_commissioned_date;",nativeQuery=true)
    List<Map<String,Object>> getServiceDetailByServiceIds(@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds,
            @Param("serviceIds") List<String> serviceIds);

    /**
     * This method is to get the SI information for GSC only. 
     * The tollfree no & order id should be passed to filter out the required data.
     * 
     * @param leIds
     * @param orderIds
     * @param number
     * @param outpulse
     * @return {@link List<Map<String,Object>>} contains the data.
     */
    @Query(value="select ordTbl.op_order_code as orderCode, ordTbl.erf_cust_customer_id as customerId,\r\n" + 
    		"ordTbl.erf_cust_customer_name as customerName,ordTbl.sfdc_account_id as sfdcAccountId,\r\n" + 
    		"ordTbl.tps_sfdc_cuid as sfdcCuid, ordTbl.erf_cust_le_id as leId, \r\n" + 
    		"ordTbl.erf_cust_le_name as leName, ordTbl.customer_segment as customerSegment, \r\n" + 
    		"ordTbl.opportunity_classification as opportunityClassification,\r\n" + 
    		"ordTbl.erf_cust_sp_le_id as supplierLeId, ordTbl.erf_cust_sp_le_name as supplierLeName, \r\n" + 
    		"serviceTbl.tps_service_id as serviceId, serviceTbl.id as siServiceDetailId, \r\n" + 
    		"serviceTbl.erf_prd_catalog_product_id as productId, serviceTbl.erf_prd_catalog_offering_id as offeringId, \r\n" + 
    		"serviceTbl.erf_prd_catalog_product_name as productName, \r\n" + 
    		"serviceTbl.erf_prd_catalog_offering_name as offeringName, serviceTbl.access_type as accessType, \r\n" + 
    		"serviceTbl.site_link_label as siteLinkLabel, serviceTbl.lat_long as latLong, \r\n" + 
    		"serviceTbl.site_address as siteAddress, serviceTbl.site_topology as siteTopology, \r\n" + 
    		"serviceTbl.service_topology as serviceTopology, serviceTbl.service_class as serviceClass, \r\n" + 
    		"serviceTbl.sm_name as smName, serviceTbl.sm_email as smEmail, \r\n" + 
    		"serviceTbl.service_classification as serviceClassification, serviceTbl.site_type as siteType, \r\n" + 
    		"serviceTbl.service_commissioned_date as commissionedDate, serviceTbl.source_city as sourceCity, \r\n" + 
    		"serviceTbl.destination_city as destinationCity,serviceTbl.bw_portspeed as portSpeed,\r\n" +
    		"serviceTbl.bw_unit as portSpeedUnit \r\n" + 
    		"from \r\n" + 
    		"si_order ordTbl inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id \r\n" + 
    		"left join si_contract_info contractInfoTbl on contractInfoTbl.SI_order_id=ordTbl.id \r\n" + 
    		"where serviceTbl.service_status!='Terminated' \r\n" + 
    		"and (ordTbl.erf_cust_le_id in (:leIds) or (ordTbl.erf_cust_partner_le_id in (:partnerLeIds) and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%'))) \r\n" +
    		"and serviceTbl.tps_service_id in \r\n" + 
    		"(select SI_service_detail_tps_service_id from si_asset_to_service where SI_Asset_ID = \r\n" + 
    		"(SELECT id FROM si_asset where fqdn = (:number) and name = (:outpulse) and type = 'Toll-Free' limit 1) and is_active = 'Y')\r\n" + 
    		"and serviceTbl.si_order_id in (:orderId) \r\n" + 
    		"order by serviceTbl.tps_service_id,serviceTbl.service_commissioned_date;", nativeQuery=true)
    Map<String,Object> getServiceDetailByServiceIdsForGSC(@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds,
            @Param("orderId") String orderId, @Param("number") String number, @Param("outpulse") String outpulse);
    
    @Query(value="select ordTbl.op_order_code as orderCode, ordTbl.erf_cust_customer_id as customerId,\r\n" + 
    		"ordTbl.erf_cust_customer_name as customerName,ordTbl.sfdc_account_id as sfdcAccountId,\r\n" + 
    		"ordTbl.tps_sfdc_cuid as sfdcCuid, ordTbl.erf_cust_le_id as leId, \r\n" + 
    		"ordTbl.erf_cust_le_name as leName, ordTbl.customer_segment as customerSegment, \r\n" + 
    		"ordTbl.opportunity_classification as opportunityClassification,\r\n" + 
    		"ordTbl.erf_cust_sp_le_id as supplierLeId, ordTbl.erf_cust_sp_le_name as supplierLeName, \r\n" + 
    		"serviceTbl.tps_service_id as serviceId, serviceTbl.id as siServiceDetailId, \r\n" + 
    		"serviceTbl.erf_prd_catalog_product_id as productId, serviceTbl.erf_prd_catalog_offering_id as offeringId, \r\n" + 
    		"serviceTbl.erf_prd_catalog_product_name as productName, \r\n" + 
    		"serviceTbl.erf_prd_catalog_offering_name as offeringName, serviceTbl.access_type as accessType, \r\n" + 
    		"serviceTbl.site_link_label as siteLinkLabel, serviceTbl.lat_long as latLong, \r\n" + 
    		"serviceTbl.site_address as siteAddress, serviceTbl.site_topology as siteTopology, \r\n" + 
    		"serviceTbl.service_topology as serviceTopology, serviceTbl.service_class as serviceClass, \r\n" + 
    		"serviceTbl.sm_name as smName, serviceTbl.sm_email as smEmail, \r\n" + 
    		"serviceTbl.service_classification as serviceClassification, serviceTbl.site_type as siteType, \r\n" + 
    		"serviceTbl.service_commissioned_date as commissionedDate, serviceTbl.source_city as sourceCity, \r\n" + 
    		"serviceTbl.destination_city as destinationCity,serviceTbl.bw_portspeed as portSpeed,\r\n" +
    		"serviceTbl.bw_unit as portSpeedUnit \r\n" + 
    		"from \r\n" + 
    		"si_order ordTbl inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id \r\n" + 
    		"left join si_contract_info contractInfoTbl on contractInfoTbl.SI_order_id=ordTbl.id \r\n" + 
    		"where serviceTbl.service_status!='Terminated' \r\n" + 
    		"and (ordTbl.erf_cust_le_id in (:leIds) or (ordTbl.erf_cust_partner_le_id in (:partnerLeIds) and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%'))) \r\n" +
    		"and serviceTbl.tps_service_id in \r\n" + 
    		"(select SI_service_detail_tps_service_id from si_asset_to_service where SI_Asset_ID = \r\n" + 
    		"(SELECT id FROM si_asset where fqdn = (:number)  and type = 'Toll-Free' limit 1) and is_active = 'Y')\r\n" + 
    		"and serviceTbl.si_order_id in (:orderId) \r\n" + 
    		"order by serviceTbl.tps_service_id,serviceTbl.service_commissioned_date;", nativeQuery=true)
    Map<String,Object> getServiceDetailByServiceIdsWithoutOutpulseForGSC(@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds,
            @Param("orderId") String orderId, @Param("number") String number);


	/**
	 * Querying SI_service_detail to get product family specific info. 
	 * product family is passed as a binding param.
	 * @param leIds
	 * @param id
	 * @return list of map containing data
	 */
    @Query(value="select serviceTbl.service_option as serviceOptionType,serviceTbl.tps_service_id as serviceId,serviceTbl.site_type as siteType,contractInfoTbl.billing_address as billingAddress,serviceTbl.tax_exemption_flag as taxExemptionFlag,serviceTbl.billing_gst_number as billingGstNumber,serviceTbl.erf_loc_site_address_id as siteLocationId,serviceTbl.billing_account_id as billingAccountId, serviceTbl.erf_prd_catalog_offering_name as prdFlavour,serviceTbl.tps_copf_id as orderID, \r\n"+
			"serviceTbl.service_commissioned_date as commissionedDate,serviceTbl.primary_secondary as priSec, \r\n"+
			"concat(serviceTbl.bw_portspeed,' ',serviceTbl.bw_unit)  as portSpeed,serviceTbl.service_topology as serviceTopology,serviceTbl.lastmile_provider as aEndLlProvider, \r\n"+
			"serviceTbl.lastmile_provider as bEndLlProvider ,serviceTbl.source_city as sourceCity, \r\n"+
			"serviceTbl.site_address as sourceAddress,serviceTbl.erf_loc_destination_city_id as destinationCity, \r\n"+
			"serviceTbl.erf_loc_pop_site_address_id as destinationAddress \r\n"+
			"from\r\n" + 
			"si_order ordTbl\r\n" + 
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\r\n" + 
			"left join si_contract_info contractInfoTbl on contractInfoTbl.SI_order_id=ordTbl.id\r\n" + 
			"where serviceTbl.erf_prd_catalog_product_id=:id and serviceTbl.service_status!='Terminated'\r\n" + 
			"and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) order by  serviceTbl.vpn_name, \r\n"+
			"serviceTbl.tps_service_id, serviceTbl.primary_tps_service_id, serviceTbl.service_sequence,serviceTbl.service_commissioned_date;",nativeQuery=true)
	List<Map<String,Object>> getServiceDetailForExcel(@Param("leIds") List<Integer> leIds,@Param("partnerLeIds") List<Integer> partnerLeIds,@Param("id") Integer id);
    @Query(value="select serviceTbl.service_option as serviceOptionType,serviceTbl.tps_service_id as serviceId,serviceTbl.site_type as siteType,contractInfoTbl.billing_address as billingAddress,serviceTbl.tax_exemption_flag as taxExemptionFlag,serviceTbl.billing_gst_number as billingGstNumber,serviceTbl.erf_loc_site_address_id as siteLocationId,serviceTbl.billing_account_id as billingAccountId, serviceTbl.erf_prd_catalog_offering_name as prdFlavour,serviceTbl.tps_copf_id as orderID,serviceTbl.service_commissioned_date as commissionedDate,serviceTbl.primary_secondary as priSec,concat(serviceTbl.bw_portspeed,' ',serviceTbl.bw_unit)as portSpeed,serviceTbl.service_topology as serviceTopology,serviceTbl.lastmile_provider as aEndLlProvider,serviceTbl.lastmile_provider as bEndLlProvider ,serviceTbl.source_city as sourceCity, serviceTbl.site_address as sourceAddress,serviceTbl.erf_loc_destination_city_id as destinationCity,serviceTbl.erf_loc_pop_site_address_id as destinationAddress, serviceTbl.pri_sec_service_link as serviceLink,ordTbl.id as orderSysID, serviceTbl.source_city as originCity, serviceTbl.destination_country_code as destCntryCode, serviceTbl.access_type as accessType,serviceTbl.parent_bundle_service_id as parentID,serviceTbl.parent_bundle_service_name as parentService, ordTbl.erf_cust_customer_name as accountName,ordTbl.erf_cust_le_name as legalEntity,serviceTbl.erf_prd_catalog_product_name as serviceType,serviceTbl.tps_service_id as customerServiceID,serviceTbl.site_alias as  alias,serviceTbl.service_status as finalStatus,serviceTbl.source_country as sourceCountry, serviceTbl.service_type as accessNumberType, assetInfo.asset_name as assetName,assetInfo.asset_type as assetType,origin_ntwrk as originNetwork, SCOPE_OF_MANAGEMENT as scopeOfManagement "
    		+ " from si_order ordTbl inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id left join si_contract_info contractInfoTbl on contractInfoTbl.SI_order_id=ordTbl.id left join vw_service_asset_info assetInfo on assetInfo.service_id=serviceTbl.tps_service_id where serviceTbl.erf_prd_catalog_product_id=:id and serviceTbl.service_status!='Terminated' and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) order by serviceTbl.vpn_name,serviceTbl.tps_service_id, serviceTbl.primary_tps_service_id, serviceTbl.service_sequence,serviceTbl.service_commissioned_date;",nativeQuery=true)
	List<Map<String,Object>> getServiceDetailForExcelData(@Param("leIds") List<Integer> leIds,@Param("partnerLeIds") List<Integer> partnerLeIds,@Param("id") Integer id);
    
    @Query(value="SELECT ord.erf_cust_customer_name as accountName, ord.erf_cust_le_name as legalEntity, sd.site_alias as alias, sd.tps_service_id as customerServiceID, ord.id as orderSysID, sd.erf_prd_catalog_product_name as serviceType, sd.source_country as sourceCountry, sd.source_city as originCity, sd.destination_country_code as destCntryCode, sd.service_type as accessNumberType, sd.access_type as accessType, sd.parent_bundle_service_id as parentID, sd.parent_bundle_service_name as parentService, sd.service_commissioned_date as commissionedDate, sd.service_status as finalStatus, asst.name as accessNumber, asst.type as assetType, asst.origin_ntwrk as originNetwork, sa.attribute_value as endCustName FROM si_order ord INNER JOIN si_service_detail sd ON sd.si_order_id = ord.id INNER JOIN si_asset_to_service sda ON sda.SI_service_detail_id = sd.id INNER JOIN si_asset asst ON asst.id = sda.SI_Asset_ID INNER JOIN si_product_reference prodref ON prodref.id = sd.product_reference_id LEFT JOIN si_order_attributes sa ON sa.SI_order_id = ord.id WHERE (ord.erf_cust_le_id IN (:leIds)  or ord.erf_cust_partner_le_id in (:partnerLeIds)) AND sd.erf_prd_catalog_product_name = 'GSIP' AND asst.type = 'Toll-Free' AND sda.is_active = 'Y' AND sd.is_active = 'Y' AND ord.is_active = 'Y' AND prodref.is_active = 'Y' AND prodref.variant = 'GSIP' AND sd.erf_prd_catalog_product_id =:id",nativeQuery=true)
	List<Map<String,Object>> getServiceDetailForExcelDataGSIP(@Param("leIds") List<Integer> leIds,@Param("partnerLeIds") List<Integer> partnerLeIds,@Param("id") Integer id);


	/**
	 * This Query is to get the service detail info based on serviceDetail Id
	 *
	 * @param leIds
	 * @param orderIds
	 * @param number
	 * @param outpulse
	 * @return {@link List<Map<String,Object>>} contains the data.
	 */
	@Query(value="select ordTbl.op_order_code as orderCode,serviceTbl.id as siServiceDetailId,contractInfoTbl.billing_address as billingAddress,serviceTbl.tax_exemption_flag as taxExemptionFlag,serviceTbl.billing_gst_number as billingGstNumber,serviceTbl.erf_loc_site_address_id as siteLocationId,serviceTbl.billing_account_id as billingAccountId, ordTbl.erf_cust_customer_id as customerId,ordTbl.erf_cust_customer_name as customerName,ordTbl.sfdc_account_id as sfdcAccountId,\r\n" +
			"ordTbl.tps_sfdc_cuid as sfdcCuid, ordTbl.erf_cust_le_id as leId, ordTbl.erf_cust_le_name as leName, ordTbl.customer_segment as customerSegment,contractInfoTbl.contract_start_date as contractStartDate,contractInfoTbl.contract_end_date as contractEndDate,contractInfoTbl.order_term_in_months as termInMonths,\r\n" +
			"ordTbl.opportunity_classification as opportunityClassification,ordTbl.erf_cust_sp_le_id as supplierLeId, ordTbl.erf_cust_sp_le_name as supplierLeName,\r\n" +
			"serviceTbl.tps_service_id as serviceId,serviceTbl.tps_service_id as primaryServiceId, serviceTbl.erf_prd_catalog_product_id as productId, serviceTbl.erf_prd_catalog_offering_id as offeringId\r\n" +
			",serviceTbl.erf_prd_catalog_product_name as productName, serviceTbl.erf_prd_catalog_offering_name as offeringName,\r\n" +
			"serviceTbl.access_type as accessType, serviceTbl.site_link_label as siteLinkLabel, serviceTbl.lat_long as latLong, serviceTbl.site_address as siteAddress,\r\n" +
			"serviceTbl.site_topology as siteTopology,serviceTbl.si_order_id as siOrderId, serviceTbl.service_topology as serviceTopology, serviceTbl.service_class as serviceClass, serviceTbl.sm_name as smName,\r\n" +
			"serviceTbl.sm_email as smEmail, serviceTbl.service_classification as serviceClassification, serviceTbl.site_type as siteType, serviceTbl.service_commissioned_date as commissionedDate,\r\n" +
			"serviceTbl.associate_billable_id as associateBillableId, serviceTbl.source_city as sourceCity,serviceTbl.IP_address_provided_by as ipAddressProvidedBy,serviceTbl.service_status as serviceStatus,serviceTbl.lastmile_provider as lastmileProvider,serviceTbl.primary_secondary as linkType,serviceTbl.pri_sec_service_link as secondaryServiceId,serviceTbl.vpn_name as vpnName, serviceTbl.destination_city as destinationCity,serviceTbl.bw_portspeed as portSpeed,serviceTbl.bw_unit as portSpeedUnit,serviceTbl.site_alias as alias,serviceTbl.show_cos_message as showCosMessage,contractInfoTbl.billing_frequency as billingFrequency, serviceTbl.circuit_expiry_date as circuitExpiryDate, contractInfoTbl.billing_method as billingMethod, contractInfoTbl.payment_term as paymentTerm \r\n" +
			"from\r\n" +
			"si_order ordTbl\r\n" +
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\r\n" +
			"left join si_contract_info contractInfoTbl on contractInfoTbl.SI_order_id=ordTbl.id\r\n" +
			"where serviceTbl.service_status!='Terminated'\r\n" +
			"and serviceTbl.id=:srv_sys_id",nativeQuery=true)
	List<Map<String,Object>> getServiceDetailByServiceId(@Param("srv_sys_id") Integer srv_sys_id );
	
/**
 * This Query is to get distinct cuid based on partnerId
 *
 * @param partnerId
 * @return {@link List<String>} contains the data.
 */
	@Query(value="SELECT DISTINCT ordTbl.tps_sfdc_cuid as sfdcCuid FROM si_order ordTbl WHERE ordTbl.erf_cust_partner_id =:partnerId and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%')",nativeQuery=true)
	List<String> findDistinctCustomerCUIDByPartnerId(@Param("partnerId") Integer partnerId);


	/**
	 * Find total count of siorder based on productId and leIds
	 * @param productId and leIds
	 * @return
	 * 
	 */
	@Query(value="select count(*) from (select distinct so.id from si_order so join si_service_detail ssd on so.id = ssd.si_order_id"
			+ " where ssd.erf_prd_catalog_product_id=:productId and ssd.service_status!='Terminated' and so.erf_cust_le_id in (:leIds)) as orderTotalCount",nativeQuery=true)
	Integer getTotalCountBasedOnProductIdAndCustomerLeIds(@Param("leIds") List<Integer> leIds,@Param("productId") Integer productId);
	
	
	/**
	 * Find distinct city code based on LeIds and productId
	 * @param productId and leIds
	 * @return
	 * 
	 */
	@Query(value="select distinct ssd.dc_city_code as city from si_order so join si_service_detail ssd on so.id = ssd.si_order_id"+
			" where ssd.erf_prd_catalog_product_id=:productId and ssd.service_status!='Terminated'" + 
			" and so.erf_cust_le_id in (:leIds) and ssd.dc_city_code is not null",nativeQuery=true)
	List<String> getDistinctCityBasedOnProductIdAndCustomerLeIds(@Param("leIds") List<Integer> leIds,@Param("productId") Integer productId);
	
	/**
	 * Find distinct business unit based on LeIds and productId
	 * @param productId and leIds
	 * @return
	 * 
	 */
	@Query(value="select distinct sa.business_unit as businessUnit from si_order so join si_service_detail ssd on so.id = ssd.si_order_id"+
				" join si_asset sa on ssd.id=sa.SI_service_detail_id"+
			" where ssd.erf_prd_catalog_product_id=:productId and ssd.service_status!='Terminated'" + 
			" and so.erf_cust_le_id in (:leIds) and sa.business_unit is not null",nativeQuery=true)
	List<String> getDistinctBusinessUnitBasedOnProductIdAndCustomerLeIds(@Param("leIds") List<Integer> leIds,@Param("productId") Integer productId);
	
	/**
	 * Find distinct zone based on LeIds and productId
	 * @param productId and leIds
	 * @return
	 * 
	 */
	@Query(value="select distinct sa.zone as zone from si_order so join si_service_detail ssd on so.id = ssd.si_order_id"+
			" join si_asset sa on ssd.id=sa.SI_service_detail_id"+
			" where ssd.erf_prd_catalog_product_id=:productId and ssd.service_status!='Terminated'" + 
			" and so.erf_cust_le_id in (:leIds) and sa.zone is not null",nativeQuery=true)
	List<String> getDistinctZoneBasedOnProductIdAndCustomerLeIds(@Param("leIds") List<Integer> leIds,@Param("productId") Integer productId);

	List<SIOrder> findAll(Specification<SIOrder> specification);

	Page<SIOrder> findAll(Specification<SIOrder> specDetail, Pageable pageable);

	SIOrder findFirstByOpOrderCodeOrderByIdDesc(String opOrderCode);

	@Query(value = "select ordTbl.op_order_code as orderCode,ordTbl.erf_cust_customer_id as customerId,ordTbl.erf_cust_customer_name as customerName,ordTbl.sfdc_account_id as sfdcAccountId,ordTbl.tps_sfdc_cuid as sfdcCuid,ordTbl.erf_cust_le_id as leId,\n" +
			"ordTbl.erf_cust_le_name as leName,ordTbl.customer_segment as customerSegment,ordTbl.opportunity_classification as opportunityClassification,ordTbl.erf_cust_sp_le_id as supplierLeId,ordTbl.erf_cust_sp_le_name as supplierLeName,\n" +
			"serviceTbl.erf_prd_catalog_parent_product_name as parentProductName,serviceTbl.id as siServiceDetailId,serviceTbl.erf_loc_site_address_id as siteLocationId,serviceTbl.billing_account_id as billingAccountId,serviceTbl.tps_service_id as serviceId,\n" +
			"serviceTbl.tps_service_id as primaryServiceId,serviceTbl.erf_prd_catalog_product_id as productId,serviceTbl.erf_prd_catalog_offering_id as offeringId,serviceTbl.erf_prd_catalog_product_name as productName,serviceTbl.erf_prd_catalog_offering_name as offeringName,\n" +
			"serviceTbl.access_type as accessType,serviceTbl.site_link_label as siteLinkLabel,serviceTbl.lat_long as latLong,serviceTbl.site_address as siteAddress,serviceTbl.site_topology as siteTopology,\n" +
			"serviceTbl.si_order_id as siOrderId,serviceTbl.service_topology as serviceTopology,serviceTbl.service_class as serviceClass,serviceTbl.sm_name as smName,\n" +
			"serviceTbl.sm_email as smEmail,serviceTbl.service_classification as serviceClassification,serviceTbl.site_type as siteType,\n" +
			"serviceTbl.service_commissioned_date as commissionedDate,serviceTbl.source_city as sourceCity,serviceTbl.IP_address_provided_by as ipAddressProvidedBy,\n" +
			"serviceTbl.primary_secondary as linkType,serviceTbl.pri_sec_service_link as secondaryServiceId,serviceTbl.vpn_name as vpnName,serviceTbl.destination_city as destinationCity,serviceTbl.bw_portspeed as portSpeed,serviceTbl.bw_unit as portSpeedUnit,\n" +
			"serviceTbl.site_alias as alias,serviceTbl.remarks as remarks,serviceTbl.is_active as isActive,serviceTbl.source_country as countryName,serviceTbl.tax_exemption_flag as taxExemptionFlag,\n" +
			"serviceTbl.billing_gst_number as billingGstNumber\n" +
			"from\n" +
			"si_order ordTbl\n" +
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\n" +
			"where serviceTbl.service_status!='Terminated' and serviceTbl.is_active = 'Y' and ordTbl.is_active ='Y'\n" +
			"and ordTbl.erf_cust_customer_id = :customerId and serviceTbl.erf_prd_catalog_product_name='GSIP' and serviceTbl.access_type=:accessType\n" +
			"order by serviceTbl.vpn_name,serviceTbl.tps_service_id, serviceTbl.primary_tps_service_id, serviceTbl.service_sequence,serviceTbl.service_commissioned_date limit :page , :size", nativeQuery = true)
	List<Map<String, Object>> getAllServiceDetailsOfGsc(Integer customerId, String accessType, Integer page, Integer size);

	@Query(value = "select count(*) as count\n" +
			"from\n" +
			"si_order ordTbl\n" +
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\n" +
			"where serviceTbl.service_status!='Terminated' and serviceTbl.is_active = 'Y' and ordTbl.is_active ='Y'\n" +
			"and ordTbl.erf_cust_customer_id = :customerId and serviceTbl.erf_prd_catalog_product_name='GSIP' and serviceTbl.access_type=:accessType", nativeQuery = true)
	Integer getCountOfServiceDetailsOfGsc(Integer customerId, String accessType);
	
	@Query(value="select ordTbl.op_order_code as orderCode,serviceTbl.id as siServiceDetailId,contractInfoTbl.billing_address as billingAddress,serviceTbl.tax_exemption_flag as taxExemptionFlag,serviceTbl.billing_gst_number as billingGstNumber,serviceTbl.erf_loc_site_address_id as siteLocationId,serviceTbl.billing_account_id as billingAccountId, ordTbl.erf_cust_customer_id as customerId,ordTbl.erf_cust_customer_name as customerName,ordTbl.sfdc_account_id as sfdcAccountId,\r\n" +
			"ordTbl.tps_sfdc_cuid as sfdcCuid, ordTbl.erf_cust_le_id as leId, ordTbl.erf_cust_le_name as leName, ordTbl.customer_segment as customerSegment,\r\n" + 
			"ordTbl.opportunity_classification as opportunityClassification,ordTbl.erf_cust_sp_le_id as supplierLeId, ordTbl.erf_cust_sp_le_name as supplierLeName,\r\n" + 
			"serviceTbl.tps_service_id as serviceId, serviceTbl.tps_service_id as primaryServiceId,serviceTbl.erf_prd_catalog_product_id as productId, serviceTbl.erf_prd_catalog_offering_id as offeringId\r\n" +
			",serviceTbl.erf_prd_catalog_product_name as productName, serviceTbl.erf_prd_catalog_offering_name as offeringName,\r\n" + 
			"serviceTbl.access_type as accessType, serviceTbl.site_link_label as siteLinkLabel, serviceTbl.lat_long as latLong, serviceTbl.site_address as siteAddress,\r\n" + 
			"serviceTbl.site_topology as siteTopology,serviceTbl.si_order_id as siOrderId, serviceTbl.service_topology as serviceTopology, serviceTbl.service_class as serviceClass, serviceTbl.sm_name as smName,\r\n" + 
			"serviceTbl.sm_email as smEmail, serviceTbl.service_classification as serviceClassification, serviceTbl.site_type as siteType, serviceTbl.service_commissioned_date as commissionedDate,\r\n" + 
			"serviceTbl.source_city as sourceCity,serviceTbl.IP_address_provided_by as ipAddressProvidedBy,serviceTbl.primary_secondary as linkType,serviceTbl.pri_sec_service_link as secondaryServiceId,serviceTbl.vpn_name as vpnName, serviceTbl.destination_city as destinationCity,serviceTbl.bw_portspeed as portSpeed,serviceTbl.bw_unit as portSpeedUnit,serviceTbl.site_alias as alias,serviceTbl.remarks as remarks,serviceTbl.is_active as isActive,serviceTbl.source_country as countryName,\r\n" +
			"ordTbl.erf_cust_partner_id AS erfCustPartnerId,\r\n" +
			"ordTbl.erf_cust_partner_le_id AS erfCustPartnerLeId,\r\n" +
			"ordTbl.erf_cust_partner_name AS erfCustPartnerName,\r\n" +
			"ordTbl.partner_cuid AS partnerCuId\r\n"+
			"from\r\n" + 
			"si_order ordTbl\r\n" + 
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\r\n" + 
			"left join si_contract_info contractInfoTbl on contractInfoTbl.SI_order_id=ordTbl.id\r\n" + 
			"where serviceTbl.erf_prd_catalog_product_id=:productId and serviceTbl.service_status!='Terminated'\r\n" + 
			"and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%')) and serviceTbl.IZO_SDWAN_SRVC_ID is null order by serviceTbl.vpn_name,serviceTbl.tps_service_id, serviceTbl.primary_tps_service_id, serviceTbl.service_sequence,serviceTbl.service_commissioned_date",nativeQuery=true)
	List<Map<String,Object>> getServiceDetailStandAlone(@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);
	
	@Query(value="select ordTbl.op_order_code as orderCode,serviceTbl.id as siServiceDetailId,contractInfoTbl.billing_address as billingAddress,serviceTbl.tax_exemption_flag as taxExemptionFlag,serviceTbl.billing_gst_number as billingGstNumber,serviceTbl.erf_loc_site_address_id as siteLocationId,serviceTbl.billing_account_id as billingAccountId, ordTbl.erf_cust_customer_id as customerId,ordTbl.erf_cust_customer_name as customerName,ordTbl.sfdc_account_id as sfdcAccountId,\r\n" +
			"ordTbl.tps_sfdc_cuid as sfdcCuid, ordTbl.erf_cust_le_id as leId, ordTbl.erf_cust_le_name as leName, ordTbl.customer_segment as customerSegment,\r\n" + 
			"ordTbl.opportunity_classification as opportunityClassification,ordTbl.erf_cust_sp_le_id as supplierLeId, ordTbl.erf_cust_sp_le_name as supplierLeName,\r\n" + 
			"serviceTbl.tps_service_id as serviceId,serviceTbl.tps_service_id as primaryServiceId, serviceTbl.erf_prd_catalog_product_id as productId, serviceTbl.erf_prd_catalog_offering_id as offeringId\r\n" + 
			",serviceTbl.erf_prd_catalog_product_name as productName, serviceTbl.erf_prd_catalog_offering_name as offeringName,\r\n" + 
			"serviceTbl.access_type as accessType, serviceTbl.site_link_label as siteLinkLabel, serviceTbl.lat_long as latLong, serviceTbl.site_address as siteAddress,\r\n" + 
			"serviceTbl.site_topology as siteTopology,serviceTbl.si_order_id as siOrderId, serviceTbl.service_topology as serviceTopology, serviceTbl.service_class as serviceClass, serviceTbl.sm_name as smName,\r\n" + 
			"serviceTbl.sm_email as smEmail, serviceTbl.service_classification as serviceClassification, serviceTbl.site_type as siteType, serviceTbl.service_commissioned_date as commissionedDate,\r\n" +
			"serviceTbl.source_city as sourceCity,serviceTbl.IP_address_provided_by as ipAddressProvidedBy,serviceTbl.primary_secondary as linkType,serviceTbl.pri_sec_service_link as secondaryServiceId,serviceTbl.vpn_name as vpnName, serviceTbl.destination_city as destinationCity,serviceTbl.bw_portspeed as portSpeed,serviceTbl.bw_unit as portSpeedUnit,serviceTbl.site_alias as alias,serviceTbl.remarks as remarks,serviceTbl.is_active as isActive,serviceTbl.source_country as countryName\r\n" +
			"from\r\n" + 
			"si_order ordTbl\r\n" + 
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\r\n" + 
			"left join si_contract_info contractInfoTbl on contractInfoTbl.SI_order_id=ordTbl.id\r\n" +
			"where serviceTbl.erf_prd_catalog_product_id=:productId and serviceTbl.service_status!='Terminated'\r\n" + 
			"and (ordTbl.erf_cust_le_id in (:leIds) or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%')) and serviceTbl.IZO_SDWAN_SRVC_ID is null  order by  serviceTbl.vpn_name,serviceTbl.tps_service_id, serviceTbl.primary_tps_service_id, serviceTbl.service_sequence,serviceTbl.service_commissioned_date limit :page , :size ;",nativeQuery=true)
	List<Map<String,Object>> getServiceDetailStandAloneWithLimit(@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId,@Param("page") Integer page,@Param("size") Integer size,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);
	

	@Query(value="select ordTbl.op_order_code as orderCode,serviceTbl.id as siServiceDetailId,contractInfoTbl.billing_address as billingAddress,serviceTbl.tax_exemption_flag as taxExemptionFlag,serviceTbl.billing_gst_number as billingGstNumber,serviceTbl.erf_loc_site_address_id as siteLocationId,serviceTbl.billing_account_id as billingAccountId, ordTbl.erf_cust_customer_id as customerId,ordTbl.erf_cust_customer_name as customerName,ordTbl.sfdc_account_id as sfdcAccountId,\r\n" +
			"ordTbl.tps_sfdc_cuid as sfdcCuid, ordTbl.erf_cust_le_id as leId, ordTbl.erf_cust_le_name as leName, ordTbl.customer_segment as customerSegment,\r\n" +
			"ordTbl.opportunity_classification as opportunityClassification,ordTbl.erf_cust_sp_le_id as supplierLeId, ordTbl.erf_cust_sp_le_name as supplierLeName,\r\n" +
			"serviceTbl.tps_service_id as serviceId, serviceTbl.tps_service_id as primaryServiceId,serviceTbl.erf_prd_catalog_product_id as productId, serviceTbl.erf_prd_catalog_offering_id as offeringId\r\n" +
			",serviceTbl.erf_prd_catalog_product_name as productName, serviceTbl.erf_prd_catalog_offering_name as offeringName,\r\n" +
			"serviceTbl.access_type as accessType, serviceTbl.site_link_label as siteLinkLabel, serviceTbl.lat_long as latLong, serviceTbl.site_address as siteAddress,\r\n" +
			"serviceTbl.site_topology as siteTopology,serviceTbl.si_order_id as siOrderId, serviceTbl.service_topology as serviceTopology, serviceTbl.service_class as serviceClass, serviceTbl.sm_name as smName,\r\n" +
			"serviceTbl.sm_email as smEmail, serviceTbl.service_classification as serviceClassification, serviceTbl.site_type as siteType, serviceTbl.service_commissioned_date as commissionedDate,\r\n" +
			"serviceTbl.source_city as sourceCity,serviceTbl.IP_address_provided_by as ipAddressProvidedBy,serviceTbl.primary_secondary as linkType,serviceTbl.pri_sec_service_link as secondaryServiceId,serviceTbl.vpn_name as vpnName, serviceTbl.destination_city as destinationCity,serviceTbl.bw_portspeed as portSpeed,serviceTbl.bw_unit as portSpeedUnit,serviceTbl.site_alias as alias,serviceTbl.remarks as remarks,serviceTbl.is_active as isActive,serviceTbl.source_country as countryName,\r\n" +
			"ordTbl.erf_cust_partner_id AS erfCustPartnerId,\r\n" +
			"ordTbl.erf_cust_partner_le_id AS erfCustPartnerLeId,\r\n" +
			"ordTbl.erf_cust_partner_name AS erfCustPartnerName,\r\n" +
			"ordTbl.partner_cuid AS partnerCuId\r\n"+
			"from\r\n" +
			"si_order ordTbl\r\n" +
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\r\n" +
			"left join si_contract_info contractInfoTbl on contractInfoTbl.SI_order_id=ordTbl.id\r\n" +
			"where serviceTbl.erf_prd_catalog_product_id=:productId and serviceTbl.service_status!='Terminated'\r\n" +
			"and (:leIds is null or ordTbl.erf_cust_le_id in (:leIds)) and (:partnerLeIds is null or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%')) order by serviceTbl.vpn_name,serviceTbl.tps_service_id, serviceTbl.primary_tps_service_id, serviceTbl.service_sequence,serviceTbl.service_commissioned_date",nativeQuery=true)
	List<Map<String,Object>> getServiceDetailForPartner(@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);

	@Query(value="select ordTbl.op_order_code as orderCode,serviceTbl.id as siServiceDetailId,contractInfoTbl.billing_address as billingAddress,serviceTbl.tax_exemption_flag as taxExemptionFlag,serviceTbl.billing_gst_number as billingGstNumber,serviceTbl.erf_loc_site_address_id as siteLocationId,serviceTbl.billing_account_id as billingAccountId, ordTbl.erf_cust_customer_id as customerId,ordTbl.erf_cust_customer_name as customerName,ordTbl.sfdc_account_id as sfdcAccountId,\r\n" +
			"ordTbl.tps_sfdc_cuid as sfdcCuid, ordTbl.erf_cust_le_id as leId, ordTbl.erf_cust_le_name as leName, ordTbl.customer_segment as customerSegment,\r\n" +
			"ordTbl.opportunity_classification as opportunityClassification,ordTbl.erf_cust_sp_le_id as supplierLeId, ordTbl.erf_cust_sp_le_name as supplierLeName,\r\n" +
			"serviceTbl.tps_service_id as serviceId,serviceTbl.tps_service_id as primaryServiceId, serviceTbl.erf_prd_catalog_product_id as productId, serviceTbl.erf_prd_catalog_offering_id as offeringId\r\n" +
			",serviceTbl.erf_prd_catalog_product_name as productName, serviceTbl.erf_prd_catalog_offering_name as offeringName,\r\n" +
			"serviceTbl.access_type as accessType, serviceTbl.site_link_label as siteLinkLabel, serviceTbl.lat_long as latLong, serviceTbl.site_address as siteAddress,\r\n" +
			"serviceTbl.site_topology as siteTopology,serviceTbl.si_order_id as siOrderId, serviceTbl.service_topology as serviceTopology, serviceTbl.service_class as serviceClass, serviceTbl.sm_name as smName,\r\n" +
			"serviceTbl.sm_email as smEmail, serviceTbl.service_classification as serviceClassification, serviceTbl.site_type as siteType, serviceTbl.service_commissioned_date as commissionedDate,\r\n" +
			"serviceTbl.source_city as sourceCity,serviceTbl.IP_address_provided_by as ipAddressProvidedBy,serviceTbl.primary_secondary as linkType,serviceTbl.pri_sec_service_link as secondaryServiceId,serviceTbl.vpn_name as vpnName, serviceTbl.destination_city as destinationCity,serviceTbl.bw_portspeed as portSpeed,serviceTbl.bw_unit as portSpeedUnit,serviceTbl.site_alias as alias,serviceTbl.remarks as remarks,serviceTbl.is_active as isActive,serviceTbl.source_country as countryName\r\n" +
			"from\r\n" +
			"si_order ordTbl\r\n" +
			"inner join si_service_detail serviceTbl on serviceTbl.si_order_id = ordTbl.id\r\n" +
			"left join si_contract_info contractInfoTbl on contractInfoTbl.SI_order_id=ordTbl.id\r\n" +
			"where serviceTbl.erf_prd_catalog_product_id=:productId and serviceTbl.service_status!='Terminated'\r\n" +
			"and (:leIds is null or ordTbl.erf_cust_le_id in (:leIds)) and (:partnerLeIds is null or ordTbl.erf_cust_partner_le_id in (:partnerLeIds)) and (:customerId is null or ordTbl.erf_cust_customer_id= :customerId) and (:partnerId is null or ordTbl.erf_cust_partner_id=:partnerId and (ordTbl.opportunity_classification like '%sell with%' or ordTbl.opportunity_classification like '%sell through%')) order by  serviceTbl.vpn_name,serviceTbl.tps_service_id, serviceTbl.primary_tps_service_id, serviceTbl.service_sequence,serviceTbl.service_commissioned_date limit :page , :size ;",nativeQuery=true)
	List<Map<String,Object>> getServiceDetailForPartner(@Param("leIds") List<Integer> leIds, @Param("partnerLeIds") List<Integer> partnerLeIds, @Param("productId") Integer productId,@Param("page") Integer page,@Param("size") Integer size,@Param("customerId") Integer customerId,@Param("partnerId") Integer partnerId);
	
	@Query(value="SELECT so.* FROM si_order so WHERE so.erf_cust_partner_le_id =:partnerLeId group by so.erf_cust_le_id",nativeQuery=true)
	List<SIOrder> findCustomerLeByPartnerLeId(@Param("partnerLeId") Integer partnerLeId);
	
	@Query(value="SELECT so.erf_cust_le_id FROM si_order so WHERE so.erf_cust_partner_id =:partnerId group by so.erf_cust_le_id",nativeQuery=true)
	List<Integer> findCustomerLesByPartnerId(@Param("partnerId") Integer partnerId);

	@Query(value = "select distinct ssd.service_varient, pr.sub_variant from si_order ord left join si_service_detail ssd on ord.id = ssd.si_order_id " +
			"left join si_product_reference pr on ssd.product_reference_id = pr.id where ord.erf_cust_le_id = :erfCustLeId " +
			"and pr.sub_variant = :subVariant and ord.erf_cust_sp_le_id = :erfSupplierLeId order by ord.order_start_date desc", nativeQuery = true)
	List<Map<String, String>> getGscServiceAbbreviation(@Param("erfCustLeId") String erfCustLeId, @Param("erfSupplierLeId") String erfSupplierLeId, @Param("subVariant") String subVariant);

}
