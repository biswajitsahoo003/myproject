package com.tcl.dias.oms.teamsdr.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * License list price bean
 * 
 * @author srraghav
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LicenseListPricesBean {

	@JsonProperty("Offer Display Name")
	private String offerDisplayName;

	@JsonProperty("no_of_license")
	private Integer noOfLicense;

	@JsonProperty("license_cost_mrc")
	private Double licenseCostMrc;

	@JsonProperty("license_cost_arc")
	private Double licenseCostArc;

	@JsonProperty("license_cost_nrc")
	private Double licenseCostNrc;

	@JsonProperty("licenseCostTcv")
	private Double licenseCostTcv;

	@JsonProperty("discount_to_tata")
	private Double discountToTata;

	@JsonProperty("after_discount_mrc")
	private Double afterDiscountMrc;

	@JsonProperty("after_discount_nrc")
	private Double afterDiscountNrc;

	@JsonProperty("after_discount_arc")
	private Double afterDiscountArc;

	@JsonProperty("after_discount_tcv")
	private Double afterDiscountTcv;

	@JsonProperty("markup_pct")
	private Double markupPct;

	@JsonProperty("unit_mrc_sp")
	private Double unitMrcSp;

	@JsonProperty("unit_nrc_sp")
	private Double unitNrcSp;

	@JsonProperty("unit_arc_sp")
	private Double unitArcSp;

	@JsonProperty("unit_tcv_sp")
	private Double unitTcvSp;

	@JsonProperty("mrc")
	private Double mrc;

	@JsonProperty("nrc")
	private Double nrc;

	@JsonProperty("arc")
	private Double arc;

	@JsonProperty("tcv")
	private Double tcv;

	public String getOfferDisplayName() {
		return offerDisplayName;
	}

	public void setOfferDisplayName(String offerDisplayName) {
		this.offerDisplayName = offerDisplayName;
	}

	public Integer getNoOfLicense() {
		return noOfLicense;
	}

	public void setNoOfLicense(Integer noOfLicense) {
		this.noOfLicense = noOfLicense;
	}

	public Double getLicenseCostMrc() {
		return licenseCostMrc;
	}

	public void setLicenseCostMrc(Double licenseCostMrc) {
		this.licenseCostMrc = licenseCostMrc;
	}

	public Double getLicenseCostArc() {
		return licenseCostArc;
	}

	public void setLicenseCostArc(Double licenseCostArc) {
		this.licenseCostArc = licenseCostArc;
	}

	public Double getLicenseCostNrc() {
		return licenseCostNrc;
	}

	public void setLicenseCostNrc(Double licenseCostNrc) {
		this.licenseCostNrc = licenseCostNrc;
	}

	public Double getLicenseCostTcv() {
		return licenseCostTcv;
	}

	public void setLicenseCostTcv(Double licenseCostTcv) {
		this.licenseCostTcv = licenseCostTcv;
	}

	public Double getDiscountToTata() {
		return discountToTata;
	}

	public void setDiscountToTata(Double discountToTata) {
		this.discountToTata = discountToTata;
	}

	public Double getAfterDiscountMrc() {
		return afterDiscountMrc;
	}

	public void setAfterDiscountMrc(Double afterDiscountMrc) {
		this.afterDiscountMrc = afterDiscountMrc;
	}

	public Double getAfterDiscountNrc() {
		return afterDiscountNrc;
	}

	public void setAfterDiscountNrc(Double afterDiscountNrc) {
		this.afterDiscountNrc = afterDiscountNrc;
	}

	public Double getAfterDiscountArc() {
		return afterDiscountArc;
	}

	public void setAfterDiscountArc(Double afterDiscountArc) {
		this.afterDiscountArc = afterDiscountArc;
	}

	public Double getAfterDiscountTcv() {
		return afterDiscountTcv;
	}

	public void setAfterDiscountTcv(Double afterDiscountTcv) {
		this.afterDiscountTcv = afterDiscountTcv;
	}

	public Double getMarkupPct() {
		return markupPct;
	}

	public void setMarkupPct(Double markupPct) {
		this.markupPct = markupPct;
	}

	public Double getUnitMrcSp() {
		return unitMrcSp;
	}

	public void setUnitMrcSp(Double unitMrcSp) {
		this.unitMrcSp = unitMrcSp;
	}

	public Double getUnitNrcSp() {
		return unitNrcSp;
	}

	public void setUnitNrcSp(Double unitNrcSp) {
		this.unitNrcSp = unitNrcSp;
	}

	public Double getUnitArcSp() {
		return unitArcSp;
	}

	public void setUnitArcSp(Double unitArcSp) {
		this.unitArcSp = unitArcSp;
	}

	public Double getUnitTcvSp() {
		return unitTcvSp;
	}

	public void setUnitTcvSp(Double unitTcvSp) {
		this.unitTcvSp = unitTcvSp;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	@Override
	public String toString() {
		return "LicenseListPricesBean{" + "offerDisplayName='" + offerDisplayName + '\'' + ", noOfLicense="
				+ noOfLicense + ", licenseCostMrc=" + licenseCostMrc + ", licenseCostArc=" + licenseCostArc
				+ ", licenseCostNrc=" + licenseCostNrc + ", licenseCostTcv=" + licenseCostTcv + ", discountToTata="
				+ discountToTata + ", afterDiscountMrc=" + afterDiscountMrc + ", afterDiscountNrc=" + afterDiscountNrc
				+ ", afterDiscountArc=" + afterDiscountArc + ", afterDiscountTcv=" + afterDiscountTcv + ", markupPct="
				+ markupPct + ", unitMrcSp=" + unitMrcSp + ", unitNrcSp=" + unitNrcSp + ", unitArcSp=" + unitArcSp
				+ ", unitTcvSp=" + unitTcvSp + ", mrc=" + mrc + ", nrc=" + nrc + ", arc=" + arc + ", tcv=" + tcv + '}';
	}
}
