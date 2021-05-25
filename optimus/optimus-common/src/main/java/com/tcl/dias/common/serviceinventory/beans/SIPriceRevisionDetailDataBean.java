package com.tcl.dias.common.serviceinventory.beans;

/**
 * Bean class to hold SI Price Revision detail data
 *
 * @author Veera B
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SIPriceRevisionDetailDataBean {
	
	private String serviceId;

	private String maxPriceRevDate;

	private String effDateOfPriceRevision;

	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return the maxPriceRevDate
	 */
	public String getMaxPriceRevDate() {
		return maxPriceRevDate;
	}

	/**
	 * @param maxPriceRevDate the maxPriceRevDate to set
	 */
	public void setMaxPriceRevDate(String maxPriceRevDate) {
		this.maxPriceRevDate = maxPriceRevDate;
	}

	/**
	 * @return the effDateOfPriceRevision
	 */
	public String getEffDateOfPriceRevision() {
		return effDateOfPriceRevision;
	}

	/**
	 * @param effDateOfPriceRevision the effDateOfPriceRevision to set
	 */
	public void setEffDateOfPriceRevision(String effDateOfPriceRevision) {
		this.effDateOfPriceRevision = effDateOfPriceRevision;
	}

	@Override
	public String toString() {
		return "SIPriceRevisionDetailBean [serviceId=" + serviceId + ", maxPriceRevDate=" + maxPriceRevDate
				+ ", effDateOfPriceRevision=" + effDateOfPriceRevision + "]";
	}

}
