package com.tcl.dias.servicefulfillment.beans.gsc;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class GSCServiceDetailBean extends TaskDetailsBaseBean {
	
	private Integer id;
	
    private String serviceCode;
    
    private String offeringName;
  
	private String srcCountry;
    
    private String dstCountry;

	public Integer getId() {
		return id;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public String getSrcCountry() {
		return srcCountry;
	}

	public String getDstCountry() {
		return dstCountry;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public void setSrcCountry(String srcCountry) {
		this.srcCountry = srcCountry;
	}

	public void setDstCountry(String dstCountry) {
		this.dstCountry = dstCountry;
	}
}
