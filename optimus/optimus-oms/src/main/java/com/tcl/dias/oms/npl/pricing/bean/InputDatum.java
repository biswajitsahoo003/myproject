
package com.tcl.dias.oms.npl.pricing.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"link_id",
	"site_id",
    "bw_mbps",
    "burstable_bw",
    "customer_segment",
    "sales_org",
    "product_name",
    "local_loop_interface",
    "last_mile_contract_term",
    "prospect_name",
    "resp_city",
    "account_id_with_18_digit",
    "opportunity_term",
    "quotetype_quote",
    "sla_varient",
    "latitude_final",
    "longitude_final",
    "feasibility_response_created_date",
    "a_or_b_end",
    "pop_selected",
    "pop_ui_id",
    "dc_selected",
    //macd attributes
    "macd_service",
	"macd_option",
	"trigger_feasibility",
	"service_id",
	"access_provider",
	"lat_long",
	"zip_code",
	"cu_le_id",
	"service_commissioned_date",
	"old_Ll_Bw",
	"old_contract_term",
	"prd_category",
	"address",
	"bandwidth",
	"ll_change",
	//nde
	"isHub",

//	CST-35 customer portal change
	"is_customer",
	
	"parallel_run_days",

	"local_loop_bw",
	"partner_account_id_with_18_digit",
	"partner_profile",
	"quotetype_partner",
	"deal_reg_flag"

})
public class InputDatum {

	@JsonProperty("link_id")
    private Integer linkId;
	@JsonProperty("site_id")
	private String siteId;    
    @JsonProperty("bw_mbps")
    private Integer bwMbps;
    @JsonProperty("burstable_bw")
    private Integer burstableBw;
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
    @JsonProperty("prospect_name")
    private String prospectName;
    @JsonProperty("resp_city")
    private String respCity;
    @JsonProperty("account_id_with_18_digit")
    private String accountIdWith18Digit;
    @JsonProperty("opportunity_term")
    private Integer opportunityTerm;
    @JsonProperty("quotetype_quote")
    private String quotetypeQuote;
    @JsonProperty("sla_varient")
    private String slaVariant;
    @JsonProperty("latitude_final")
    private Double latitudeFinal;
    @JsonProperty("longitude_final")
    private Double longitudeFinal;
    @JsonProperty("feasibility_response_created_date")
    private String feasibilityResponseCreatedDate;
    @JsonProperty("a_or_b_end")
    private String aOrBEnd;
    @JsonProperty("pop_selected")
    private String popSelected;
    @JsonProperty("pop_ui_id")
    private String popUiId;
    @JsonProperty("dc_selected")
    private String dcSelected;
    //macd attributes
    @JsonProperty("macd_option")
	private String macdOption;
	@JsonProperty("trigger_feasibility")
	private String triggerFeasibility;
	@JsonProperty("macd_service")
	private String macdService;
	@JsonProperty("service_id")
	private String serviceId;
	@JsonProperty("access_provider")
	private String accessProvider;
	@JsonProperty("lat_long")
	private String latLong;
	@JsonProperty("zip_code")
	private String zipCode;
	@JsonProperty("cu_le_id")
	private String cuLeId;
	@JsonProperty("service_commissioned_date")
	private String serviceCommissionedDate;
	@JsonProperty("old_Ll_Bw")
	private String oldLlBw;
	@JsonProperty("old_contract_term")
	private String oldContractTerm;
	@JsonProperty("prd_category")
	private String prdCategory;
	@JsonProperty("address")
	private String address;
	@JsonProperty("bandwidth")
	private String bandwidth;
	@JsonProperty("ll_change")
	private String llChange;
	@JsonProperty("old_Ll_Bw_ref")
	private String oldLlBwRef;

	@JsonProperty("isHub")
	private Integer isHub;
	
	
	

	@JsonProperty("resp_state")
	private String respState;


	@JsonProperty("is_customer")
	private String isCustomer;
	
	
	@JsonProperty("parallel_run_days")
	private String parallelRunDays;

	@JsonProperty("local_loop_bw")
	private Double localLoopBw;

	@JsonProperty("partner_account_id_with_18_digit")
	private String partnerAccountIdWith18Digit;

	@JsonProperty("partner_profile")
	private String partnerProfile;

	@JsonProperty("quotetype_partner")
	private String quoteTypePartner;

	@JsonProperty("deal_reg_flag")
	private String dealRegFlag;

	@JsonProperty("Last_mile_type")
	private String lastMileType;

	@JsonProperty("isHub")
    public Integer getIsHub() {
		return isHub;
	}
	@JsonProperty("isHub")
	public void setIsHub(Integer isHub) {
		this.isHub = isHub;
	}

	public Integer getBwMbps() {
        return bwMbps;
    }

