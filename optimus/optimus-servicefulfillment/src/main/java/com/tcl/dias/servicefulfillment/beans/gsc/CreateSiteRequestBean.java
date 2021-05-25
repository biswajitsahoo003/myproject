package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class CreateSiteRequestBean extends TaskDetailsBaseBean {

	private Integer customerId;
	private List<SiteDetailsBean> siteDetails;
	private String message;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<SiteDetailsBean> getSiteDetails() {
		return siteDetails;
	}

	public void setSiteDetails(List<SiteDetailsBean> siteDetails) {
		this.siteDetails = siteDetails;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
}
