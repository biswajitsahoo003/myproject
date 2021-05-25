package com.tcl.dias.oms.izopc.pricing.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Construct the pricing input for pricing model
 * 
 * @author PAULRAJ SUNDAR
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "site_id", "latitude_final", "longitude_final", "prospect_name", "bw_mbps", "burstable_bw",
		"resp_city", "customer_segment", "sales_org", "product_name", "feasibility_response_created_date",
		"local_loop_interface", "last_mile_contract_term", "account_id_with_18_digit", "opportunity_term",
		"quotetype_quote", "connection_type", "sum_no_of_sites_uni_len", "cpe_variant", "cpe_management_type",
		"cpe_supply_type", "topology", "sum_onnet_flag", "sum_offnet_flag", "lm_arc_bw_onwl", "lm_nrc_bw_onwl",
		"lm_nrc_mux_onwl", "lm_nrc_inbldg_onwl", "lm_nrc_ospcapex_onwl", "lm_nrc_nerental_onwl", "lm_arc_bw_prov_ofrf",
		"lm_nrc_bw_prov_ofrf", "lm_nrc_mast_ofrf", "lm_arc_bw_onrf", "lm_nrc_bw_onrf", "lm_nrc_mast_onrf",
		"Orch_Connection", "Orch_LM_Type", "additional_ip_flag", "ip_address_arrangement", "ipv4_address_pool_size",
		"ipv6_address_pool_size",
		//Introduced for NPL 
		"Last_Modified_Date","BW_mbps_upd","DistanceBetweenPOPs","sum_model_name","model_name_transform","product_flavor_transform","country",
	    "siteFlag",
	    "backup_port_requested","cu_le_id",
		"BW_mbps_upd","trigger_feasibility","macd_option","service_commissioned_date",
		"old_contract_term","lat_long","ll_change","macd_service","old_Ll_Bw","old_Port_Bw","vpn_Name","parallel_run_days",
		"cpe_chassis_changed","local_loop_bw","cpe_intl_chassis_flag","change_lastmile_provider","Orch_Category","prd_category"
 })
public class PricingInputDatum {

	@JsonProperty("site_id")
	private String siteId;
	@JsonProperty("latitude_final")
	private String latitudeFinal;
	@JsonProperty("longitude_final")
	private String longitudeFinal;
	@JsonProperty("prospect_name")
	private String prospectName;
	@JsonProperty("bw_mbps")
	private String bwMbps;
	@JsonProperty("burstable_bw")
	private String burstableBw;
	@JsonProperty("resp_city")
	private String respCity;
	@JsonProperty("customer_segment")
	private String customerSegment;
	@JsonProperty("sales_org")
	private String salesOrg;
	@JsonProperty("product_name")
	private String productName;
	@JsonProperty("feasibility_response_created_date")
	private String feasibilityResponseCreatedDate;
	@JsonProperty("local_loop_interface")
	private String localLoopInterface;
	@JsonProperty("last_mile_contract_term")
	private String lastMileContractTerm;
	@JsonProperty("account_id_with_18_digit")
	private String accountIdWith18Digit;
	@JsonProperty("opportunity_term")
	private String opportunityTerm;
	@JsonProperty("quotetype_quote")
	private String quotetypeQuote;
	@JsonProperty("connection_type")
	private String connectionType;
	@JsonProperty("sum_no_of_sites_uni_len")
	private String sumNoOfSitesUniLen;
	@JsonProperty("cpe_variant")
	private String cpeVariant;
	@JsonProperty("cpe_management_type")
	private String cpeManagementType;
	@JsonProperty("cpe_supply_type")
	private String cpeSupplyType;
	@JsonProperty("topology")
	private String topology;
	@JsonProperty("sum_onnet_flag")
	private String sumOnnetFlag;
	@JsonProperty("sum_offnet_flag")
	private String sumOffnetFlag;
	@JsonProperty("lm_arc_bw_onwl")
	private String lmArcBwOnwl;
	@JsonProperty("lm_nrc_bw_onwl")
	private String lmNrcBwOnwl;
	@JsonProperty("lm_nrc_mux_onwl")
	private String lmNrcMuxOnwl;
	@JsonProperty("lm_nrc_inbldg_onwl")
	private String lmNrcInbldgOnwl;
	@JsonProperty("lm_nrc_ospcapex_onwl")
	private String lmNrcOspcapexOnwl;
	@JsonProperty("lm_nrc_nerental_onwl")
	private String lmNrcNerentalOnwl;
	@JsonProperty("lm_arc_bw_prov_ofrf")
	private String lmArcBwProvOfrf;
	@JsonProperty("lm_nrc_bw_prov_ofrf")
	private String lmNrcBwProvOfrf;
	@JsonProperty("lm_nrc_mast_ofrf")
	private String lmNrcMastOfrf;
	@JsonProperty("lm_arc_bw_onrf")
	private String lmArcBwOnrf;
	@JsonProperty("lm_nrc_bw_onrf")
	private String lmNrcBwOnrf;
	@JsonProperty("lm_nrc_mast_onrf")
	private String lmNrcMastOnrf;
	@JsonProperty("Orch_Connection")
	private String orchConnection;
	@JsonProperty("Orch_LM_Type")
	private String orchLMType;
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
	
