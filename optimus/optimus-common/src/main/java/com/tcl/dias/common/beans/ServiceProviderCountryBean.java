package com.tcl.dias.common.beans;

public class ServiceProviderCountryBean {
	
	private String serviceProviderName;
	
	private String serviceProviderCountry;
	
	private String serviceProviderCountryGroup;

	public String getServiceProviderName() {
		return serviceProviderName;
	}

	public void setServiceProviderName(String serviceProviderName) {
		this.serviceProviderName = serviceProviderName;
	}

	public String getServiceProviderCountry() {
		return serviceProviderCountry;
	}

	public void setServiceProviderCountry(String serviceProviderCountry) {
		this.serviceProviderCountry = serviceProviderCountry;
	}

	public String getServiceProviderCountryGroup() {
		return serviceProviderCountryGroup;
	}

	public void setServiceProviderCountryGroup(String serviceProviderCountryGroup) {
		this.serviceProviderCountryGroup = serviceProviderCountryGroup;
	}

	@Override
	public String toString() {
		return "ServiceProviderTaxBean [serviceProviderName=" + serviceProviderName + ", serviceProviderCountry="
				+ serviceProviderCountry + ", serviceProviderCountryGroup=" + serviceProviderCountryGroup + "]";
	}

}
