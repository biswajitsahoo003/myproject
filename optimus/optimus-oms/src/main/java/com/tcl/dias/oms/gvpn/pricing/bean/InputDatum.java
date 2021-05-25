
package com.tcl.dias.oms.gvpn.pricing.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * This file is used to set the input request for feasibility model
 * @author PAULRAJ SUNDAR
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "bw_mbps",
    "customer_segment",
    "sales_org",
    "product_name",
    "local_loop_interface",
    "last_mile_contract_term",
    "site_id",
    "prospect_name",
    "burstable_bw",
    "resp_city",
    "account_id_with_18_digit",
    "opportunity_term",
    "quotetype_quote",
    "connection_type",
    "sum_no_of_sites_uni_len",
    "cpe_variant",
    "cpe_management_type",
    "cpe_supply_type",
    "topology",
    "latitude_final",
    "longitude_final",
    "feasibility_response_created_date",
    "additional_ip_flag",
    "ip_address_arrangement",
    "ipv4_address_pool_size",
    "ipv6_address_pool_size",
    "country",
    "siteFlag",
    "cu_le_id","product_solution",
    "macd_service",
    "ll_change",
    "macd_option",
    "trigger_feasibility",
    "old_contract_term",
    "service_commissioned_date",
    "lat_long",
    "service_id",
    "backup_port_requested",
    "old_Port_Bw",
    "old_Ll_Bw",
    "vpn_Name",
    "parallel_run_days",
    "prd_category",
    "cpe_chassis_changed",
    "local_loop_bw",
    "latitude",
    "longitude",
    "bandwidth",
    "product",
    "address",
    "zip_code",
    "partner_account_id_with_18_digit",
    "partner_profile",
    "quotetype_partner",
    "deal_reg_flag",
    "izo_sdwan",
    "access_provider",
    "non_standard",
    "is_customer",
    "multi_vrf_flag",
    "billing_type",
    "no_of_vrfs"

})
public class InputDatum {

