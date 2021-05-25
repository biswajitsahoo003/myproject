package com.tcl.dias.oms.macd.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tcl.dias.oms.beans.CompareQuotes;

/**
 * This is the bean class for the macd order summary response
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MACDOrderSummaryResponse {
	
	public String serviceId;
	public String primaryServiceId;
	public String secondaryServiceId;
	public String changeRequests;
	private String parallelBuild;
	private String parallelRunDays;
	private String siteAddress;
	private String serviceType;
	private String siteType;
	
	private MACDAttributesBean quotesAttributes;
	
	private CompareQuotes pricingsList;

	/**
	 * @return the changeRequests
	 */
	public String getChangeRequests() {
		return changeRequests;
	}

	/**
	 * @param changeRequests the changeRequests to set
	 */
	public void setChangeRequests(String changeRequests) {
		this.changeRequests = changeRequests;
	}

	/**
	 * @return the parallelBuild
	 */
	public String getParallelBuild() {
		return parallelBuild;
	}

	/**
	 * @param parallelBuild the parallelBuild to set
	 */
	public void setParallelBuild(String parallelBuild) {
		this.parallelBuild = parallelBuild;
	}

	/**
	 * @return the parallelRunDays
	 */
	public String getParallelRunDays() {
		return parallelRunDays;
	}

	/**
	 * @param parallelRunDays the parallelRunDays to set
	 */
	public void setParallelRunDays(String parallelRunDays) {
		this.parallelRunDays = parallelRunDays;
	}



	public CompareQuotes getPricingsList() {
		return pricingsList;
	}

	public void setPricingsList(CompareQuotes pricingsList) {
		this.pricingsList = pricingsList;
	}

	/**
	 * @return the quotesAttributes
	 */
	public MACDAttributesBean getQuotesAttributes() {
		return quotesAttributes;
	}

	/**
	 * @param quotesAttributes the quotesAttributes to set
	 */
	public void setQuotesAttributes(MACDAttributesBean quotesAttributes) {
		this.quotesAttributes = quotesAttributes;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}


	public String getSecondaryServiceId() {
		return secondaryServiceId;
	}

	public void setSecondaryServiceId(String secondaryServiceId) {
		this.secondaryServiceId = secondaryServiceId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getPrimaryServiceId() {
		return primaryServiceId;
	}

	public void setPrimaryServiceId(String primaryServiceId) {
		this.primaryServiceId = primaryServiceId;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	@Override
	public String toString() {
		return "MACDOrderSummaryResponse{" +
				"serviceId='" + serviceId + '\'' +
				", primaryServiceId='" + primaryServiceId + '\'' +
				", secondaryServiceId='" + secondaryServiceId + '\'' +
				", changeRequests='" + changeRequests + '\'' +
				", parallelBuild='" + parallelBuild + '\'' +
				", parallelRunDays='" + parallelRunDays + '\'' +
				", siteAddress='" + siteAddress + '\'' +
				", serviceType='" + serviceType + '\'' +
				", siteType='" + siteType + '\'' +
				", quotesAttributes=" + quotesAttributes +
				", pricingsList=" + pricingsList +
				'}';
	}
}
