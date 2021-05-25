package com.tcl.dias.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class ServiceIdDetails {

	
	@JsonProperty("customerSiteName")
	private String customerSiteName;
	
	@JsonProperty("serviceId")
	private String serviceIdentifier;

	@JsonProperty("customerSiteName")
	public String getCustomerSiteName() {
		return customerSiteName;
	}

	@JsonProperty("customerSiteName")
	public void setCustomerSiteName(String customerSiteName) {
		this.customerSiteName = customerSiteName;
	}

	@JsonProperty("serviceId")
	public String getServiceIdentifier() {
		return serviceIdentifier;
	}

	@JsonProperty("serviceId")
	public void setServiceIdentifier(String serviceIdentifier) {
		this.serviceIdentifier = serviceIdentifier;
	}

}