	@JsonProperty("cpe_chassis_changed")
	private String cpeChassisChanged;
	 @JsonProperty("prd_category")
	  private String prd_category; 
    @JsonProperty("bw_mbps")
    private Double bwMbps;
    @JsonProperty("customer_segment")
    private String customerSegment;
    @JsonProperty("sales_org")
    private String salesOrg;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("local_loop_interface")
    private String localLoopInterface;
    @JsonProperty("last_mile_contract_term")
    private String lastMileContractTerm;
    @JsonProperty("site_id")
    private String siteId;
    @JsonProperty("prospect_name")
    private String prospectName;
    @JsonProperty("burstable_bw")
    private Double burstableBw;
    @JsonProperty("resp_city")
    private String respCity;
    @JsonProperty("account_id_with_18_digit")
    private String accountIdWith18Digit;
    @JsonProperty("opportunity_term")
    private Integer opportunityTerm;
    @JsonProperty("quotetype_quote")
    private String quotetypeQuote;
    @JsonProperty("connection_type")
    private String connectionType;
    @JsonProperty("sum_no_of_sites_uni_len")
    private Integer sumNoOfSitesUniLen;
    @JsonProperty("cpe_variant")
    private String cpeVariant;
    @JsonProperty("cpe_management_type")
    private String cpeManagementType;
    @JsonProperty("cpe_supply_type")
    private String cpeSupplyType;
    @JsonProperty("topology")
    private String topology;
    @JsonProperty("latitude_final")
    private Double latitudeFinal;
    @JsonProperty("longitude_final")
    private Double longitudeFinal;
    @JsonProperty("feasibility_response_created_date")
    private String feasibilityResponseCreatedDate;
    @JsonProperty("additional_ip_flag")
    private String additionalIpFlag;
    @JsonProperty("ip_address_arrangement")
    private String ipAddressArrangement;
    @JsonProperty("ipv4_address_pool_size")
    private String ipv4AddressPoolSize;
    @JsonProperty("ipv6_address_pool_size")
    private String ipv6AddressPoolSize;
    @JsonProperty("country")
    private String country;
    @JsonProperty("siteFlag")
    private String siteFlag;
    @JsonProperty("cu_le_id")
	private String cuLeId;
    @JsonProperty("product_solution")
	private String productSolution;
    @JsonProperty("geo_code")
    private String geoCode;
    @JsonProperty("send_to_manual")
    private String sendToManual;
    @JsonProperty("geo_code_error_dec")
    private String geoCodeErrorDec;
    @JsonProperty("old_contract_term")
    private String oldContractTerm;
    @JsonProperty("service_commissioned_date")
    private String serviceCommissionedDate;
    @JsonProperty("macd_option")
    private String macdOption;
    @JsonProperty("trigger_feasibility")
    private String triggerFeasibility;
    @JsonProperty("ll_change")
    private String llChange;
    @JsonProperty("macd_service")
    private String macdService;
    @JsonProperty("lat_long")
    private String latLong;
    @JsonProperty("service_id")
    private String serviceId;
	@JsonProperty("backup_port_requested")
    private String backupPortRequested;
	@JsonProperty("old_Port_Bw")
	private String oldPortBw;
	@JsonProperty("old_Ll_Bw")
	private String oldLlBw;
	@JsonProperty("vpn_Name")
	private String vpnName;
	@JsonProperty("parallel_run_days")
	private String parallelRunDays;
	@JsonProperty("local_loop_bw")
	private Double localLoopBw;
	// new params added on 16/05/2019
	@JsonProperty("latitude")
	private String latitude;
	@JsonProperty("longitude")
	private String longitude;
	@JsonProperty("bandwidth")
	private String bandwidth;
	@JsonProperty("product")
	private String product;
	@JsonProperty("address")
	private String address;
	@JsonProperty("zip_code")
	private String zipCode;
    @JsonProperty("partner_account_id_with_18_digit")
    private String partnerAccountIdWith18Digit;
    @JsonProperty("partner_profile")
    private String partnerProfile;
    @JsonProperty("quotetype_partner")
    private String quoteTypePartner;
    @JsonProperty("deal_reg_flag")
    private String dealRegFlag;
    
    @JsonProperty("izo_sdwan")
    private String izoSdwan;
    
    @JsonProperty("access_provider")
    private String accessProvider;
    @JsonProperty("cpe_intl_chassis_flag")
    private String cpeIntlChassisFlag;
    @JsonProperty("non_standard")
    private String nonStandard;
    
    
    
    //CST-21 customer portal changes
    @JsonProperty("is_customer")
    private String isCustomer;

    @JsonProperty("is_demo")
    private String isDemo;
    @JsonProperty("demo_type")
    private String demoType;
	
	public String getServiceCommissionedDate() {
		return serviceCommissionedDate;
	}

	public void setServiceCommissionedDate(String serviceCommissionedDate) {
		this.serviceCommissionedDate = serviceCommissionedDate;
	}

	public String getIzoSdwan() {
		return izoSdwan;
	}

	public void setIzoSdwan(String izoSdwan) {
		this.izoSdwan = izoSdwan;
	}	
    @JsonProperty("resp_state")
	private String respState;	
    
  //mutli VRF
    @JsonProperty("multi_vrf_flag")
	private String multiVrf;
    
    @JsonProperty("billing_type")
   	private String vrfBillingType;
    
    @JsonProperty("no_of_vrfs")
   	private String NoofVRFs;
    
    @JsonProperty("multi_vrf_flag")
    public String getMultiVrf() {
		return multiVrf;
	}

    @JsonProperty("multi_vrf_flag")
	public void setMultiVrf(String multiVrf) {
		this.multiVrf = multiVrf;
	}

    @JsonProperty("billing_type")
	public String getVrfBillingType() {
		return vrfBillingType;
	}

    @JsonProperty("billing_type")
	public void setVrfBillingType(String vrfBillingType) {
		this.vrfBillingType = vrfBillingType;
	}

    @JsonProperty("no_of_vrfs")
	public String getNoofVRFs() {
		return NoofVRFs;
	}

