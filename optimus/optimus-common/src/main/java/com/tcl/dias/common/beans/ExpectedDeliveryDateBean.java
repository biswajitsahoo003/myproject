package com.tcl.dias.common.beans;

/**
 * Bean class for RfsDate estimation
 * 
 * @author VISHESH AWASTHI
 *
 */
public class ExpectedDeliveryDateBean {

	private String service;
	private String accessType;
	private String country;
	
	public ExpectedDeliveryDateBean() {
	}
	
	public ExpectedDeliveryDateBean(String service, String accessType, String country) {
		super();
		this.service = service;
		this.accessType = accessType;
		this.country = country;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "RfsDateBean [service=" + service + ", accessType=" + accessType + ", country="
				+ country + "]";
	}
}
