package com.tcl.dias.oms.beans;

import java.util.List;

public class CommercialRejectionBean {
	private String quoteId;
	private String siteId;
	private Boolean siteRejectionStatus;
	private String quoteRejectionComments;
	private Boolean quoteRejectionStatus;
	private List<Integer> sites;
	private String linkId;
	private List<Integer> links;
	
	
	
	
	
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public List<Integer> getLinks() {
		return links;
	}
	public void setLinks(List<Integer> links) {
		this.links = links;
	}
	public List<Integer> getSites() {
		return sites;
	}
	public void setSites(List<Integer> sites) {
		this.sites = sites;
	}
	public String getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
	public String getQuoteRejectionComments() {
		return quoteRejectionComments;
	}
	public void setQuoteRejectionComments(String quoteRejectionComments) {
		this.quoteRejectionComments = quoteRejectionComments;
	}
	public Boolean getSiteRejectionStatus() {
		return siteRejectionStatus;
	}
	public void setSiteRejectionStatus(Boolean siteRejectionStatus) {
		this.siteRejectionStatus = siteRejectionStatus;
	}
	public Boolean getQuoteRejectionStatus() {
		return quoteRejectionStatus;
	}
	public void setQuoteRejectionStatus(Boolean quoteRejectionStatus) {
		this.quoteRejectionStatus = quoteRejectionStatus;
	}
	
	
	

}
