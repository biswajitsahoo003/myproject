package com.tcl.dias.oms.teamsdr.beans;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the TeamsDRServicesResponseBean.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "componentName", "quantity", "plan", "contractTerm", "isBundle", "isAddOn", "isMediaGateway",
		"agreementType", "providerName", "licenseType", "isLicense", "mrc", "nrc", "arc", "tcv" })
public class TeamsDRServicesResponseBean {
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

	@JsonProperty("mrc")
	private BigDecimal mrc;

	@JsonProperty("nrc")
	private BigDecimal nrc;

	@JsonProperty("arc")
	private BigDecimal arc;

	@JsonProperty("tcv")
	private BigDecimal tcv;

	public TeamsDRServicesResponseBean() {
	}

	@JsonProperty("componentName")
	public String getComponentName() {
		return componentName;
	}

	@JsonProperty("componentName")
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	@JsonProperty("quantity")
	public String getQuantity() {
		return quantity;
	}

	@JsonProperty("quantity")
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	@JsonProperty("plan")
	public String getPlan() {
		return plan;
	}

	@JsonProperty("plan")
	public void setPlan(String plan) {
		this.plan = plan;
	}

	@JsonProperty("contractTerm")
	public String getContractTerm() {
		return contractTerm;
	}

	@JsonProperty("contractTerm")
	public void setContractTerm(String contractTerm) {
		this.contractTerm = contractTerm;
	}

	@JsonProperty("isBundle")
	public String getIsBundle() {
		return isBundle;
	}

	@JsonProperty("isBundle")
	public void setIsBundle(String isBundle) {
		this.isBundle = isBundle;
	}

	@JsonProperty("isAddOn")
	public String getIsAddOn() {
		return isAddOn;
	}

	@JsonProperty("isAddOn")
	public void setIsAddOn(String isAddOn) {
		this.isAddOn = isAddOn;
	}

	@JsonProperty("isMediaGateway")
	public String getIsMediaGateway() {
		return isMediaGateway;
	}

	@JsonProperty("isMediaGateway")
	public void setIsMediaGateway(String isMediaGateway) {
		this.isMediaGateway = isMediaGateway;
	}

	@JsonProperty("agreementType")
	public String getAgreementType() {
		return agreementType;
	}

	@JsonProperty("agreementType")
	public void setAgreementType(String agreementType) {
		this.agreementType = agreementType;
	}

	@JsonProperty("providerName")
	public String getProviderName() {
		return providerName;
	}

	@JsonProperty("providerName")
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	@JsonProperty("licenseType")
	public String getLicenseType() {
		return licenseType;
	}

	@JsonProperty("licenseType")
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	@JsonProperty("isLicense")
	public String getIsLicense() {
		return isLicense;
	}

	@JsonProperty("isLicense")
	public void setIsLicense(String isLicense) {
		this.isLicense = isLicense;
	}

	@JsonProperty("mrc")
	public BigDecimal getMrc() {
		return mrc;
	}

	@JsonProperty("mrc")
	public void setMrc(BigDecimal mrc) {
		this.mrc = mrc;
	}

	@JsonProperty("nrc")
	public BigDecimal getNrc() {
		return nrc;
	}

	@JsonProperty("nrc")
	public void setNrc(BigDecimal nrc) {
		this.nrc = nrc;
	}

	@JsonProperty("arc")
	public BigDecimal getArc() {
		return arc;
	}

	@JsonProperty("arc")
	public void setArc(BigDecimal arc) {
		this.arc = arc;
	}

	@JsonProperty("tcv")
	public BigDecimal getTcv() {
		return tcv;
	}

	@JsonProperty("tcv")
	public void setTcv(BigDecimal tcv) {
		this.tcv = tcv;
	}

	@Override
	public String toString() {
		return "TeamsDRServicesResponseBean{" + "componentName='" + componentName + '\'' + ", quantity='" + quantity
				+ '\'' + ", plan='" + plan + '\'' + ", contractTerm='" + contractTerm + '\'' + ", isBundle='" + isBundle
				+ '\'' + ", isAddOn='" + isAddOn + '\'' + ", isMediaGateway='" + isMediaGateway + '\''
				+ ", agreementType='" + agreementType + '\'' + ", providerName='" + providerName + '\''
				+ ", licenseType='" + licenseType + '\'' + ", isLicense='" + isLicense + '\'' + ", mrc=" + mrc
				+ ", nrc=" + nrc + ", arc=" + arc + ", tcv=" + tcv + '}';
	}
}
