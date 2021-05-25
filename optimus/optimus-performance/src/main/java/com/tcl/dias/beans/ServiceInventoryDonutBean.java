package com.tcl.dias.beans;

/**
 * This file contains Service Inventory details for downloadable MSP Report -
 * donut charts
 *
 * @author KRUTSRIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ServiceInventoryDonutBean {
	private String serviceIdCount;
	private String cuid;
	private String targetServiceType;
	private String monthYr;
	private String serviceStatus;

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public String getServiceIdCount() {
		return serviceIdCount;
	}

	public void setServiceIdCount(String serviceIdCount) {
		this.serviceIdCount = serviceIdCount;
	}

	public String getCuid() {
		return cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}

	public String getTargetServiceType() {
		return targetServiceType;
	}

	public void setTargetServiceType(String targetServiceType) {
		this.targetServiceType = targetServiceType;
	}

	public String getMonthYr() {
		return monthYr;
	}

	public void setMonthYr(String monthYr) {
		this.monthYr = monthYr;
	}

}
