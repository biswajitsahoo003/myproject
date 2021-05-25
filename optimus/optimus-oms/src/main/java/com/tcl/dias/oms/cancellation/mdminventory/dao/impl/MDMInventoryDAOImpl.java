package com.tcl.dias.oms.cancellation.mdminventory.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tcl.dias.common.beans.MDMServiceDetailBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.ServiceDetailBean;
import com.tcl.dias.oms.beans.MDMServiceInventoryBean;
import com.tcl.dias.oms.cancellation.mdminventory.dao.MDMInventoryDAO;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Repository
public class MDMInventoryDAOImpl implements MDMInventoryDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MDMInventoryDAOImpl.class);
	
	@Autowired
	JdbcTemplate jdbcTemplateMDMLake;

	@Override
	public MDMServiceInventoryBean getInventoryDetails(Integer page, Integer size, Integer customerId, Integer customerLeId, 
			Integer opportunityId, String orderCode, String serviceId, String status, String customerName) throws TclCommonException {
		LOGGER.info("MDMInventoryDAOImpl getInventoryDetails Customer ID {}, customer LeId {}, page {}, size {}, oppId {}, orderId {}, serviceId {}, status {}", customerId, customerLeId, page, size,  opportunityId, orderCode, serviceId, status);
		MDMServiceInventoryBean mdmInventoryBean = new MDMServiceInventoryBean();
		List<Map<String,Object>> customerLegalInfo =new ArrayList<>();
		try {
			
			String condition = "";
			

			// String sql = "SELECT CUID, Legal_Entity, Billing_Method, Billing_Type, Billing_Frequency, Payment_Term  from optimus_customer_onboarding_legal where CUID = " + customerId + " limit "+(page-1)+","+size;
			
			if(customerLeId != null)
				condition += " and order_cust_le_id = " + customerLeId;
			if(customerId != null)
				condition += " and order_customer_id = " + customerId;
			if(opportunityId != null)
				condition += " and opportunity_id = " + opportunityId;
			if(orderCode != null)
				condition += " and order_code = '" + orderCode + "'";
			if(serviceId != null)
				condition += " and srv_service_id = '" + serviceId + "'";
			if(status != null) 
				condition += " and srv_service_status = '" + status + "'";
			if(customerName != null) 
				condition += " and order_customer_name like '%" + customerName + "%'";
			 
			
			LOGGER.info("condition {}", condition);
			List<MDMServiceDetailBean> servicesList = findServiceDetailsByPage(page,size,customerId, customerLeId, opportunityId, orderCode, serviceId, status, condition);
			
			
			
			mdmInventoryBean.setServiceDetailBeans(servicesList);
			int totalItems = findRowCount(customerId,condition);
			mdmInventoryBean.setTotalItems(totalItems);
			mdmInventoryBean.setTotalPages((int) Math.ceil((double) totalItems / size));
			LOGGER.info("mdmbean total items {}, total pages {}", mdmInventoryBean.getTotalItems(), mdmInventoryBean.getTotalPages());
			//customerLegalInfo = jdbcTemplateMDMLake.queryForList(billingDetails, customerLeId);
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		
		return mdmInventoryBean;
	}
	
	
	
	public List<MDMServiceDetailBean> findServiceDetailsByPage(Integer page, Integer size, Integer customerId, 
			Integer customerLeId, Integer opportunityId, String orderCode, String serviceId, String status, String condition) {
		page = (page -1) * size;
		String sql = null;
	
			
			if(page != null && size != null) {
				if(StringUtils.isNotBlank(condition)) {
				sql = "SELECT * from view_Cancellation_orders where 1=1" + condition + " limit "+(page)+","+size;
			} else {
				sql = "SELECT * from view_Cancellation_orders where 1=1"  + " limit "+(page)+","+size;
			}
			}
			
		 LOGGER.info("sql {}", sql);
	        List<MDMServiceDetailBean> serviceDetailsList  = jdbcTemplateMDMLake.query(sql, new RowMapper<MDMServiceDetailBean>() {
	            @Override
	            public MDMServiceDetailBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	MDMServiceDetailBean serviceDetailBean = new MDMServiceDetailBean();
	            	serviceDetailBean.setCustomerId(rs.getString("order_customer_id"));
	            	serviceDetailBean.setLeId(rs.getString("order_cust_le_id"));
	            	serviceDetailBean.setOfferingName(rs.getString("srv_product_offering_name"));
	            	serviceDetailBean.setOrderCode(rs.getString("order_code"));
	            	serviceDetailBean.setProductName(rs.getString("srv_product_family_name"));
	            	serviceDetailBean.setPortSpeed(rs.getString("srv_bandwidth"));
	            	serviceDetailBean.setServiceId(rs.getString("srv_service_id"));
	            	serviceDetailBean.setServiceStatus(rs.getString("srv_service_status"));
	            	serviceDetailBean.setSiteAddress(rs.getString("A_end_site_address"));
	            	serviceDetailBean.setTermInMonths(rs.getInt("order_term_in_months"));
	            	serviceDetailBean.setSourceSystem(rs.getString("source_system"));
	            	serviceDetailBean.setSiteCode(rs.getString("site_code"));
	            	serviceDetailBean.setPrimarySecondary(rs.getString("srv_pri_sec"));
	            	serviceDetailBean.setOpportunityClassification(rs.getString("opportunity_type"));
	            	serviceDetailBean.setCustomerName(rs.getString("order_customer_name"));
	            	serviceDetailBean.setLeName(rs.getString("order_cust_le_name"));
	            	serviceDetailBean.setSiteLocationId(rs.getString("A_end_erf_loc_site_address_id"));
	            	serviceDetailBean.setLocalLoopBandwidth(rs.getString("A_END_srv_lastmile_bandwidth"));
	            	serviceDetailBean.setSfdcCuid(rs.getString("CUID"));
	            	serviceDetailBean.setOrderType(rs.getString("quote_type"));
	            	serviceDetailBean.setOrderCategory(rs.getString("quote_category"));
	            	serviceDetailBean.setOpportunityId(rs.getString("opportunity_id"));
	            	serviceDetailBean.setSiteType(rs.getString("site_type"));
	            	serviceDetailBean.setLocalLoopBandwidthBEnd(rs.getString("B_END_srv_lastmile_bandwidth"));
	            	serviceDetailBean.setSiteAddressBEnd(rs.getString("B_end_site_address"));
	            	serviceDetailBean.setSiteLocationIdBEnd(rs.getString("B_end_erf_loc_site_address_id"));
	            	serviceDetailBean.setLinkCode(rs.getString("link_code"));
	            	serviceDetailBean.setSupplierLeId(rs.getString("order_sp_le_id"));
	            	serviceDetailBean.setCustomerContactName(rs.getString("Customer_contact_Name"));
	            	serviceDetailBean.setCustomerContactEmail(rs.getString("Customer_contact_Email"));
	            	serviceDetailBean.setCustomerContactNumber(rs.getString("Customer_contact_Number"));
	            	serviceDetailBean.setAccessType(rs.getString("ACCESS_TYPE"));
	            	serviceDetailBean.setPoNumber(rs.getString("PO_Number"));
	            	serviceDetailBean.setPaymentType(rs.getString("Payment_Type"));
	            	serviceDetailBean.setPaymentMethod(rs.getString("Payment_Method"));
	            	serviceDetailBean.setPaymentTerm(rs.getString("Payment_Term"));
	            	serviceDetailBean.setProgramManager(rs.getString("Program_Manager"));
	            	serviceDetailBean.setGstNumber(rs.getString("GST_Number"));
	            	serviceDetailBean.setAccountManager(rs.getString("Account_Manager"));
	            	serviceDetailBean.setCrnId(rs.getString("CRN_ID"));
	            	serviceDetailBean.setCustomerSegment(rs.getString("CUSTOMER_SEGMENT"));
	            	serviceDetailBean.setFeasibilityId(rs.getString("CUSTOMER_SEGMENT"));
	            	serviceDetailBean.setBillingType(rs.getString("BILLING_TYPE"));
	            	serviceDetailBean.setIsMultiVrf(rs.getString("MULTI_VRF_SOLUTION"));
	            	serviceDetailBean.setAlias(rs.getString("ALIAS"));
	            	serviceDetailBean.setCosProfile(rs.getString("COS_PROFILE"));
	            	serviceDetailBean.setSltVariant(rs.getString("SLT_VARIANT"));
	            	serviceDetailBean.setVpnTopology(rs.getString("VPN_TOPOLOGY"));
	            	serviceDetailBean.setSiteTopology(rs.getString("Site_Topology"));
	            	serviceDetailBean.setCosModel(rs.getString("COS_MODEL"));
	            	serviceDetailBean.setCos1(rs.getString("CoS1"));
	            	serviceDetailBean.setCos2(rs.getString("CoS2"));
	            	serviceDetailBean.setCos3(rs.getString("CoS3"));
	            	serviceDetailBean.setCos4(rs.getString("CoS4"));
	            	serviceDetailBean.setCos5(rs.getString("CoS5"));
	            	serviceDetailBean.setCos6(rs.getString("CoS6"));
	            	serviceDetailBean.setScopeOfManagement(rs.getString("SCOPE_OF_MANAGEMENT"));
	            	serviceDetailBean.setAdditionalIPv4Address(rs.getString("ADDITIONAL_IPV4_ADDRESS"));
	            	serviceDetailBean.setSolutionType(rs.getString("Solution_Type"));
	            	serviceDetailBean.setWanIPAddressApprovedBy(rs.getString("WAN_IP_ADDRESS_PROV_BY"));
	            	serviceDetailBean.setSiteInterface(rs.getString("INTERFACE"));
	            	serviceDetailBean.setCpeBasicChassis(rs.getString("CPE_Basic_chasis"));
	            	serviceDetailBean.setConnectorType(rs.getString("CONNECTOR_TYPE"));
	            	serviceDetailBean.setaEndChargeableDistance(rs.getString("A_END_CHARGEABLE_DISTANCE"));
	            	serviceDetailBean.setbEndChargeableDistance(rs.getString("B_END_CHARGEABLE_DISTANCE"));
	            	serviceDetailBean.setCpeManaged(rs.getString("CPE_MANAGED"));
	            	serviceDetailBean.setCpeArrangedBy(rs.getString("CPE_ARRANGED_BY"));
	            	serviceDetailBean.setBillingMethod(rs.getString("billing_method"));
	            	serviceDetailBean.setBillingFrequency(rs.getString("BILLING_FREQUENCY"));
	            	serviceDetailBean.setEquipmentMake(rs.getString("EQUIPMENT_MAKE"));
	            	serviceDetailBean.setaEndConnectorType(rs.getString("A_END_CONNECTOR_TYPE"));
	            	serviceDetailBean.setbEndConnectorType(rs.getString("B_END_CONNECTOR_TYPE"));
	            	serviceDetailBean.setaEndInterface(rs.getString("A_END_INTERFACE"));
	            	serviceDetailBean.setbEndInterface(rs.getString("B_END_INTERFACE"));
	            	serviceDetailBean.setNetworkProtection(rs.getString("Network_Protection"));
	            	serviceDetailBean.setCircuitType(rs.getString("CIRCUIT_TYPE"));
	            	serviceDetailBean.setPortMode(rs.getString("Port_Mode"));
	            	serviceDetailBean.setCopfId(rs.getString("COPF_ID"));
	            	serviceDetailBean.setNplProductFlavour(rs.getString("product_flavour"));
	            	serviceDetailBean.setMrc(rs.getDouble("mrc"));
	            	serviceDetailBean.setNrc(rs.getDouble("nrc"));
	            	serviceDetailBean.setArc(rs.getDouble("arc"));
	            	serviceDetailBean.setCurrencyCode(rs.getString("currency_code"));
	            	serviceDetailBean.setBillingCurrency(rs.getString("billing_currency"));
	            	serviceDetailBean.setPaymentCurrency(rs.getString("payment_currency"));
	            	serviceDetailBean.setBillingCurrencyPos(rs.getString("BILLING_CURRENCY_POS"));
	            	serviceDetailBean.setPaymentCurrencyPos(rs.getString("PAYMENT_CURRENCY_POS"));
	            	serviceDetailBean.setMrcPos(rs.getDouble("MRC_POS"));
	            	serviceDetailBean.setArcPos(rs.getDouble("ARC_POS"));
	            	serviceDetailBean.setNrcPos(rs.getDouble("NRC_POS"));
	            	serviceDetailBean.setPaymentMethodPos(rs.getString("PAYMENT_METHOD_POS"));
	            	return serviceDetailBean;
	            }
	        });
	        LOGGER.info("service details bean {}", serviceDetailsList.toString());
	        return serviceDetailsList;
	    
    }
	
	private int findRowCount(Integer customerLeId, String condition) {
		String rowCountSql = null;
		if(condition != null) {
		 rowCountSql = "SELECT count(1) AS row_count " +
                "FROM view_Cancellation_orders " +
                "WHERE 1=1" + condition;
		} else {
			 rowCountSql = "SELECT count(1) AS row_count " +
	                "FROM view_Cancellation_orders " +
	                "WHERE 1=1";
		}
        int total =
        		jdbcTemplateMDMLake.queryForObject(
                        rowCountSql, (rs, rowNum) -> rs.getInt(1)
                );
        return total;
	}



	@Override
	public List<Map<String, Object>>  getPosServiceDetailByOrderCodeAndServiceId(String orderCode, List<String> serviceIds) throws TclCommonException {
		String serviceId = String.join(",", serviceIds);
		String query = "select srv_product_family_name as productName, srv_product_offering_name as offeringName,"
				+ " A_end_erf_loc_site_address_id as siteALocationId, B_end_erf_loc_site_address_id as siteBLocationId , srv_service_id as serviceId from view_Cancellation_orders where source_system!='OPTIMUS_O2C' and order_code='"+orderCode+"' and srv_service_id in (?)" ;
		LOGGER.info("Query {}",query);
		return jdbcTemplateMDMLake.queryForList(query, serviceIds);

	}

}