	// NPL related attributes - start
	@JsonProperty("Last_Modified_Date")
	private String lastModifiedDate;

	//MACD Attributes
	@JsonProperty("BW_mbps_upd")
	private String BWMbpsUpd;

	@JsonProperty("trigger_feasibility")
	private String triggerFeasibility;

	@JsonProperty("macd_option")
	private String macdOption;

	@JsonProperty("service_commissioned_date")
	private String serviceCommissionedDate;

	@JsonProperty("old_contract_term")
	private String oldContractTerm;

	@JsonProperty("lat_long")
	private String latLong;

	@JsonProperty("ll_change")
	private String llChange;

	@JsonProperty("macd_service")
	private String macdService;

	@JsonProperty("old_Ll_Bw")
	private String oldLlBw;

	@JsonProperty("old_Port_Bw")
	private String oldPortBw;

	@JsonProperty("vpn_Name")
	private String vpnName;

	@JsonProperty("parallel_run_days")
	private String parallelRunDays;

	@JsonProperty("cpe_chassis_changed")
	private String cpeChassisChanged;

	@JsonProperty("local_loop_bw")
	private String localLoopBw;

	@JsonProperty("cpe_intl_chassis_flag")
	private String cpeIntlChassisFlag;

	@JsonProperty("change_lastmile_provider")
	private String changeLastmileProvider;

	@JsonProperty("Orch_Category")
	private String orchCategory;

	@JsonProperty("prd_category")
	private String prd_category;
	
		@JsonProperty("DistanceBetweenPOPs")
		private String distanceBetweenPOPs;
		
		@JsonProperty("sum_model_name")
		private String sumModelName;
		
		@JsonProperty("model_name_transform")
		private String modelNameTransform;

		@JsonProperty("product_flavor_transform")
		private String productFlavorTransform;
		@JsonProperty("datacentre_location")
		private String dataCentreLoc;
	    @JsonProperty("cloud_provider")
	   	private String cloudProvider;
	    @JsonProperty("service_id")
	   	private String serviceId;

	    
		@JsonProperty("DistanceBetweenPOPs")
		public String getDistanceBetweenPOPs() {
			return distanceBetweenPOPs;
		}
		
		@JsonProperty("DistanceBetweenPOPs")
		public void setDistanceBetweenPOPs(String distanceBetweenPOPs) {
			this.distanceBetweenPOPs = distanceBetweenPOPs;
		}
		
		@JsonProperty("sum_model_name")
		public String getSumModelName() {
			return sumModelName;
		}

		@JsonProperty("sum_model_name")
		public void setSumModelName(String sumModelName) {
			this.sumModelName = sumModelName;
		}

		@JsonProperty("model_name_transform")
		public String getModelNameTransform() {
			return modelNameTransform;
		}

		@JsonProperty("model_name_transform")
		public void setModelNameTransform(String modelNameTransform) {
			this.modelNameTransform = modelNameTransform;
		}

		@JsonProperty("product_flavor_transform")
		public String getProductFlavorTransform() {
			return productFlavorTransform;
		}

		@JsonProperty("product_flavor_transform")
		public void setProductFlavorTransform(String productFlavorTransform) {
			this.productFlavorTransform = productFlavorTransform;
		}
		
		@JsonProperty("Last_Modified_Date")
		public String getLastModifiedDate() {
			return lastModifiedDate;
		}

		@JsonProperty("Last_Modified_Date")
		public void setLastModifiedDate(String lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}

		@JsonProperty("BW_mbps_upd")
		public String getBWMbpsUpd() {
			return BWMbpsUpd;
		}

		@JsonProperty("BW_mbps_upd")
		public void setBWMbpsUpd(String bWMbpsUpd) {
			BWMbpsUpd = bWMbpsUpd;
		}
		