    @JsonProperty("no_of_vrfs")
	public void setNoofVRFs(String noofVRFs) {
		NoofVRFs = noofVRFs;
	}
	


    @JsonProperty("Last_mile_type")
    private String lastMileType;


    @JsonProperty("address_line_a")
    private String addressLineA;


    // Attribute to indicate the quote is non standard or not
    public String getNonStandard() {
		return nonStandard;
	}

	public void setNonStandard(String nonStandard) {
		this.nonStandard = nonStandard;
	}

	@JsonProperty("local_loop_bw")
	public Double getLocalLoopBw() {
		return localLoopBw;
	}

	@JsonProperty("local_loop_bw")
	public void setLocalLoopBw(Double localLoopBw) {
		this.localLoopBw = localLoopBw;
	}



	@JsonProperty("cpe_chassis_changed")
	public String getCpeChassisChanged() {
		return cpeChassisChanged;
	}

	@JsonProperty("cpe_chassis_changed")
	public void setCpeChassisChanged(String cpeChasisChanged) {
		this.cpeChassisChanged = cpeChasisChanged;
	}



	@JsonProperty("parallel_run_days")
	public String getParallelRunDays() {
		return parallelRunDays;
	}

	@JsonProperty("parallel_run_days")
	public void setParallelRunDays(String parallelRunDays) {
		this.parallelRunDays = parallelRunDays;
	}


	@JsonProperty("bw_mbps")
	public Double getBwMbps() {
		return bwMbps;
	}

	@JsonProperty("bw_mbps")
	public void setBwMbps(Double bwMbps) {
		this.bwMbps = bwMbps;
	}

    @JsonProperty("customer_segment")
    public String getCustomerSegment() {
        return customerSegment;
    }

    @JsonProperty("customer_segment")
    public void setCustomerSegment(String customerSegment) {
        this.customerSegment = customerSegment;
    }

    @JsonProperty("sales_org")
    public String getSalesOrg() {
        return salesOrg;
    }

    @JsonProperty("sales_org")
    public void setSalesOrg(String salesOrg) {
        this.salesOrg = salesOrg;
    }

    @JsonProperty("product_name")
    public String getProductName() {
        return productName;
    }

    @JsonProperty("product_name")
    public void setProductName(String productName) {
        this.productName = productName;
    }

    @JsonProperty("local_loop_interface")
    public String getLocalLoopInterface() {
        return localLoopInterface;
    }

    @JsonProperty("local_loop_interface")
    public void setLocalLoopInterface(String localLoopInterface) {
        this.localLoopInterface = localLoopInterface;
    }

    @JsonProperty("last_mile_contract_term")
    public String getLastMileContractTerm() {
        return lastMileContractTerm;
    }

    @JsonProperty("last_mile_contract_term")
    public void setLastMileContractTerm(String lastMileContractTerm) {
        this.lastMileContractTerm = lastMileContractTerm;
    }

    @JsonProperty("site_id")
    public String getSiteId() {
        return siteId;
    }

    @JsonProperty("site_id")
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    @JsonProperty("prospect_name")
    public String getProspectName() {
        return prospectName;
    }

    @JsonProperty("prospect_name")
    public void setProspectName(String prospectName) {
        this.prospectName = prospectName;
    }

    @JsonProperty("burstable_bw")
    public Double getBurstableBw() {
        return burstableBw;
    }

    @JsonProperty("burstable_bw")
    public void setBurstableBw(Double burstableBw) {
        this.burstableBw = burstableBw;
    }

    @JsonProperty("resp_city")
    public String getRespCity() {
        return respCity;
    }

	@JsonProperty("resp_city")
    public void setRespCity(String respCity) {
        this.respCity = respCity;
    }

    @JsonProperty("account_id_with_18_digit")
    public String getAccountIdWith18Digit() {
        return accountIdWith18Digit;
    }

    @JsonProperty("account_id_with_18_digit")
    public void setAccountIdWith18Digit(String accountIdWith18Digit) {
        this.accountIdWith18Digit = accountIdWith18Digit;
    }

