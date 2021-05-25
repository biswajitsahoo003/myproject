package com.tcl.dias.oms.pricing.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "Bucket_Adjustment_Type", "burstable_bw", "bw_mbps", "contract_expiry_date",
		"country", "cpe_management_type", "cpe_rental_mrc", "cpe_supply_type", "cpe_variant",
		"customer_segment", "error_flag", "error_msg", "etc", "old_contract_term", "payment_currency",
		"prev_ll_mrc", "prev_port_mrc", "product_name", "prospect_name", "quotetype_quote", "resp_city",
		"sales_org", "service_commissioned_date", "service_id", "siteFlag", "site_id",
		"termination_date", "total_mrc", "version"
		})
public class ETCResult {

	@JsonProperty("Bucket_Adjustment_Type")
	private String bucketAdjustmentType;
	@JsonProperty("burstable_bW")
	private String burstableBW;
    @JsonProperty("bw_mbps")
    private String bwMbps;
	@JsonProperty("contract_expiry_date")
	private String contractExpiryDate;    
    @JsonProperty("country")
    private String country;
	@JsonProperty("cpe_management_type")
	private String cpeManagementType;
	@JsonProperty("cpe_rental_mrc")
	private String cpeRentalMrc;
	@JsonProperty("cpe_supply_type")
	private String cpeSupplyType;
	@JsonProperty("cpe_variant")
	private String cpeVariant;
	@JsonProperty("customer_segment")
	private String customerSegment;
    @JsonProperty("error_flag")
    private String errorFlag;
    @JsonProperty("error_msg")
    private String errorMsg;
    @JsonProperty("etc")
    private String etc;
    @JsonProperty("old_contract_term")
    private String oldContractTerm;
    @JsonProperty("payment_currency")
    private String paymentCurrency;
    @JsonProperty("prev_ll_mrc")
    private String prevLLMrc;
    @JsonProperty("prev_port_mrc")
    private String prevPortMrc;
    @JsonProperty("product_name")
	private String productName;
	@JsonProperty("prospect_name")
	private String prospectName;
	@JsonProperty("quotetype_quote")
	private String quotetypeQuote;
	@JsonProperty("resp_city")
	private String respCity;
	@JsonProperty("sales_org")
	private String salesOrg;
    @JsonProperty("service_commissioned_date")
	private String serviceCommissionedDate;
	@JsonProperty("service_id")
	private String serviceId;
    @JsonProperty("siteFlag")
    private String siteFlag;
    @JsonProperty("site_id")
    private String siteId;
    @JsonProperty("termination_date")
    private String terminationDate;
    @JsonProperty("total_mrc")
	private String totalMrc;
    @JsonProperty("version")
	private String version;
    