		// NPL related attributes - end

	@JsonProperty("site_id")
	public String getSiteId() {
		return siteId;
	}

	@JsonProperty("site_id")
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	@JsonProperty("latitude_final")
	public String getLatitudeFinal() {
		return latitudeFinal;
	}

	@JsonProperty("latitude_final")
	public void setLatitudeFinal(String latitudeFinal) {
		this.latitudeFinal = latitudeFinal;
	}

	@JsonProperty("longitude_final")
	public String getLongitudeFinal() {
		return longitudeFinal;
	}

	@JsonProperty("longitude_final")
	public void setLongitudeFinal(String longitudeFinal) {
		this.longitudeFinal = longitudeFinal;
	}

	@JsonProperty("prospect_name")
	public String getProspectName() {
		return prospectName;
	}

	@JsonProperty("prospect_name")
	public void setProspectName(String prospectName) {
		this.prospectName = prospectName;
	}

	@JsonProperty("bw_mbps")
	public String getBwMbps() {
		return bwMbps;
	}

	@JsonProperty("bw_mbps")
	public void setBwMbps(String bwMbps) {
		this.bwMbps = bwMbps;
	}

	@JsonProperty("burstable_bw")
	public String getBurstableBw() {
		return burstableBw;
	}

	@JsonProperty("burstable_bw")
	public void setBurstableBw(String burstableBw) {
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

	@JsonProperty("feasibility_response_created_date")
	public String getFeasibilityResponseCreatedDate() {
		return feasibilityResponseCreatedDate;
	}

	@JsonProperty("feasibility_response_created_date")
	public void setFeasibilityResponseCreatedDate(String feasibilityResponseCreatedDate) {
		this.feasibilityResponseCreatedDate = feasibilityResponseCreatedDate;
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

	@JsonProperty("account_id_with_18_digit")
	public String getAccountIdWith18Digit() {
		return accountIdWith18Digit;
	}

	@JsonProperty("account_id_with_18_digit")
	public void setAccountIdWith18Digit(String accountIdWith18Digit) {
		this.accountIdWith18Digit = accountIdWith18Digit;
	}

	@JsonProperty("opportunity_term")
	public String getOpportunityTerm() {
		return opportunityTerm;
	}

	@JsonProperty("opportunity_term")
	public void setOpportunityTerm(String opportunityTerm) {
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
	public String getSumNoOfSitesUniLen() {
		return sumNoOfSitesUniLen;
	}

	@JsonProperty("sum_no_of_sites_uni_len")
	public void setSumNoOfSitesUniLen(String sumNoOfSitesUniLen) {
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

	@JsonProperty("sum_onnet_flag")
	public String getSumOnnetFlag() {
		return sumOnnetFlag;
	}

	@JsonProperty("sum_onnet_flag")
	public void setSumOnnetFlag(String sumOnnetFlag) {
		this.sumOnnetFlag = sumOnnetFlag;
	}

	@JsonProperty("sum_offnet_flag")
	public String getSumOffnetFlag() {
		return sumOffnetFlag;
	}

	@JsonProperty("sum_offnet_flag")
	public void setSumOffnetFlag(String sumOffnetFlag) {
		this.sumOffnetFlag = sumOffnetFlag;
	}

	@JsonProperty("lm_arc_bw_onwl")
	public String getLmArcBwOnwl() {
		return lmArcBwOnwl;
	}

	@JsonProperty("lm_arc_bw_onwl")
	public void setLmArcBwOnwl(String lmArcBwOnwl) {
		this.lmArcBwOnwl = lmArcBwOnwl;
	}

	@JsonProperty("lm_nrc_bw_onwl")
	public String getLmNrcBwOnwl() {
		return lmNrcBwOnwl;
	}

	@JsonProperty("lm_nrc_bw_onwl")
	public void setLmNrcBwOnwl(String lmNrcBwOnwl) {
		this.lmNrcBwOnwl = lmNrcBwOnwl;
	}

	@JsonProperty("lm_nrc_mux_onwl")
	public String getLmNrcMuxOnwl() {
		return lmNrcMuxOnwl;
	}

	@JsonProperty("lm_nrc_mux_onwl")
	public void setLmNrcMuxOnwl(String lmNrcMuxOnwl) {
		this.lmNrcMuxOnwl = lmNrcMuxOnwl;
	}

	@JsonProperty("lm_nrc_inbldg_onwl")
	public String getLmNrcInbldgOnwl() {
		return lmNrcInbldgOnwl;
	}

	@JsonProperty("lm_nrc_inbldg_onwl")
	public void setLmNrcInbldgOnwl(String lmNrcInbldgOnwl) {
		this.lmNrcInbldgOnwl = lmNrcInbldgOnwl;
	}

	@JsonProperty("lm_nrc_ospcapex_onwl")
	public String getLmNrcOspcapexOnwl() {
		return lmNrcOspcapexOnwl;
	}

	@JsonProperty("lm_nrc_ospcapex_onwl")
	public void setLmNrcOspcapexOnwl(String lmNrcOspcapexOnwl) {
		this.lmNrcOspcapexOnwl = lmNrcOspcapexOnwl;
	}

	@JsonProperty("lm_nrc_nerental_onwl")
	public String getLmNrcNerentalOnwl() {
		return lmNrcNerentalOnwl;
	}

	@JsonProperty("lm_nrc_nerental_onwl")
	public void setLmNrcNerentalOnwl(String lmNrcNerentalOnwl) {
		this.lmNrcNerentalOnwl = lmNrcNerentalOnwl;
	}

	@JsonProperty("lm_arc_bw_prov_ofrf")
	public String getLmArcBwProvOfrf() {
		return lmArcBwProvOfrf;
	}

	@JsonProperty("lm_arc_bw_prov_ofrf")
	public void setLmArcBwProvOfrf(String lmArcBwProvOfrf) {
		this.lmArcBwProvOfrf = lmArcBwProvOfrf;
	}

	@JsonProperty("lm_nrc_bw_prov_ofrf")
	public String getLmNrcBwProvOfrf() {
		return lmNrcBwProvOfrf;
	}

	@JsonProperty("lm_nrc_bw_prov_ofrf")
	public void setLmNrcBwProvOfrf(String lmNrcBwProvOfrf) {
		this.lmNrcBwProvOfrf = lmNrcBwProvOfrf;
	}

	@JsonProperty("lm_nrc_mast_ofrf")
	public String getLmNrcMastOfrf() {
		return lmNrcMastOfrf;
	}

	@JsonProperty("lm_nrc_mast_ofrf")
	public void setLmNrcMastOfrf(String lmNrcMastOfrf) {
		this.lmNrcMastOfrf = lmNrcMastOfrf;
	}

	@JsonProperty("lm_arc_bw_onrf")
	public String getLmArcBwOnrf() {
		return lmArcBwOnrf;
	}

	@JsonProperty("lm_arc_bw_onrf")
	public void setLmArcBwOnrf(String lmArcBwOnrf) {
		this.lmArcBwOnrf = lmArcBwOnrf;
	}

	@JsonProperty("lm_nrc_bw_onrf")
	public String getLmNrcBwOnrf() {
		return lmNrcBwOnrf;
	}

	@JsonProperty("lm_nrc_bw_onrf")
	public void setLmNrcBwOnrf(String lmNrcBwOnrf) {
		this.lmNrcBwOnrf = lmNrcBwOnrf;
	}

	@JsonProperty("lm_nrc_mast_onrf")
	public String getLmNrcMastOnrf() {
		return lmNrcMastOnrf;
	}

	@JsonProperty("lm_nrc_mast_onrf")
	public void setLmNrcMastOnrf(String lmNrcMastOnrf) {
		this.lmNrcMastOnrf = lmNrcMastOnrf;
	}

	@JsonProperty("Orch_Connection")
	public String getOrchConnection() {
		return orchConnection;
	}

	@JsonProperty("Orch_Connection")
	public void setOrchConnection(String orchConnection) {
		this.orchConnection = orchConnection;
	}

	@JsonProperty("Orch_LM_Type")
	public String getOrchLMType() {
		return orchLMType;
	}

	@JsonProperty("Orch_LM_Type")
	public void setOrchLMType(String orchLMType) {
		this.orchLMType = orchLMType;
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
		return new ToStringBuilder(this).append("siteId", siteId).append("latitudeFinal", latitudeFinal)
				.append("longitudeFinal", longitudeFinal).append("prospectName", prospectName).append("bwMbps", bwMbps)
				.append("burstableBw", burstableBw).append("respCity", respCity)
				.append("customerSegment", customerSegment).append("salesOrg", salesOrg)
				.append("productName", productName)
				.append("feasibilityResponseCreatedDate", feasibilityResponseCreatedDate)
				.append("localLoopInterface", localLoopInterface).append("lastMileContractTerm", lastMileContractTerm)
				.append("accountIdWith18Digit", accountIdWith18Digit).append("opportunityTerm", opportunityTerm)
				.append("quotetypeQuote", quotetypeQuote).append("connectionType", connectionType)
				.append("sumNoOfSitesUniLen", sumNoOfSitesUniLen).append("cpeVariant", cpeVariant)
				.append("cpeManagementType", cpeManagementType).append("cpeSupplyType", cpeSupplyType)
				.append("topology", topology).append("sumOnnetFlag", sumOnnetFlag)
				.append("sumOffnetFlag", sumOffnetFlag).append("lmArcBwOnwl", lmArcBwOnwl)
				.append("lmNrcBwOnwl", lmNrcBwOnwl).append("lmNrcMuxOnwl", lmNrcMuxOnwl)
				.append("lmNrcInbldgOnwl", lmNrcInbldgOnwl).append("lmNrcOspcapexOnwl", lmNrcOspcapexOnwl)
				.append("lmNrcNerentalOnwl", lmNrcNerentalOnwl).append("lmArcBwProvOfrf", lmArcBwProvOfrf)
				.append("lmNrcBwProvOfrf", lmNrcBwProvOfrf).append("lmNrcMastOfrf", lmNrcMastOfrf)
				.append("lmArcBwOnrf", lmArcBwOnrf).append("lmNrcBwOnrf", lmNrcBwOnrf)
				.append("lmNrcMastOnrf", lmNrcMastOnrf).append("orchConnection", orchConnection)
				.append("orchLMType", orchLMType).append("additionalIpFlag", additionalIpFlag)
				.append("ipAddressArrangement", ipAddressArrangement).append("ipv4AddressPoolSize", ipv4AddressPoolSize)
				.append("ipv6AddressPoolSize", ipv6AddressPoolSize).toString();
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

	public String getTriggerFeasibility() {
		return triggerFeasibility;
	}

	public void setTriggerFeasibility(String triggerFeasibility) {
		this.triggerFeasibility = triggerFeasibility;
	}

	public String getMacdOption() {
		return macdOption;
	}

	public void setMacdOption(String macdOption) {
		this.macdOption = macdOption;
	}

	public String getMacdService() {
		return macdService;
	}

	public void setMacdService(String macdService) {
		this.macdService = macdService;
	}

	public String getOldLlBw() {
		return oldLlBw;
	}

	public void setOldLlBw(String oldLlBw) {
		this.oldLlBw = oldLlBw;
	}

	public String getOldPortBw() {
		return oldPortBw;
	}

	public void setOldPortBw(String oldPortBw) {
		this.oldPortBw = oldPortBw;
	}

	public String getVpnName() {
		return vpnName;
	}

	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}

	public String getParallelRunDays() {
		return parallelRunDays;
	}

	public void setParallelRunDays(String parallelRunDays) {
		this.parallelRunDays = parallelRunDays;
	}

	public String getCpeChassisChanged() {
		return cpeChassisChanged;
	}

	public void setCpeChassisChanged(String cpeChassisChanged) {
		this.cpeChassisChanged = cpeChassisChanged;
	}

	public String getLocalLoopBw() {
		return localLoopBw;
	}

	public void setLocalLoopBw(String localLoopBw) {
		this.localLoopBw = localLoopBw;
	}

	public String getCpeIntlChassisFlag() {
		return cpeIntlChassisFlag;
	}

	public void setCpeIntlChassisFlag(String cpeIntlChassisFlag) {
		this.cpeIntlChassisFlag = cpeIntlChassisFlag;
	}

	public String getChangeLastmileProvider() {
		return changeLastmileProvider;
	}

	public void setChangeLastmileProvider(String changeLastmileProvider) {
		this.changeLastmileProvider = changeLastmileProvider;
	}

	public String getServiceCommissionedDate() {
		return serviceCommissionedDate;
	}

	public void setServiceCommissionedDate(String serviceCommissionedDate) {
		this.serviceCommissionedDate = serviceCommissionedDate;
	}

	public String getOrchCategory() {
		return orchCategory;
	}

	public void setOrchCategory(String orchCategory) {
		this.orchCategory = orchCategory;
	}

	public String getOldContractTerm() {
		return oldContractTerm;
	}

	public void setOldContractTerm(String oldContractTerm) {
		this.oldContractTerm = oldContractTerm;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getLlChange() {
		return llChange;
	}

	public void setLlChange(String llChange) {
		this.llChange = llChange;
	}

	public String getPrd_category() {
		return prd_category;
	}

	public void setPrd_category(String prd_category) {
		this.prd_category = prd_category;
	}
}
