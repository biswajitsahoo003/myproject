package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the vw_mstmdr_license database table.
 * 
 * @author Srinivasa Raghavan
 */
@Entity
@Table(name = "vw_mstmdr_license")
@NamedQuery(name = "TeamsDRLicensePrices.findAll", query = "SELECT v FROM TeamsDRLicensePrices v")
public class TeamsDRLicensePrices implements Serializable {
	private static final long serialVersionUID = 1L;

	private String agreement;

	@Column(name = "billing_frequency")
	private String billingFrequency;

	@Column(name = "disp_nm")
	private String dispNm;

	@Column(name = "is_volunteer")
	private String isVolunteer;

	@Column(name = "long_desc")
	private String longDesc;

	@Column(name = "max_seat")
	private Integer maxSeat;

	@Column(name = "min_seat")
	private Integer minSeat;

	private String nm;

	@Id
	@Column(name = "offer_id")
	private String offerId;


	@Column(name = "with_ac")
	private String withAc;

	@Column(name = "vendor")
	private String vendor;

	@Column(name = "list_price")
	private Double listPrice;

	@Column(name = "list_price_currency")
	private String list_price_currency;

	@Column(name = "erp_price")
	private Double erpPrice;

	@Column(name = "erp_price_currency")
	private String erpPriceCurrency;

	@Column(name = "is_active_ind")
	private String isActiveInd;

	@Column(name = "sfdc_product_name")
	private String sfdcProductName;


	public TeamsDRLicensePrices() {
	}

	public String getAgreement() {
		return this.agreement;
	}

	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}

	public String getBillingFrequency() {
		return this.billingFrequency;
	}

	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}

	public String getDispNm() {
		return this.dispNm;
	}

	public void setDispNm(String dispNm) {
		this.dispNm = dispNm;
	}

	public String getIsVolunteer() {
		return this.isVolunteer;
	}

	public void setIsVolunteer(String isVolunteer) {
		this.isVolunteer = isVolunteer;
	}

	public String getLongDesc() {
		return this.longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	public Integer getMaxSeat() {
		return this.maxSeat;
	}

	public void setMaxSeat(Integer maxSeat) {
		this.maxSeat = maxSeat;
	}

	public Integer getMinSeat() {
		return this.minSeat;
	}

	public void setMinSeat(Integer minSeat) {
		this.minSeat = minSeat;
	}

	public String getNm() {
		return this.nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}

	public String getOfferId() {
		return this.offerId;
	}

	public String getWithAc() {
		return this.withAc;
	}

	public void setWithAc(String withAc) {
		this.withAc = withAc;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Double getListPrice() {
		return listPrice;
	}

	public void setListPrice(Double listPrice) {
		this.listPrice = listPrice;
	}

	public String getList_price_currency() {
		return list_price_currency;
	}

	public void setList_price_currency(String list_price_currency) {
		this.list_price_currency = list_price_currency;
	}

	public Double getErpPrice() {
		return erpPrice;
	}

	public void setErpPrice(Double erpPrice) {
		this.erpPrice = erpPrice;
	}

	public String getErpPriceCurrency() {
		return erpPriceCurrency;
	}

	public void setErpPriceCurrency(String erpPriceCurrency) {
		this.erpPriceCurrency = erpPriceCurrency;
	}

	public String getIsActiveInd() {
		return isActiveInd;
	}

	public void setIsActiveInd(String isActiveInd) {
		this.isActiveInd = isActiveInd;
	}

	public String getSfdcProductName() {
		return sfdcProductName;
	}

	public void setSfdcProductName(String sfdcProductName) {
		this.sfdcProductName = sfdcProductName;
	}
}