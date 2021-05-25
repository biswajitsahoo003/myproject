package com.tcl.dias.common.beans;


public class MfLinkDetailsBean {
	
	private Integer linkId;
	private String mfLinkType;
	private String overallLinkFeasibilityStatus ;
	private Integer quoteId;
	private Integer isSelected;
	
	private String returnedSiteType;
	
	
	public String getReturnedSiteType() {
		return returnedSiteType;
	}
	public void setReturnedSiteType(String returnedSiteType) {
		this.returnedSiteType = returnedSiteType;
	}
	public Integer getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(Integer isSelected) {
		this.isSelected = isSelected;
	}
	public Integer getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}
	public String getOverallLinkFeasibilityStatus() {
		return overallLinkFeasibilityStatus;
	}
	public void setOverallLinkFeasibilityStatus(String overallLinkFeasibilityStatus) {
		this.overallLinkFeasibilityStatus = overallLinkFeasibilityStatus;
	}
	public String getMfLinkType() {
		return mfLinkType;
	}
	public void setMfLinkType(String mfLinkType) {
		this.mfLinkType = mfLinkType;
	}
	private MfSiteADetailsBean siteADetail;
	public MfSiteADetailsBean getSiteADetail() {
		return siteADetail;
	}
	public void setSiteADetail(MfSiteADetailsBean siteADetail) {
		this.siteADetail = siteADetail;
	}
	private MfSiteBDetailsBean siteBDetail;
	public MfSiteBDetailsBean getSiteBDetail() {
		return siteBDetail;
	}
	public void setSiteBDetail(MfSiteBDetailsBean siteBDetail) {
		this.siteBDetail = siteBDetail;
	}
	private String linkResponseJson;
	public Integer getLinkId() {
		return linkId;
	}
	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	
	public String getLinkResponseJson() {
		return linkResponseJson;
	}
	public void setLinkResponseJson(String linkResponseJson) {
		this.linkResponseJson = linkResponseJson;
	}
	

	 @Override
	    public String toString(){
	        return "linkId:: "+linkId+", mfLinkType::" +mfLinkType +", overallLinkFeasibilityStatus:: "+overallLinkFeasibilityStatus +",quoteId ::"+quoteId
	        		+ " ,isSelected:: "+isSelected;
	    }
	

}
