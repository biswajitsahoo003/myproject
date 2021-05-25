package com.tcl.dias.common.beans;

import java.util.List;
import java.util.Map;

public class CompleteTaskBean {

	private List<SiteDetail> sitedetail;
	private Map<String, Object> discountapproval;
	public List<SiteDetail> getSitedetail() {
		return sitedetail;
	}
	public void setSitedetail(List<SiteDetail> sitedetail) {
		this.sitedetail = sitedetail;
	}
	public Map<String, Object> getDiscountapproval() {
		return discountapproval;
	}
	public void setDiscountapproval(Map<String, Object> discountapproval) {
		this.discountapproval = discountapproval;
	}
	
	
}
