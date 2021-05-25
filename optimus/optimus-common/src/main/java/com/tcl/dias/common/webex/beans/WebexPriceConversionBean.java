package com.tcl.dias.common.webex.beans;

import java.math.BigDecimal;

/**
 * Bean for price conversion of webex
 * 
 * @author Srinivasa Raghavan
 *
 */
public class WebexPriceConversionBean {

	private String existingCurrency;

	private String inputCurrency;

	private Integer uID;
	private String country;
	private String phoneType;
	private Integer destId;
	private String destinationName;
	private BigDecimal nrc;
	private BigDecimal mrc;
	private BigDecimal highRate;
	private String comments;

	public WebexPriceConversionBean() {
	}

	public String getExistingCurrency() {
		return existingCurrency;
	}

	public void setExistingCurrency(String existingCurrency) {
		this.existingCurrency = existingCurrency;
	}

	public String getInputCurrency() {
		return inputCurrency;
	}

	public void setInputCurrency(String inputCurrency) {
		this.inputCurrency = inputCurrency;
	}

	public BigDecimal getMrc() {
		return mrc;
	}

	public void setMrc(BigDecimal mrc) {
		this.mrc = mrc;
	}

	public BigDecimal getNrc() {
		return nrc;
	}

	public void setNrc(BigDecimal nrc) {
		this.nrc = nrc;
	}

	public BigDecimal getHighRate() {
		return highRate;
	}

	public void setHighRate(BigDecimal highRate) {
		this.highRate = highRate;
	}

	public Integer getuID() {
		return uID;
	}

	public void setuID(Integer uID) {
		this.uID = uID;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public Integer getDestId() {
		return destId;
	}

	public void setDestId(Integer destId) {
		this.destId = destId;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