    @JsonProperty("opportunity_term")
    public Integer getOpportunityTerm() {
        return opportunityTerm;
    }

    @JsonProperty("opportunity_term")
    public void setOpportunityTerm(Integer opportunityTerm) {
        this.opportunityTerm = opportunityTerm;
    }

    @JsonProperty("quotetype_quote")
    public String getQuotetypeQuote() {
        return quotetypeQuote;
    }

    @JsonProperty("quotetype_quote")
    public void setQuotetypeQuote(String quotetypeQuote) {
        this.quotetypeQuote = quotetypeQuote;
    }

    @JsonProperty("connection_type")
    public String getConnectionType() {
        return connectionType;
    }

    @JsonProperty("connection_type")
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    @JsonProperty("sum_no_of_sites_uni_len")
    public Integer getSumNoOfSitesUniLen() {
        return sumNoOfSitesUniLen;
    }

    @JsonProperty("sum_no_of_sites_uni_len")
    public void setSumNoOfSitesUniLen(Integer sumNoOfSitesUniLen) {
        this.sumNoOfSitesUniLen = sumNoOfSitesUniLen;
    }

    @JsonProperty("cpe_variant")
    public String getCpeVariant() {
        return cpeVariant;
    }

    @JsonProperty("cpe_variant")
    public void setCpeVariant(String cpeVariant) {
        this.cpeVariant = cpeVariant;
    }

    @JsonProperty("cpe_management_type")
    public String getCpeManagementType() {
        return cpeManagementType;
    }

    @JsonProperty("prd_category")
    public String getPrd_category() {
		return prd_category;
	}

    @JsonProperty("prd_category")
	public void setPrd_category(String prd_category) {
		this.prd_category = prd_category;
	}

	@JsonProperty("cpe_management_type")
    public void setCpeManagementType(String cpeManagementType) {
        this.cpeManagementType = cpeManagementType;
    }

    @JsonProperty("cpe_supply_type")
    public String getCpeSupplyType() {
        return cpeSupplyType;
    }

    @JsonProperty("cpe_supply_type")
    public void setCpeSupplyType(String cpeSupplyType) {
        this.cpeSupplyType = cpeSupplyType;
    }

    @JsonProperty("topology")
    public String getTopology() {
        return topology;
    }

    @JsonProperty("topology")
    public void setTopology(String topology) {
        this.topology = topology;
    }

    @JsonProperty("latitude_final")
    public Double getLatitudeFinal() {
        return latitudeFinal;
    }

    @JsonProperty("latitude_final")
    public void setLatitudeFinal(Double latitudeFinal) {
        this.latitudeFinal = latitudeFinal;
    }

    @JsonProperty("longitude_final")
    public Double getLongitudeFinal() {
        return longitudeFinal;
    }

    @JsonProperty("longitude_final")
    public void setLongitudeFinal(Double longitudeFinal) {
        this.longitudeFinal = longitudeFinal;
    }

    @JsonProperty("feasibility_response_created_date")
    public String getFeasibilityResponseCreatedDate() {
        return feasibilityResponseCreatedDate;
    }

    @JsonProperty("feasibility_response_created_date")
    public void setFeasibilityResponseCreatedDate(String feasibilityResponseCreatedDate) {
        this.feasibilityResponseCreatedDate = feasibilityResponseCreatedDate;
    }
    
    @JsonProperty("additional_ip_flag")
    public String getAdditionalIpFlag() {
        return additionalIpFlag;
    }

    @JsonProperty("additional_ip_flag")
    public void setAdditionalIpFlag(String additionalIpFlag) {
        this.additionalIpFlag = additionalIpFlag;
    }