    @JsonProperty("bw_mbps")
    public void setBwMbps(Integer bwMbps) {
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
    public Integer getBurstableBw() {
        return burstableBw;
    }

    @JsonProperty("burstable_bw")
    public void setBurstableBw(Integer burstableBw) {
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

    public Integer getLinkId() {
		return linkId;
	}

	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}

	public String getSlaVariant() {
		return slaVariant;
	}

	public void setSlaVariant(String slaVariant) {
		this.slaVariant = slaVariant;
	}

	public String getaOrBEnd() {
		return aOrBEnd;
	}

	public void setaOrBEnd(String aOrBEnd) {
		this.aOrBEnd = aOrBEnd;
	}

	public String getPopSelected() {
		return popSelected;
	}

	public void setPopSelected(String popSelected) {
		this.popSelected = popSelected;
	}

	public String getPopUiId() {
		return popUiId;
	}

	public void setPopUiId(String popUiId) {
		this.popUiId = popUiId;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this).append("bwMbps", bwMbps).append("customerSegment", customerSegment).append("salesOrg", salesOrg).append("productName", productName).append("localLoopInterface", localLoopInterface).append("lastMileContractTerm", lastMileContractTerm).append("siteId", siteId).append("prospectName", prospectName).append("burstableBw", burstableBw).append("respCity", respCity).append("accountIdWith18Digit", accountIdWith18Digit).append("opportunityTerm", opportunityTerm).append("quotetypeQuote", quotetypeQuote).toString();//.append("connectionType", connectionType).append("sumNoOfSitesUniLen", sumNoOfSitesUniLen).append("cpeVariant", cpeVariant).append("cpeManagementType", cpeManagementType).append("cpeSupplyType", cpeSupplyType).append("topology", topology).append("latitudeFinal", latitudeFinal).append("longitudeFinal", longitudeFinal).append("feasibilityResponseCreatedDate", feasibilityResponseCreatedDate).append("additionalIpFlag", additionalIpFlag).append("ipAddressArrangement", ipAddressArrangement).append("ipv4AddressPoolSize", ipv4AddressPoolSize).append("ipv6AddressPoolSize", ipv6AddressPoolSize).toString();
    }

	public String getDcSelected() {
		return dcSelected;
	}

	public void setDcSelected(String dcSelected) {
		this.dcSelected = dcSelected;
	}
	//macd attributes

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
	@JsonProperty("access_provider")
	public String getAccessProvider() {
		return accessProvider;
	}
	@JsonProperty("access_provider")
	public void setAccessProvider(String accessProvider) {
		this.accessProvider = accessProvider;
	}
	@JsonProperty("lat_long")
	public String getLatLong() {
		return latLong;
	}
	@JsonProperty("lat_long")
	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}
	@JsonProperty("zip_code")
	public String getZipCode() {
		return zipCode;
	}
	@JsonProperty("zip_code")
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	@JsonProperty("cu_le_id")
	public String getCuLeId() {
		return cuLeId;
	}
	@JsonProperty("cu_le_id")
	public void setCuLeId(String cuLeId) {
		this.cuLeId = cuLeId;
	}
	@JsonProperty("service_commissioned_date")
	public String getServiceCommissionedDate() {
		return serviceCommissionedDate;
	}
	@JsonProperty("service_commissioned_date")
	public void setServiceCommissionedDate(String serviceCommissionedDate) {
		this.serviceCommissionedDate = serviceCommissionedDate;
	}
	@JsonProperty("old_Ll_Bw")
	public String getOldLlBw() {
		return oldLlBw;
	}
	@JsonProperty("old_Ll_Bw")
	public void setOldLlBw(String oldLlBw) {
		this.oldLlBw = oldLlBw;
	}
	@JsonProperty("old_contract_term")
	public String getOldContractTerm() {
		return oldContractTerm;
	}
	@JsonProperty("old_contract_term")
	public void setOldContractTerm(String oldContractTerm) {
		this.oldContractTerm = oldContractTerm;
	}
	@JsonProperty("prd_category")
	public String getPrdCategory() {
		return prdCategory;
	}
	@JsonProperty("prd_category")
	public void setPrdCategory(String prdCategory) {
		this.prdCategory = prdCategory;
	}
	@JsonProperty("address")
	public String getAddress() {
		return address;
	}
	@JsonProperty("address")
	public void setAddress(String address) {
		this.address = address;
	}
	@JsonProperty("bandwidth")
	public String getBandwidth() {
		return bandwidth;
	}
	@JsonProperty("bandwidth")
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	@JsonProperty("ll_change")
	public String getLlChange() {
		return llChange;
	}
	@JsonProperty("ll_change")
	public void setLlChange(String llChange) {
		this.llChange = llChange;
	}
	public String getRespState() {
		return respState;
	}
	public void setRespState(String respState) {
		this.respState = respState;
	}
	public String getIsCustomer() {
		return isCustomer;
	}

	public void setIsCustomer(String isCustomer) {
		this.isCustomer = isCustomer;
	}
	
	@JsonProperty("parallel_run_days")
	public String getParallelRunDays() {
		return parallelRunDays;
	}

	@JsonProperty("parallel_run_days")
	public void setParallelRunDays(String parallelRunDays) {
		this.parallelRunDays = parallelRunDays;
	}
	public String getOldLlBwRef() {
		return oldLlBwRef;
	}
	public void setOldLlBwRef(String oldLlBwRef) {
		this.oldLlBwRef = oldLlBwRef;
	}

	@JsonProperty("local_loop_bw")
	public Double getLocalLoopBw() {
		return localLoopBw;
	}

	@JsonProperty("local_loop_bw")
	public void setLocalLoopBw(Double localLoopBw) {
		this.localLoopBw = localLoopBw;
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


	public String getLastMileType() {
		return lastMileType;
	}

	public void setLastMileType(String lastMileType) {
		this.lastMileType = lastMileType;
	}
}
