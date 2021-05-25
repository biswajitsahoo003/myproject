package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * POJO to fold LM details of a service ID
 *
 * @author krutsrin
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class LMDetailBean {
	
	private String siteType;
	private String serviceId;
	private String serviceInventoryLMProvider;
	private boolean preFeasible;
	
	public boolean isPreFeasible() {
		return preFeasible;
	}
	public void setPreFeasible(boolean preFeasible) {
		this.preFeasible = preFeasible;
	}
	@JsonInclude(Include.NON_NULL)
	private String mfLMProvider;
	private Integer siteId;

	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public String getServiceInventoryLMProvider() {
		return serviceInventoryLMProvider;
	}
	public void setServiceInventoryLMProvider(String serviceInventoryLMProvider) {
		this.serviceInventoryLMProvider = serviceInventoryLMProvider;
	}
	public String getMfLMProvider() {
		return mfLMProvider;
	}
	public void setMfLMProvider(String mfLMProvider) {
		this.mfLMProvider = mfLMProvider;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	

}