    @JsonProperty("ip_address_arrangement")
    public String getIpAddressArrangement() {
		return ipAddressArrangement;
	}
    @JsonProperty("ip_address_arrangement")
	public void setIpAddressArrangement(String ipAddressArrangement) {
		this.ipAddressArrangement = ipAddressArrangement;
	}
    @JsonProperty("ipv4_address_pool_size")
	public String getIpv4AddressPoolSize() {
		return ipv4AddressPoolSize;
	}
    @JsonProperty("ipv4_address_pool_size")
	public void setIpv4AddressPoolSize(String ipv4AddressPoolSize) {
		this.ipv4AddressPoolSize = ipv4AddressPoolSize;
	}
    @JsonProperty("ipv6_address_pool_size")
	public String getIpv6AddressPoolSize() {
		return ipv6AddressPoolSize;
	}
    @JsonProperty("ipv6_address_pool_size")
	public void setIpv6AddressPoolSize(String ipv6AddressPoolSize) {
		this.ipv6AddressPoolSize = ipv6AddressPoolSize;
	}

    @JsonProperty("country")
	public String getCountry() {
		return country;
	}
    @JsonProperty("country")
	public void setCountry(String country) {
		this.country = country;
	}
    @JsonProperty("siteFlag")
	public String getSiteFlag() {
		return siteFlag;
	}
    @JsonProperty("siteFlag")
	public void setSiteFlag(String siteFlag) {
		this.siteFlag = siteFlag;
	}
    
    @JsonProperty("cu_le_id")
	public String getCuLeId() {
		return cuLeId;
	}
    @JsonProperty("cu_le_id")
	public void setCuLeId(String cuLeId) {
		this.cuLeId = cuLeId;
	}

	public String getProductSolution() {
		return productSolution;
	}

	public void setProductSolution(String productSolution) {
		this.productSolution = productSolution;
	}

	/**
	 * @return the geoCode
	 */
	public String getGeoCode() {
		return geoCode;
	}

	/**
	 * @param geoCode the geoCode to set
	 */
	public void setGeoCode(String geoCode) {
		this.geoCode = geoCode;
	}


	/**
	 * @return the sendToManual
	 */
	public String getSendToManual() {
		return sendToManual;
	}

	/**
	 * @param sendToManual the sendToManual to set
	 */
	public void setSendToManual(String sendToManual) {
		this.sendToManual = sendToManual;
	}

	/**
	 * @return the geoCodeErrorDec
	 */
	public String getGeoCodeErrorDec() {
		return geoCodeErrorDec;
	}

	/**
	 * @param geoCodeErrorDec the geoCodeErrorDec to set
	 */
	public void setGeoCodeErrorDec(String geoCodeErrorDec) {
		this.geoCodeErrorDec = geoCodeErrorDec;
	}
	
	  //Macd code
    @JsonProperty("old_contract_term")
    public String getOldContractTerm() {
        return oldContractTerm;
    }

    @JsonProperty("old_contract_term")
    public void setOldContractTerm(String oldContractTerm) {
        this.oldContractTerm = oldContractTerm;
    }


    @JsonProperty("service_commissioned_date")
    public String getServiceCommisionedDate() {
        return serviceCommissionedDate;
    }

    @JsonProperty("service_commissioned_date")
    
    public void setServiceCommisionedDate(String serviceCommissionedDate) {
    	this.serviceCommissionedDate = serviceCommissionedDate;
    }

    @JsonProperty("lat_long")
    public String getLatLong() {
        return latLong;
    }

    @JsonProperty("lat_long")
    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    @JsonProperty("macd_option")
    public String getMacdOption() {
        return macdOption;
    }

    @JsonProperty("macd_option")
    public void setMacdOption(String macdOption) {
        this.macdOption = macdOption;
    }

    @JsonProperty("trigger_feasibility")
    public String getTriggerFeasibility() {
        return triggerFeasibility;
    }

    @JsonProperty("trigger_feasibility")
    public void setTriggerFeasibility(String triggerFeasibility) {
        this.triggerFeasibility = triggerFeasibility;
    }

    @JsonProperty("ll_change")
    public String getLlChange() {
        return llChange;
    }

    @JsonProperty("ll_change")
    public void setLlChange(String llChange) {
        this.llChange = llChange;
    }

    @JsonProperty("macd_service")
    public String getMacdService() {
        return macdService;
    }