    @JsonProperty("Bucket_Adjustment_Type")
	public String getBucketAdjustmentType() {
		return bucketAdjustmentType;
	}
    @JsonProperty("Bucket_Adjustment_Type")
	public void setBucketAdjustmentType(String bucketAdjustmentType) {
		this.bucketAdjustmentType = bucketAdjustmentType;
	}
    @JsonProperty("burstable_bW")
	public String getBurstableBW() {
		return burstableBW;
	}
    @JsonProperty("burstable_bW")
	public void setBurstableBW(String burstableBW) {
		this.burstableBW = burstableBW;
	}
    @JsonProperty("bw_mbps")
	public String getBwMbps() {
		return bwMbps;
	}
    @JsonProperty("bw_mbps")
	public void setBwMbps(String bwMbps) {
		this.bwMbps = bwMbps;
	}
    @JsonProperty("contract_expiry_date")
	public String getContractExpiryDate() {
		return contractExpiryDate;
	}
    @JsonProperty("contract_expiry_date")
	public void setContractExpiryDate(String contractExpiryDate) {
		this.contractExpiryDate = contractExpiryDate;
	}
    @JsonProperty("country")
	public String getCountry() {
		return country;
	}
    @JsonProperty("country")
	public void setCountry(String country) {
		this.country = country;
	}
    @JsonProperty("cpe_management_type")
	public String getCpeManagementType() {
		return cpeManagementType;
	}
    @JsonProperty("cpe_management_type")
	public void setCpeManagementType(String cpeManagementType) {
		this.cpeManagementType = cpeManagementType;
	}
    @JsonProperty("cpe_rental_mrc")
	public String getCpeRentalMrc() {
		return cpeRentalMrc;
	}
    @JsonProperty("cpe_rental_mrc")
	public void setCpeRentalMrc(String cpeRentalMrc) {
		this.cpeRentalMrc = cpeRentalMrc;
	}
    @JsonProperty("cpe_supply_type")
	public String getCpeSupplyType() {
		return cpeSupplyType;
	}
    @JsonProperty("cpe_supply_type")
	public void setCpeSupplyType(String cpeSupplyType) {
		this.cpeSupplyType = cpeSupplyType;
	}
    @JsonProperty("cpe_variant")
	public String getCpeVariant() {
		return cpeVariant;
	}
    @JsonProperty("cpe_variant")
	public void setCpeVariant(String cpeVariant) {
		this.cpeVariant = cpeVariant;
	}
    @JsonProperty("customer_segment")
	public String getCustomerSegment() {
		return customerSegment;
	}
    @JsonProperty("customer_segment")
	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}
    @JsonProperty("error_flag")
	public String getErrorFlag() {
		return errorFlag;
	}
    @JsonProperty("error_flag")
	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}
    @JsonProperty("error_msg")
	public String getErrorMsg() {
		return errorMsg;
	}
    @JsonProperty("error_msg")
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
    @JsonProperty("etc")
	public String getEtc() {
		return etc;
	}
    @JsonProperty("etc")
	public void setEtc(String etc) {
		this.etc = etc;
	}
    @JsonProperty("old_contract_term")
	public String getOldContractTerm() {
		return oldContractTerm;
	}
    @JsonProperty("old_contract_term")
	public void setOldContractTerm(String oldContractTerm) {
		this.oldContractTerm = oldContractTerm;
	}
    @JsonProperty("payment_currency")
	public String getPaymentCurrency() {
		return paymentCurrency;
	}
    @JsonProperty("payment_currency")
	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}
    @JsonProperty("prev_ll_mrc")
	public String getPrevLLMrc() {
		return prevLLMrc;
	}
    @JsonProperty("prev_ll_mrc")
	public void setPrevLLMrc(String prevLLMrc) {
		this.prevLLMrc = prevLLMrc;
	}
    @JsonProperty("prev_port_mrc")
	public String getPrevPortMrc() {
		return prevPortMrc;
	}
    @JsonProperty("prev_port_mrc")
	public void setPrevPortMrc(String prevPortMrc) {
		this.prevPortMrc = prevPortMrc;
	}
    @JsonProperty("product_name")
	public String getProductName() {
		return productName;
	}
    @JsonProperty("product_name")
	public void setProductName(String productName) {
		this.productName = productName;
	}
    @JsonProperty("prospect_name")
	public String getProspectName() {
		return prospectName;
	}
    @JsonProperty("prospect_name")
	public void setProspectName(String prospectName) {
		this.prospectName = prospectName;
	}
    @JsonProperty("quotetype_quote")
	public String getQuotetypeQuote() {
		return quotetypeQuote;
	}
    @JsonProperty("quotetype_quote")
	public void setQuotetypeQuote(String quotetypeQuote) {
		this.quotetypeQuote = quotetypeQuote;
	}
    @JsonProperty("resp_city")
	public String getRespCity() {
		return respCity;
	}
    @JsonProperty("resp_city")
	public void setRespCity(String respCity) {
		this.respCity = respCity;
	}
    @JsonProperty("sales_org")
	public String getSalesOrg() {
		return salesOrg;
	}
    @JsonProperty("sales_org")
	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}
    @JsonProperty("service_commissioned_date")
	public String getServiceCommissionedDate() {
		return serviceCommissionedDate;
	}
    @JsonProperty("service_commissioned_date")
	public void setServiceCommissionedDate(String serviceCommissionedDate) {
		this.serviceCommissionedDate = serviceCommissionedDate;
	}
    @JsonProperty("service_id")
	public String getServiceId() {
		return serviceId;
	}
    @JsonProperty("service_id")
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
    @JsonProperty("siteFlag")
	public String getSiteFlag() {
		return siteFlag;
	}
    @JsonProperty("siteFlag")
	public void setSiteFlag(String siteFlag) {
		this.siteFlag = siteFlag;
	}
    @JsonProperty("site_id")
	public String getSiteId() {
		return siteId;
	}
    @JsonProperty("site_id")
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
    @JsonProperty("termination_date")
	public String getTerminationDate() {
		return terminationDate;
	}
    @JsonProperty("termination_date")
	public void setTerminationDate(String terminationDate) {
		this.terminationDate = terminationDate;
	}
    @JsonProperty("total_mrc")
	public String getTotalMrc() {
		return totalMrc;
	}
    @JsonProperty("total_mrc")
	public void setTotalMrc(String totalMrc) {
		this.totalMrc = totalMrc;
	}
    @JsonProperty("version")
	public String getVersion() {
		return version;
	}
    @JsonProperty("version")
	public void setVersion(String version) {
		this.version = version;
	}
	@Override
	public String toString() {
		return "ETCResult [bucketAdjustmentType=" + bucketAdjustmentType + ", burstableBW=" + burstableBW + ", bwMbps="
				+ bwMbps + ", contractExpiryDate=" + contractExpiryDate + ", country=" + country
				+ ", cpeManagementType=" + cpeManagementType + ", cpeRentalMrc=" + cpeRentalMrc + ", cpeSupplyType="
				+ cpeSupplyType + ", cpeVariant=" + cpeVariant + ", customerSegment=" + customerSegment + ", errorFlag="
				+ errorFlag + ", errorMsg=" + errorMsg + ", etc=" + etc + ", oldContractTerm=" + oldContractTerm
				+ ", paymentCurrency=" + paymentCurrency + ", prevLLMrc=" + prevLLMrc + ", prevPortMrc=" + prevPortMrc
				+ ", productName=" + productName + ", prospectName=" + prospectName + ", quotetypeQuote="
				+ quotetypeQuote + ", respCity=" + respCity + ", salesOrg=" + salesOrg + ", serviceCommissionedDate="
				+ serviceCommissionedDate + ", serviceId=" + serviceId + ", siteFlag=" + siteFlag + ", siteId=" + siteId
				+ ", terminationDate=" + terminationDate + ", totalMrc=" + totalMrc + ", version=" + version + "]";
	}
    	
}