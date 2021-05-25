package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

public class NetworkSummaryDetails {
	
private Integer quoteId;
	
	private Integer quoteLeId;
	
	private SiteTypeSummary siteTypeSummary;
	
	
	private CpeBandwidthSummaryDetails cpeBandwidthSummaryDet;
	
	private List<BandwidthSummaryDetails> bandwidthSummaryDet;
	
	
	public List<BandwidthSummaryDetails> getBandwidthSummaryDe() {
		return bandwidthSummaryDet;
	}

	public void setBandwidthSummaryDe(List<BandwidthSummaryDetails> bandwidthSummaryDe) {
		this.bandwidthSummaryDet = bandwidthSummaryDe;
	}

	public CpeBandwidthSummaryDetails getCpeBandwidthSummaryDet() {
		return cpeBandwidthSummaryDet;
	}

	public void setCpeBandwidthSummaryDet(CpeBandwidthSummaryDetails cpeBandwidthSummaryDet) {
		this.cpeBandwidthSummaryDet = cpeBandwidthSummaryDet;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public Integer getQuoteLeId() {
		return quoteLeId;
	}

	public void setQuoteLeId(Integer quoteLeId) {
		this.quoteLeId = quoteLeId;
	}

	public SiteTypeSummary getSiteTypeSummary() {
		return siteTypeSummary;
	}

	public void setSiteTypeSummary(SiteTypeSummary siteTypeSummary) {
		this.siteTypeSummary = siteTypeSummary;
	}

	

}
