package com.tcl.dias.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file contains the month wise uptime percentage data.
 * 
 *
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class TrendInfoBean {

	private String month;
	
	private String uptimePercentage;

	private Integer slaBreachCount;

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
	 * @return the uptimePercentage
	 */
	public String getUptimePercentage() {
		return uptimePercentage;
	}

	/**
	 * @param uptimePercentage the uptimePercentage to set
	 */
	public void setUptimePercentage(String uptimePercentage) {
		this.uptimePercentage = uptimePercentage;
	}

	/**
	 * @return the slaBreachCount
	 */
	public Integer getSlaBreachCount() {
		return slaBreachCount;
	}

	/**
	 * @param slaBreachCount the slaBreachCount to set
	 */
	public void setSlaBreachCount(Integer slaBreachCount) {
		this.slaBreachCount = slaBreachCount;
	}

}