    @JsonProperty("macd_service")
    public void setMacdService(String macdService) {
        this.macdService = macdService;
    }
    
    
    @JsonProperty("service_id")
    public String getServiceId() {
		return serviceId;
	}

    @JsonProperty("service_id")
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

    @JsonProperty("backup_port_requested")
	public String getBackupPortRequested() {
		return backupPortRequested;
	}
	
	@JsonProperty("backup_port_requested")
	public void setBackupPortRequested(String backupPortRequested) {
		this.backupPortRequested = backupPortRequested;
	}
	
	@JsonProperty("old_Ll_Bw")
	public String getOldLlBw() {
		return oldLlBw;
	}
	@JsonProperty("old_Ll_Bw")
	public void setOldLlBw(String oldLlBw) {
		this.oldLlBw = oldLlBw;
	}

	@JsonProperty("old_Port_Bw")
	public String getOldPortBw() {
		return oldPortBw;
	}

	@JsonProperty("old_Port_Bw")
	public void setOldPortBw(String oldPortBw) {
		this.oldPortBw = oldPortBw;
	}

	@JsonProperty("vpn_Name")
	public String getVpnName() {
		return vpnName;
	}

	@JsonProperty("vpn_Name")
	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

    @JsonProperty("partner_account_id_with_18_digit")
    public String getPartnerAccountIdWith18Digit() {
        return partnerAccountIdWith18Digit;
    }

    @JsonProperty("partner_account_id_with_18_digit")
    public void setPartnerAccountIdWith18Digit(String partnerAccountIdWith18Digit) {
        this.partnerAccountIdWith18Digit = partnerAccountIdWith18Digit;
    }

    @JsonProperty("partner_profile")
    public String getPartnerProfile() {
        return partnerProfile;
    }

    @JsonProperty("partner_profile")
    public void setPartnerProfile(String partnerProfile) {
        this.partnerProfile = partnerProfile;
    }

    @JsonProperty("quotetype_partner")
    public String getQuoteTypePartner() {
        return quoteTypePartner;
    }

    @JsonProperty("quotetype_partner")
    public void setQuoteTypePartner(String quoteTypePartner) {
        this.quoteTypePartner = quoteTypePartner;
    }

    @JsonProperty("deal_reg_flag")
    public String getDealRegFlag() {
        return dealRegFlag;
    }

    @JsonProperty("deal_reg_flag")
    public void setDealRegFlag(String dealRegFlag) {
        this.dealRegFlag = dealRegFlag;
    }
    
    @JsonProperty("access_provider")
    public String getAccessProvider() {
		return accessProvider;
	}
    
    @JsonProperty("access_provider")
	public void setAccessProvider(String accessProvider) {
		this.accessProvider = accessProvider;
	}
    @JsonProperty("cpe_intl_chassis_flag")
    public String getCpeIntlChassisFlag() {
        return cpeIntlChassisFlag;
    }
    @JsonProperty("cpe_intl_chassis_flag")
    public void setCpeIntlChassisFlag(String cpeIntlChassisFlag) {
        this.cpeIntlChassisFlag = cpeIntlChassisFlag;
    }
	public String getIsCustomer() {
		return isCustomer;
	}

	public void setIsCustomer(String isCustomer) {
		this.isCustomer = isCustomer;
	}

	public String getRespState() {
		return respState;
	}
	public void setRespState(String respState) {
		this.respState = respState;
	}

    @JsonProperty("is_demo")
    public String getIsDemo() {
        return isDemo;
    }

    @JsonProperty("is_demo")
    public void setIsDemo(String isDemo) {
        this.isDemo = isDemo;
    }

    @JsonProperty("demo_type")
    public String getDemoType() {
        return demoType;
    }

    @JsonProperty("demo_type")
    public void setDemoType(String demoType) {
        this.demoType = demoType;
    }

