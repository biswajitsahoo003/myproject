
package com.tcl.dias.oms.ipc.beans.pricebean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.netty.util.internal.StringUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "quoteId", "region", "term", "managementEnabled", "prospect_name", "customer_segment", "sales_org",
		"account_id_with_18_digit", "customer_id", "cu_le_id", "customerLeCountry", "classification",
		"partnerProfileId", "isDealRegistration", "askedPrice", "askedPriceComment", "partnerCommissionPercentage", "volumeDiscountPercentage",
		"delegationDiscountPercentage", "termDiscountPercentage", "additionalDiscountPercentage",
		"inputDiscountPercentage", "ipcFinalPrice", "crossBorderWhTaxPercentage", "performTaxCalculation", "cloudvm",
		"access", "addon", "fixedBwpricingResponse", "dealRegistrationCommissionPercentage", "multiYearCommissionPercentage" })
public class Quote {

	@JsonProperty("quoteId")
	private String quoteId;
	@JsonProperty("region")
	private String region;
	@JsonProperty("term")
	private Integer term;
	@JsonProperty("managementEnabled")
	private Boolean managementEnabled;
	@JsonProperty("prospect_name")
	private String prospectName;
	@JsonProperty("customer_segment")
	private String customerSegment;
	@JsonProperty("sales_org")
	private String salesOrg;
	@JsonProperty("account_id_with_18_digit")
	private String accountId;
	@JsonProperty("customer_id")
	private Integer customerId;
	@JsonProperty("cu_le_id")
	private String customerLeId;
	@JsonProperty("customerLeCountry")
	private String customerLeCountry = StringUtil.EMPTY_STRING;
	@JsonProperty("volumeDiscountPercentage")
	private Double volumeDiscountPercentage;
	@JsonProperty("delegationDiscountPercentage")
	private Double delegationDiscountPercentage;
	@JsonProperty("termDiscountPercentage")
	private Double termDiscountPercentage;
	@JsonProperty("additionalDiscountPercentage")
	private Double additionalDiscountPercentage;
	@JsonProperty("inputDiscountPercentage")
	private Double inputDiscountPercentage;
	@JsonProperty("ipcFinalPrice")
	private Double ipcFinalPrice;
	@JsonProperty("crossBorderWhTaxPercentage")
	private Double crossBorderWhTaxPercentage = 0D;
	@JsonProperty("performTaxCalculation")
	private Boolean performTaxCalculation = false;
	@JsonProperty("cloudvm")
	private List<Cloudvm> cloudvm = new ArrayList<>();
	@JsonProperty("access")
	private List<Access> access = new ArrayList<>();
	@JsonProperty("addon")
	private List<Addon> addon = new ArrayList<>();
	@JsonProperty("fixedBwpricingResponse")
	private String fixedBwpricingResponse;

	@JsonProperty("askedPrice")
	private String askedPrice;
	@JsonProperty("askedPriceComment")
	private String askedPriceComment;
	
	@JsonProperty("classification")
	private String classification;
	@JsonProperty("partnerProfileId")
	private Integer partnerProfileId;
	@JsonProperty("isDealRegistration")
	private Boolean isDealRegistration = false;
	@JsonProperty("partnerCommissionPercentage")
	private Double partnerCommissionPercentage;
	@JsonProperty("dealRegistrationCommissionPercentage")
	private Double DealRegistrationCommissionPercentage;
	@JsonProperty("multiYearCommissionPercentage")
	private Double MultiYearCommissionPercentage;

	@JsonProperty("quoteId")
	public String getQuoteId() {
		return quoteId;
	}

	@JsonProperty("quoteId")
	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	@JsonProperty("region")
	public String getRegion() {
		return region;
	}

	@JsonProperty("region")
	public void setRegion(String region) {
		this.region = region;
	}

	@JsonProperty("term")
	public Integer getTerm() {
		return term;
	}

	@JsonProperty("term")
	public void setTerm(Integer term) {
		this.term = term;
	}

	@JsonProperty("managementEnabled")
	public Boolean getManagementEnabled() {
		return managementEnabled;
	}

	@JsonProperty("managementEnabled")
	public void setManagementEnabled(Boolean managementEnabled) {
		this.managementEnabled = managementEnabled;
	}

	@JsonProperty("prospect_name")
	public String getProspectName() {
		return prospectName;
	}

