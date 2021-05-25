package com.tcl.dias.serviceinventory.beans;

/**
 * Bean class to hold origin and destination details
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SICountryBean {

	private String origin;

	private String destination;

	private String accessType;

	private String numbersCount;

	private Integer orderId;

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getNumbersCount() {
		return numbersCount;
	}

	public void setNumbersCount(String numbersCount) {
		this.numbersCount = numbersCount;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
}
