package com.tcl.dias.beans;

/**
 * This file contains the ReportBean.java class.
 * 
 *
 * @author Deepika S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class UptimeReportBean {

	private String month;

	private Integer cuid;

	private String serviceId;

	private String serviceType;

	private String uptime;

	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * @return the cuid
	 */
	public Integer getCuid() {
		return cuid;
	}

	/**
	 * @param cuid the cuid to set
	 */
	public void setCuid(Integer cuid) {
		this.cuid = cuid;
	}
	
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
	 * @return the uptime
	 */
	public String getUptime() {
		return uptime;
	}

	/**
	 * @param uptime the uptime to set
	 */
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}
	
}
