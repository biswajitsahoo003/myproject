package com.tcl.dias.beans;

import java.util.List;

/**
 * This file contains the monthly uptime data - product with uptime percentage.
 * 
 * @author Deepika Sivalingam.
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CircuitUptimeResponseBean {
	
	private String month;
	
	private List<UptimeInfoBean> uptimeBeans;

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
	 * @return the uptimeBeans
	 */
	public List<UptimeInfoBean> getUptimeBeans() {
		return uptimeBeans;
	}

	/**
	 * @param uptimeBeans the uptimeBeans to set
	 */
	public void setUptimeBeans(List<UptimeInfoBean> uptimeBeans) {
		this.uptimeBeans = uptimeBeans;
	}
	
	
}
