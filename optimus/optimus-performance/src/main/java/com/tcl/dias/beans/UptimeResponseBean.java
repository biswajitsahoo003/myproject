package com.tcl.dias.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the ticket count with percentage uptime data.
 * 
 *
 * @author DSIVALIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class UptimeResponseBean {
	
	private Integer totalCount;
	
	private String totalUptimePercentage;
	
	
	private List<UptimeReportBean> uptimeReports = new ArrayList<>();

	/**
	 * @return the totalCount
	 */
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the totalUptimePercentage
	 */
	public String getTotalUptimePercentage() {
		return totalUptimePercentage;
	}

	/**
	 * @param totalUptimePercentage the totalUptimePercentage to set
	 */
	public void setTotalUptimePercentage(String totalUptimePercentage) {
		this.totalUptimePercentage = totalUptimePercentage;
	}

	/**
	 * @return the uptimeReports
	 */
	public List<UptimeReportBean> getUptimeReports() {
		return uptimeReports;
	}

	/**
	 * @param uptimeReports the uptimeReports to set
	 */
	public void setUptimeReports(List<UptimeReportBean> uptimeReports) {
		this.uptimeReports = uptimeReports;
	}

}
