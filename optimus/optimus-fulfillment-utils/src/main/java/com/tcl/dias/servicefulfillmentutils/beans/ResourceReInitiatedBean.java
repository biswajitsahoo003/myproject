package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class ResourceReInitiatedBean extends BaseRequest {
	private Integer serviceId;

	private String rrfsDate;
	
	
	/**
	 * @return the rrfsDate
	 */
	public String getRrfsDate() {
		return rrfsDate;
	}

	/**
	 * @param rrfsDate the rrfsDate to set
	 */
	public void setRrfsDate(String rrfsDate) {
		this.rrfsDate = rrfsDate;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

}
