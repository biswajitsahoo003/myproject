package com.tcl.dias.common.teamsdr.beans;

/**
 * Bean for request and response for quote prices for initial services offered
 * by teams DR
 * 
 * @author Srinivasa Raghavan
 */
public class TeamsDRServicesQuoteOfferingsBean {
	private String offeringName;
	private Integer noOfUsers;
	private String plan;
	private String offeringType;
	private Double mrc;
	private Double nrc;
	private Double arc;
	private Double tcv;

	public TeamsDRServicesQuoteOfferingsBean() {
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getOfferingType() {
		return offeringType;
	}

	public void setOfferingType(String offeringType) {
		this.offeringType = offeringType;
	}

	public Integer getNoOfUsers() {
		return noOfUsers;
	}

	public void setNoOfUsers(Integer noOfUsers) {
		this.noOfUsers = noOfUsers;
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
		return "TeamsDRServicesQuoteOfferingsBean{" + "offeringName='" + offeringName + '\'' + ", noOfUsers="
				+ noOfUsers + ", plan='" + plan + '\'' + ", mrc=" + mrc + ", nrc=" + nrc + ", arc=" + arc + ", tcv="
				+ tcv + '}';
	}
}