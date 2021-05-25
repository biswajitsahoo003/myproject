package com.tcl.dias.oms.webex.beans;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Pricing Bean for Ucaas Quotes.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "arc", "hsnCode", "id", "isEndpoint", "isEndpointIntl", "isLicenseComponent", "license_fee",
		"license_lcmc", "license_ohs", "unitMrc", "mrc", "mrc_plus_tax", "unitNrc", "nrc", "quantity", "skuId",
		"skuName", "tcv", "uom" })
public class PricingUcaasQuotesBean {

	@JsonProperty("arc")
	private BigDecimal arc;

	@JsonProperty("hsnCode")
	private BigDecimal hsnCode;

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("isEndpoint")
	private String isEndpoint;

	@JsonProperty("isEndpointIntl")
	private String isEndpointIntl;

	@JsonProperty("isLicenseComponent")
	private String isLicenseComponent;

	@JsonProperty("license_fee")
	private BigDecimal licenseFee;

	@JsonProperty("license_lcmc")
	private BigDecimal licenseLcmc;

	@JsonProperty("license_ohs")
	private BigDecimal licenseOhs;

	@JsonProperty("mrc")
	private BigDecimal mrc;

	@JsonProperty("unitMrc")
	private BigDecimal unitMrc;

	@JsonProperty("unitNrc")
	private BigDecimal unitNrc;

	@JsonProperty("mrc_plus_tax")
	private BigDecimal mrcPlusTax;

	@JsonProperty("nrc")
	private BigDecimal nrc;

	@JsonProperty("quantity")
	private Integer quantity;

	@JsonProperty("skuId")
	private String skuID;

	@JsonProperty("skuName")
	private String skuName;

	@JsonProperty("tcv")
	private BigDecimal tcv;

	@JsonProperty("uom")
	private String uom;

	public PricingUcaasQuotesBean() {

	}

	@JsonProperty("arc")
	public BigDecimal getArc() {
		return arc;
	}

	@JsonProperty("arc")
	public void setArc(BigDecimal arc) {
		this.arc = arc;
	}

	@JsonProperty("hsnCode")
	public BigDecimal getHsnCode() {
		return hsnCode;
	}

	@JsonProperty("hsnCode")
	public void setHsnCode(BigDecimal hsnCode) {
		this.hsnCode = hsnCode;
	}

	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("license_fee")
	public BigDecimal getLicenseFee() {
		return licenseFee;
	}

	@JsonProperty("license_fee")
	public void setLicenseFee(BigDecimal licenseFee) {
		this.licenseFee = licenseFee;
	}

	@JsonProperty("license_lcmc")
	public BigDecimal getLicenseLcmc() {
		return licenseLcmc;
	}

	@JsonProperty("license_lcmc")
	public void setLicenseLcmc(BigDecimal licenseLcmc) {
		this.licenseLcmc = licenseLcmc;
	}

	@JsonProperty("license_ohs")
	public BigDecimal getLicenseOhs() {
		return licenseOhs;
	}

	@JsonProperty("license_ohs")
	public void setLicenseOhs(BigDecimal licenseOhs) {
		this.licenseOhs = licenseOhs;
	}

	@JsonProperty("mrc")
	public BigDecimal getMrc() {
		return mrc;
	}

	@JsonProperty("mrc")
	public void setMrc(BigDecimal mrc) {
		this.mrc = mrc;
	}

	@JsonProperty("mrc_plus_tax")
	public BigDecimal getMrcPlusTax() {
		return mrcPlusTax;
	}

	@JsonProperty("mrc_plus_tax")
	public void setMrcPlusTax(BigDecimal mrcPlusTax) {
		this.mrcPlusTax = mrcPlusTax;
	}

	@JsonProperty("nrc")
	public BigDecimal getNrc() {
		return nrc;
	}

	@JsonProperty("nrc")
	public void setNrc(BigDecimal nrc) {
		this.nrc = nrc;
	}

	@JsonProperty("quantity")
	public Integer getQuantity() {
		return quantity;
	}

	@JsonProperty("quantity")
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@JsonProperty("skuId")
	public String getSkuID() {
		return skuID;
	}

	@JsonProperty("skuId")
	public void setSkuID(String skuID) {
		this.skuID = skuID;
	}

	@JsonProperty("skuName")
	public String getSkuName() {
		return skuName;
	}

	@JsonProperty("skuName")
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	@JsonProperty("tcv")
	public BigDecimal getTcv() {
		return tcv;
	}

	@JsonProperty("tcv")
	public void setTcv(BigDecimal tcv) {
		this.tcv = tcv;
	}

	@JsonProperty("uom")
	public String getUom() {
		return uom;
	}

	@JsonProperty("uom")
	public void setUom(String uom) {
		this.uom = uom;
	}

	@JsonProperty("isEndpoint")
	public String getIsEndpoint() {
		return isEndpoint;
	}

	@JsonProperty("isEndpoint")
	public void setIsEndpoint(String isEndpoint) {
		this.isEndpoint = isEndpoint;
	}

	@JsonProperty("isEndpointIntl")
	public String getIsEndpointIntl() {
		return isEndpointIntl;
	}

	@JsonProperty("isEndpointIntl")
	public void setIsEndpointIntl(String isEndpointIntl) {
		this.isEndpointIntl = isEndpointIntl;
	}

	@JsonProperty("isLicenseComponent")
	public String getIsLicenseComponent() {
		return isLicenseComponent;
	}

	@JsonProperty("isLicenseComponent")
	public void setIsLicenseComponent(String isLicenseComponent) {
		this.isLicenseComponent = isLicenseComponent;
	}

	@JsonProperty("unitMrc")
	public BigDecimal getUnitMrc() {
		return unitMrc;
	}

	@JsonProperty("unitMrc")
	public void setUnitMrc(BigDecimal unitMrc) {
		this.unitMrc = unitMrc;
	}

	@JsonProperty("unitNrc")
	public BigDecimal getUnitNrc() {
		return unitNrc;
	}

	@JsonProperty("unitNrc")
	public void setUnitNrc(BigDecimal unitNrc) {
		this.unitNrc = unitNrc;
	}

	@Override
	public String toString() {
		return "PricingUcaasQuotesBean [arc=" + arc + ", hsnCode=" + hsnCode + ", id=" + id + ", isEndpoint="
				+ isEndpoint + ", isEndpointIntl=" + isEndpointIntl + ", isLicenseComponent=" + isLicenseComponent
				+ ", licenseFee=" + licenseFee + ", licenseLcmc=" + licenseLcmc + ", licenseOhs=" + licenseOhs
				+ ", mrc=" + mrc + ", unitMrc=" + unitMrc + ", unitNrc=" + unitNrc + ", mrcPlusTax=" + mrcPlusTax
				+ ", nrc=" + nrc + ", quantity=" + quantity + ", skuID=" + skuID + ", skuName=" + skuName + ", tcv="
				+ tcv + ", uom=" + uom + "]";
	}

}
