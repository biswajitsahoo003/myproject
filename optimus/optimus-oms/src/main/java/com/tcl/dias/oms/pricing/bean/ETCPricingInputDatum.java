
package com.tcl.dias.oms.pricing.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "site_id","prospect_name", "bw_mbps", "burstable_bw",
	"resp_city", "customer_segment", "sales_org", "product_name", 
	"quotetype_quote", "cpe_variant", "cpe_management_type", "cpe_supply_type", 
	"old_contract_term", "service_commissioned_date", "contract_expiry_date",
	"service_id", "country", "siteFlag", "termination_date"
})
public class ETCPricingInputDatum {

	@JsonProperty("site_id")
	private String siteId;
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
	@JsonProperty("quotetype_quote")
	private String quotetypeQuote;
	@JsonProperty("cpe_variant")
	private String cpeVariant;
	@JsonProperty("cpe_management_type")
	private String cpeManagementType;
	@JsonProperty("cpe_supply_type")
	private String cpeSupplyType;
	@JsonProperty("old_contract_term")
	private String oldContractTerm;
	@JsonProperty("service_commissioned_date")
	private String serviceCommissionedDate;
	@JsonProperty("contract_expiry_date")
	private String contractExpiryDate;
	@JsonProperty("service_id")
	private String serviceId;
    @JsonProperty("country")
    private String country;
    @JsonProperty("siteFlag")
    private String siteFlag;
    @JsonProperty("termination_date")
    private String terminationDate;
    
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getProspectName() {
		return prospectName;
	}
	public void setProspectName(String prospectName) {
		this.prospectName = prospectName;
	}
	public String getBwMbps() {
		return bwMbps;
	}
	public void setBwMbps(String bwMbps) {
		this.bwMbps = bwMbps;
	}
	public String getBurstableBw() {
		return burstableBw;
	}
	public void setBurstableBw(String burstableBw) {
		this.burstableBw = burstableBw;
	}
	public String getRespCity() {
		return respCity;
	}
	public void setRespCity(String respCity) {
		this.respCity = respCity;
	}
	public String getCustomerSegment() {
		return customerSegment;
	}
	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}
	public String getSalesOrg() {
		return salesOrg;
	}
	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getQuotetypeQuote() {
		return quotetypeQuote;
	}
	public void setQuotetypeQuote(String quotetypeQuote) {
		this.quotetypeQuote = quotetypeQuote;
	}
	public String getCpeVariant() {
		return cpeVariant;
	}
	public void setCpeVariant(String cpeVariant) {
		this.cpeVariant = cpeVariant;
	}
	public String getCpeManagementType() {
		return cpeManagementType;
	}
	public void setCpeManagementType(String cpeManagementType) {
		this.cpeManagementType = cpeManagementType;
	}
	public String getCpeSupplyType() {
		return cpeSupplyType;
	}
	public void setCpeSupplyType(String cpeSupplyType) {
		this.cpeSupplyType = cpeSupplyType;
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
	public String getContractExpiryDate() {
		return contractExpiryDate;
	}
	public void setContractExpiryDate(String contractExpiryDate) {
		this.contractExpiryDate = contractExpiryDate;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getSiteFlag() {
		return siteFlag;
	}
	public void setSiteFlag(String siteFlag) {
		this.siteFlag = siteFlag;
	}
	public String getTerminationDate() {
		return terminationDate;
	}
	public void setTerminationDate(String terminationDate) {
		this.terminationDate = terminationDate;
	}
	@Override
	public String toString() {
		return "ETCPricingInputDatum [siteId=" + siteId + ", prospectName=" + prospectName + ", bwMbps=" + bwMbps
				+ ", burstableBw=" + burstableBw + ", respCity=" + respCity + ", customerSegment=" + customerSegment
				+ ", salesOrg=" + salesOrg + ", productName=" + productName + ", quotetypeQuote=" + quotetypeQuote
				+ ", cpeVariant=" + cpeVariant + ", cpeManagementType=" + cpeManagementType + ", cpeSupplyType="
				+ cpeSupplyType + ", oldContractTerm=" + oldContractTerm + ", serviceCommissionedDate="
				+ serviceCommissionedDate + ", contractExpiryDate=" + contractExpiryDate + ", serviceId=" + serviceId
				+ ", country=" + country + ", siteFlag=" + siteFlag + ", terminationDate=" + terminationDate + "]";
	}
}
