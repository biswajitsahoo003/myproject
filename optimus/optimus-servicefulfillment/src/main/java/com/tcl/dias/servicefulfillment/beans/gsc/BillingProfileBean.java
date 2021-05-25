package com.tcl.dias.servicefulfillment.beans.gsc;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BillingProfileBean {
	
	private Integer serviceId;
	private String cmsId;
    private String profileId;
    private List<String> physicalAddressId;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String profileStartDate;

    
    
	public List<String> getPhysicalAddressId() {
		return physicalAddressId;
	}

	public void setPhysicalAddressId(List<String> physicalAddressId) {
		this.physicalAddressId = physicalAddressId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getCmsId() {
		return cmsId;
	}

	public void setCmsId(String cmsId) {
		this.cmsId = cmsId;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getProfileStartDate() {
		return profileStartDate;
	}

	public void setProfileStartDate(String profileStartDate) {
		this.profileStartDate = profileStartDate;
	}

}
