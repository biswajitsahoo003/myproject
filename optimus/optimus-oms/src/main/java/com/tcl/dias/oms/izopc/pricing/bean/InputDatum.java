package com.tcl.dias.oms.izopc.pricing.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * This class is used process input signatures from OMS
 * 
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
    "country",
    "siteFlag",
    "backup_port_requested",
    "cu_le_id",
    "datacentre_location",
    "cloud_provider",
    "service_id",
    "domestic_vpn"
})
public class InputDatum {

    @JsonProperty("bw_mbps")
    private Double bwMbps;
    @JsonProperty("prd_category")
	private String prdCategory; 
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
    @JsonProperty("backup_port_requested")
    private String backupPortRequested;
    @JsonProperty("cu_le_id")
	private String cuLeId;
    @JsonProperty("datacentre_location")
	private String dataCentreLoc;
    @JsonProperty("cloud_provider")
   	private String cloudProvider;
    @JsonProperty("service_id")
   	private String serviceId;
    @JsonProperty("domestic_vpn")
   	private String isDomesticVpn;
    @JsonProperty("access_provider")
    private String accessProvider;
    @JsonProperty("Last_mile_type")
    private String lastMileType;
    @JsonProperty("old_contract_term")
    private String oldContractTerm;
    @JsonProperty("service_commissioned_date")
    private String serviceCommissionedDate;
    @JsonProperty("macd_option")
    private String macdOption;
    @JsonProperty("lat_long")
    private String latLong;
	@JsonProperty("cpe_chassis_changed")
	private String cpeChassisChanged;
	@JsonProperty("vpn_Name")
	private String vpnName;
	@JsonProperty("old_Port_Bw")
	private String oldPortBw;
	@JsonProperty("old_Ll_Bw")
	private String oldLlBw;
    @JsonProperty("macd_service")
    private String macdService;
    @JsonProperty("trigger_feasibility")
    private String triggerFeasibility;
	@JsonProperty("parallel_run_days")
	private String parallelRunDays;
	@JsonProperty("ll_change")
	private String llChange;
    
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

    
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("bwMbps", bwMbps).append("customerSegment", customerSegment).append("salesOrg", salesOrg).append("productName", productName).append("localLoopInterface", localLoopInterface).append("lastMileContractTerm", lastMileContractTerm).append("siteId", siteId).append("prospectName", prospectName).append("burstableBw", burstableBw).append("respCity", respCity).append("accountIdWith18Digit", accountIdWith18Digit).append("opportunityTerm", opportunityTerm).append("quotetypeQuote", quotetypeQuote).append("connectionType", connectionType).append("sumNoOfSitesUniLen", sumNoOfSitesUniLen).append("cpeVariant", cpeVariant).append("cpeManagementType", cpeManagementType).append("cpeSupplyType", cpeSupplyType).append("topology", topology).append("latitudeFinal", latitudeFinal).append("longitudeFinal", longitudeFinal).append("feasibilityResponseCreatedDate", feasibilityResponseCreatedDate).append("additionalIpFlag", additionalIpFlag).append("ipAddressArrangement", ipAddressArrangement).append("ipv4AddressPoolSize", ipv4AddressPoolSize).append("ipv6AddressPoolSize", ipv6AddressPoolSize).toString();
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
    @JsonProperty("backup_port_requested")
	public String getBackupPortRequested() {
		return backupPortRequested;
	}
    @JsonProperty("backup_port_requested")
	public void setBackupPortRequested(String backupPortRequested) {
		this.backupPortRequested = backupPortRequested;
	}
    @JsonProperty("cu_le_id")
	public String getCuLeId() {
		return cuLeId;
	}
    @JsonProperty("cu_le_id")
	public void setCuLeId(String cuLeId) {
		this.cuLeId = cuLeId;
	}

	public String getDataCentreLoc() {
		return dataCentreLoc;
	}

	public void setDataCentreLoc(String dataCentreLoc) {
		this.dataCentreLoc = dataCentreLoc;
	}

	public String getCloudProvider() {
		return cloudProvider;
	}

	public void setCloudProvider(String cloudProvider) {
		this.cloudProvider = cloudProvider;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return the isDomesticVpn
	 */
	public String getIsDomesticVpn() {
		return isDomesticVpn;
	}

	/**
	 * @param isDomesticVpn the isDomesticVpn to set
	 */
	public void setIsDomesticVpn(String isDomesticVpn) {
		this.isDomesticVpn = isDomesticVpn;
	}

	public String getPrdCategory() {
		return prdCategory;
	}

	public void setPrdCategory(String prdCategory) {
		this.prdCategory = prdCategory;
	}

	public String getAccessProvider() {
		return accessProvider;
	}

	public void setAccessProvider(String accessProvider) {
		this.accessProvider = accessProvider;
	}

	public String getLastMileType() {
		return lastMileType;
	}

	public void setLastMileType(String lastMileType) {
		this.lastMileType = lastMileType;
	}

	public String getOldContractTerm() {
		return oldContractTerm;
	}

	public void setOldContractTerm(String oldContractTerm) {
		this.oldContractTerm = oldContractTerm;
	}

	public String getServiceCommissionedDate() {
		return serviceCommissionedDate;
	}

	public void setServiceCommissionedDate(String serviceCommissionedDate) {
		this.serviceCommissionedDate = serviceCommissionedDate;
	}

	public String getMacdOption() {
		return macdOption;
	}

	public void setMacdOption(String macdOption) {
		this.macdOption = macdOption;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getCpeChassisChanged() {
		return cpeChassisChanged;
	}

	public void setCpeChassisChanged(String cpeChassisChanged) {
		this.cpeChassisChanged = cpeChassisChanged;
	}

	public String getVpnName() {
		return vpnName;
	}

	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}

	public String getOldPortBw() {
		return oldPortBw;
	}

	public void setOldPortBw(String oldPortBw) {
		this.oldPortBw = oldPortBw;
	}

	public String getOldLlBw() {
		return oldLlBw;
	}

	public void setOldLlBw(String oldLlBw) {
		this.oldLlBw = oldLlBw;
	}

	public String getMacdService() {
		return macdService;
	}

	public void setMacdService(String macdService) {
		this.macdService = macdService;
	}

	public String getTriggerFeasibility() {
		return triggerFeasibility;
	}

	public void setTriggerFeasibility(String triggerFeasibility) {
		this.triggerFeasibility = triggerFeasibility;
	}

	public String getParallelRunDays() {
		return parallelRunDays;
	}

	public void setParallelRunDays(String parallelRunDays) {
		this.parallelRunDays = parallelRunDays;
	}

	public String getLlChange() {
		return llChange;
	}

	public void setLlChange(String llChange) {
		this.llChange = llChange;
	}
	
    
	
}