	@JsonProperty("prospect_name")
	public void setProspectName(String prospectName) {
		this.prospectName = prospectName;
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

	@JsonProperty("account_id_with_18_digit")
	public String getAccountId() {
		return accountId;
	}

	@JsonProperty("account_id_with_18_digit")
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@JsonProperty("customer_id")
	public Integer getCustomerId() {
		return customerId;
	}

	@JsonProperty("customer_id")
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	@JsonProperty("cu_le_id")
	public String getCustomerLeId() {
		return customerLeId;
	}

	@JsonProperty("cu_le_id")
	public void setCustomerLeId(String customerLeId) {
		this.customerLeId = customerLeId;
	}

	@JsonProperty("customerLeCountry")
	public String getCustomerLeCountry() {
		return customerLeCountry;
	}

	@JsonProperty("customerLeCountry")
	public void setCustomerLeCountry(String customerLeCountry) {
		this.customerLeCountry = customerLeCountry;
	}

	@JsonProperty("additionalDiscountPercentage")
	public Double getAdditionalDiscountPercentage() {
		return additionalDiscountPercentage;
	}

	@JsonProperty("additionalDiscountPercentage")
	public void setAdditionalDiscountPercentage(Double additionalDiscountPercentage) {
		this.additionalDiscountPercentage = additionalDiscountPercentage;
	}

	@JsonProperty("inputDiscountPercentage")
	public Double getInputDiscountPercentage() {
		return inputDiscountPercentage;
	}

	@JsonProperty("inputDiscountPercentage")
	public void setInputDiscountPercentage(Double inputDiscountPercentage) {
		this.inputDiscountPercentage = inputDiscountPercentage;
	}

	@JsonProperty("ipcFinalPrice")
	public Double getIpcFinalPrice() {
		return ipcFinalPrice;
	}

	@JsonProperty("ipcFinalPrice")
	public void setIpcFinalPrice(Double ipcFinalPrice) {
		this.ipcFinalPrice = ipcFinalPrice;
	}

	@JsonProperty("crossBorderWhTaxPercentage")
	public Double getCrossBorderWhTaxPercentage() {
		return crossBorderWhTaxPercentage;
	}

	@JsonProperty("crossBorderWhTaxPercentage")
	public void setCrossBorderWhTaxPercentage(Double crossBorderWhTaxPercentage) {
		this.crossBorderWhTaxPercentage = crossBorderWhTaxPercentage;
	}

	@JsonProperty("performTaxCalculation")
	public Boolean getPerformTaxCalculation() {
		return performTaxCalculation;
	}

	@JsonProperty("performTaxCalculation")
	public void setPerformTaxCalculation(Boolean performTaxCalculation) {
		this.performTaxCalculation = performTaxCalculation;
	}

	@JsonProperty("cloudvm")
	public List<Cloudvm> getCloudvm() {
		return cloudvm;
	}

	@JsonProperty("cloudvm")
	public void setCloudvm(List<Cloudvm> cloudvm) {
		this.cloudvm = cloudvm;
	}

	@JsonProperty("access")
	public List<Access> getAccess() {
		return access;
	}

	@JsonProperty("access")
	public void setAccess(List<Access> access) {
		this.access = access;
	}

	@JsonProperty("addon")
	public List<Addon> getAddon() {
		return addon;
	}

	@JsonProperty("addon")
	public void setAddon(List<Addon> addon) {
		this.addon = addon;
	}

	@JsonProperty("askedPrice")
	public String getAskedPrice() {
		return askedPrice;
	}

	@JsonProperty("askedPrice")
	public void setAskedPrice(String askedPrice) {
		this.askedPrice = askedPrice;
	}

	@JsonProperty("askedPriceComment")
	public String getAskedPriceComment() {
		return askedPriceComment;
	}

	@JsonProperty("askedPriceComment")
	public void setAskedPriceComment(String askedPriceComment) {
		this.askedPriceComment = askedPriceComment;
	}

	public Double getVolumeDiscountPercentage() {
		return volumeDiscountPercentage;
	}

	public void setVolumeDiscountPercentage(Double volumeDiscountPercentage) {
		this.volumeDiscountPercentage = volumeDiscountPercentage;
	}

	public Double getDelegationDiscountPercentage() {
		return delegationDiscountPercentage;
	}

	public void setDelegationDiscountPercentage(Double delegationDiscountPercentage) {
		this.delegationDiscountPercentage = delegationDiscountPercentage;
	}

	public Double getTermDiscountPercentage() {
		return termDiscountPercentage;
	}

	public void setTermDiscountPercentage(Double termDiscountPercentage) {
		this.termDiscountPercentage = termDiscountPercentage;
	}
	
	public String getFixedBwpricingResponse() {
		return fixedBwpricingResponse;
	}

	public void setFixedBwpricingResponse(String fixedBwpricingResponse) {
		this.fixedBwpricingResponse = fixedBwpricingResponse;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public Integer getPartnerProfileId() {
		return partnerProfileId;
	}

	public void setPartnerProfileId(Integer partnerProfileId) {
		this.partnerProfileId = partnerProfileId;
	}

	public Boolean getIsDealRegistration() {
		return isDealRegistration;
	}

	public void setIsDealRegistration(Boolean isDealRegistration) {
		this.isDealRegistration = isDealRegistration;
	}

	public Double getPartnerCommissionPercentage() {
		return partnerCommissionPercentage;
	}

	public void setPartnerCommissionPercentage(Double partnerCommissionPercentage) {
		this.partnerCommissionPercentage = partnerCommissionPercentage;
	}
	
	@JsonProperty("dealRegistrationCommissionPercentage")
	public Double getDealRegistrationCommissionPercentage() {
		return DealRegistrationCommissionPercentage;
	}
	
	@JsonProperty("dealRegistrationCommissionPercentage")
	public void setDealRegistrationCommissionPercentage(Double dealRegistrationCommissionPercentage) {
		DealRegistrationCommissionPercentage = dealRegistrationCommissionPercentage;
	}
	
	@JsonProperty("multiYearCommissionPercentage")
	public Double getMultiYearCommissionPercentage() {
		return MultiYearCommissionPercentage;
	}
	
	@JsonProperty("multiYearCommissionPercentage")
	public void setMultiYearCommissionPercentage(Double multiYearCommissionPercentage) {
		MultiYearCommissionPercentage = multiYearCommissionPercentage;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("quoteId", quoteId).append("region", region).append("term", term)
				.append("managementEnabled", managementEnabled).append("cloudvm", cloudvm).append("access", access)
				.append("addon", addon).toString();
	}

}
