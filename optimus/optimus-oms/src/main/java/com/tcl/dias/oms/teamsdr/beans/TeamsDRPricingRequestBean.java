package com.tcl.dias.oms.teamsdr.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Price request bean for pricing team
 *
 * @author Srinivasa Raghavan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({})
public class TeamsDRPricingRequestBean {

	@JsonProperty("componentName")
	String componentName;

	@JsonProperty("quantity")
	String quantity;

	@JsonProperty("plan")
	String plan;

	@JsonProperty("contractTerm")
	String contractTerm;

	@JsonProperty("isBundle")
	String isBundle;

	@JsonProperty("isAddOn")
	String isAddOn;

	@JsonProperty("isMediaGateway")
	String isMediaGateway;

	@JsonProperty("agreementType")
	String agreementType;

	@JsonProperty("providerName")
	String providerName;

	@JsonProperty("licenseType")
	String licenseType;

	@JsonProperty("isLicense")
	String isLicense;

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getIsBundle() {
		return isBundle;
	}

	public void setIsBundle(String isBundle) {
		this.isBundle = isBundle;
	}

	public String getIsAddOn() {
		return isAddOn;
	}

	public void setIsAddOn(String isAddOn) {
		this.isAddOn = isAddOn;
	}

	public String getIsMediaGateway() {
		return isMediaGateway;
	}

	public void setIsMediaGateway(String isMediaGateway) {
		this.isMediaGateway = isMediaGateway;
	}

	public String getIsLicense() {
		return isLicense;
	}

	public void setIsLicense(String isLicense) {
		this.isLicense = isLicense;
	}

	public String getContractTerm() {
		return contractTerm;
	}

	public void setContractTerm(String contractTerm) {
		this.contractTerm = contractTerm;
	}

	public String getAgreementType() {
		return agreementType;
	}

	public void setAgreementType(String agreementType) {
		this.agreementType = agreementType;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
}
