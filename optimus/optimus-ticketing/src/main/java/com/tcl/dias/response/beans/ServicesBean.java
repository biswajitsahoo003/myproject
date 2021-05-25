package com.tcl.dias.response.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This file contains the ServicesBean.java class. used for response
 * handling
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServicesBean {
	
	private String serviceIdentifier;
	private String protectionStatus;	
	private String serviceType;
	private String serviceAlias;
	private String customerLeName;
	private String cuid;

	public String getCustomerLeName() {
		return customerLeName;
	}

	public void setCustomerLeName(String customerLeName) {
		this.customerLeName = customerLeName;
	}

	public String getCuid() {
		return cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}
	
	/**
	 * @return the serviceIdentifier
	 */
	public String getServiceIdentifier() {
		return serviceIdentifier;
	}
	/**
	 * @param serviceIdentifier the serviceIdentifier to set
	 */
	public void setServiceIdentifier(String serviceIdentifier) {
		this.serviceIdentifier = serviceIdentifier;
	}
	/**
	 * @return the protectionStatus
	 */
	public String getProtectionStatus() {
		return protectionStatus;
	}
	/**
	 * @param protectionStatus the protectionStatus to set
	 */
	public void setProtectionStatus(String protectionStatus) {
		this.protectionStatus = protectionStatus;
	}
	/**
	 * @return the serviceType
	 */
	public String getServiceType() {
		return serviceType;
	}
	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	/**
	 * @return the serviceAlias
	 */
	public String getServiceAlias() {
		return serviceAlias;
	}
	/**
	 * @param serviceAlias the serviceAlias to set
	 */
	public void setServiceAlias(String serviceAlias) {
		this.serviceAlias = serviceAlias;
	}
	

}
