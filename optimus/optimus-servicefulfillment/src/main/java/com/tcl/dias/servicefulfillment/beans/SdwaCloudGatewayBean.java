package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.CosDetail;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class SdwaCloudGatewayBean extends TaskDetailsBaseBean {

	private List<CosDetail> cosDetails;
	private String secondaryCGWServiceId;

	public List<CosDetail> getCosDetails() {
		return cosDetails;
	}
	public void setCosDetails(List<CosDetail> cosDetails) {
		this.cosDetails = cosDetails;
	}
	public String getSecondaryCGWServiceId() {
		return secondaryCGWServiceId;
	}
	public void setSecondaryCGWServiceId(String secondaryCGWServiceId) {
		this.secondaryCGWServiceId = secondaryCGWServiceId;
	}
}