    @Override
    public String toString() {
        return "InputDatum{" +
                "cpeChassisChanged='" + cpeChassisChanged + '\'' +
                ", prd_category='" + prd_category + '\'' +
                ", bwMbps=" + bwMbps +
                ", customerSegment='" + customerSegment + '\'' +
                ", salesOrg='" + salesOrg + '\'' +
                ", productName='" + productName + '\'' +
                ", localLoopInterface='" + localLoopInterface + '\'' +
                ", lastMileContractTerm='" + lastMileContractTerm + '\'' +
                ", siteId='" + siteId + '\'' +
                ", prospectName='" + prospectName + '\'' +
                ", burstableBw=" + burstableBw +
                ", respCity='" + respCity + '\'' +
                ", accountIdWith18Digit='" + accountIdWith18Digit + '\'' +
                ", opportunityTerm=" + opportunityTerm +
                ", quotetypeQuote='" + quotetypeQuote + '\'' +
                ", connectionType='" + connectionType + '\'' +
                ", sumNoOfSitesUniLen=" + sumNoOfSitesUniLen +
                ", cpeVariant='" + cpeVariant + '\'' +
                ", cpeManagementType='" + cpeManagementType + '\'' +
                ", cpeSupplyType='" + cpeSupplyType + '\'' +
                ", topology='" + topology + '\'' +
                ", latitudeFinal=" + latitudeFinal +
                ", longitudeFinal=" + longitudeFinal +
                ", feasibilityResponseCreatedDate='" + feasibilityResponseCreatedDate + '\'' +
                ", additionalIpFlag='" + additionalIpFlag + '\'' +
                ", ipAddressArrangement='" + ipAddressArrangement + '\'' +
                ", ipv4AddressPoolSize='" + ipv4AddressPoolSize + '\'' +
                ", ipv6AddressPoolSize='" + ipv6AddressPoolSize + '\'' +
                ", country='" + country + '\'' +
                ", siteFlag='" + siteFlag + '\'' +
                ", cuLeId='" + cuLeId + '\'' +
                ", productSolution='" + productSolution + '\'' +
                ", geoCode='" + geoCode + '\'' +
                ", sendToManual='" + sendToManual + '\'' +
                ", geoCodeErrorDec='" + geoCodeErrorDec + '\'' +
                ", oldContractTerm='" + oldContractTerm + '\'' +
                ", serviceCommissionedDate='" + serviceCommissionedDate + '\'' +
                ", macdOption='" + macdOption + '\'' +
                ", triggerFeasibility='" + triggerFeasibility + '\'' +
                ", llChange='" + llChange + '\'' +
                ", macdService='" + macdService + '\'' +
                ", latLong='" + latLong + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", backupPortRequested='" + backupPortRequested + '\'' +
                ", oldPortBw='" + oldPortBw + '\'' +
                ", oldLlBw='" + oldLlBw + '\'' +
                ", vpnName='" + vpnName + '\'' +
                ", parallelRunDays='" + parallelRunDays + '\'' +
                ", localLoopBw=" + localLoopBw +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", bandwidth='" + bandwidth + '\'' +
                ", product='" + product + '\'' +
                ", address='" + address + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", partnerAccountIdWith18Digit='" + partnerAccountIdWith18Digit + '\'' +
                ", partnerProfile='" + partnerProfile + '\'' +
                ", quoteTypePartner='" + quoteTypePartner + '\'' +
                ", dealRegFlag='" + dealRegFlag + '\'' +
                ", accessProvider='" + accessProvider + '\'' +
                ", cpeIntlChassisFlag='" + cpeIntlChassisFlag + '\'' +
                ", nonStandard='" + nonStandard + '\'' +
                ", isCustomer='" + isCustomer + '\'' +
                ", isDemo='" + isDemo + '\'' +
                ", demoType='" + demoType + '\'' +
                ", lastMileType='" + lastMileType + '\'' +
                ", addressLineA='" + addressLineA + '\'' +
                '}';
    }

    public String getLastMileType() {
        return lastMileType;
    }

    public void setLastMileType(String lastMileType) {
        this.lastMileType = lastMileType;
    }

    public String getAddressLineA() {
        return addressLineA;
    }

    public void setAddressLineA(String addressLineA) {
        this.addressLineA = addressLineA;
    }
}
