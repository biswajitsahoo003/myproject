package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class PoReleaseForCpeRecovery extends BaseRequest {

	private String poRelease;
	private String licencePoRelease;
	private Integer cpeComponentId;
	private Boolean isRouterExists;

	public String getPoRelease() {
		return poRelease;
	}

	public void setPoRelease(String poRelease) {
		this.poRelease = poRelease;
	}

	public String getLicencePoRelease() {
		return licencePoRelease;
	}

	public void setLicencePoRelease(String licencePoRelease) {
		this.licencePoRelease = licencePoRelease;
	}

	public Integer getCpeComponentId() {
		return cpeComponentId;
	}

	public void setCpeComponentId(Integer cpeComponentId) {
		this.cpeComponentId = cpeComponentId;
	}

	public Boolean getIsRouterExists() {
		return isRouterExists;
	}

	public void setIsRouterExists(Boolean isRouterExists) {
		this.isRouterExists = isRouterExists;
	}

}
