package com.tcl.dias.beans;


/**
 * This file contains sla breach details
 *
 * @author KRUTSRIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SLABreachBean {
	
	private String cuid;
	
	private String serviceID;

	
	private String serviceLink;

	
	private String serviceType;

	private String siteAddress;
	
	private String GRServiceInventoryPrimaryCircuitAlias;

	private String achievedUptime;

	private String committedUptime;

	private String GRServiceInventorySecondaryCircuitAlias;
	
	private String monthYr;
	
	private String UpDowntimeMinutes;
	
	private String Ticketcount;

	public String getCuid() {
		return cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	public String getServiceLink() {
		return serviceLink;
	}

	public void setServiceLink(String serviceLink) {
		this.serviceLink = serviceLink;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getGRServiceInventoryPrimaryCircuitAlias() {
		return GRServiceInventoryPrimaryCircuitAlias;
	}

	public void setGRServiceInventoryPrimaryCircuitAlias(String gRServiceInventoryPrimaryCircuitAlias) {
		GRServiceInventoryPrimaryCircuitAlias = gRServiceInventoryPrimaryCircuitAlias;
	}

	public String getAchievedUptime() {
		return achievedUptime;
	}

	public void setAchievedUptime(String achievedUptime) {
		this.achievedUptime = achievedUptime;
	}

	public String getCommittedUptime() {
		return committedUptime;
	}

	public void setCommittedUptime(String committedUptime) {
		this.committedUptime = committedUptime;
	}

	public String getGRServiceInventorySecondaryCircuitAlias() {
		return GRServiceInventorySecondaryCircuitAlias;
	}

	public void setGRServiceInventorySecondaryCircuitAlias(String gRServiceInventorySecondaryCircuitAlias) {
		GRServiceInventorySecondaryCircuitAlias = gRServiceInventorySecondaryCircuitAlias;
	}

	public String getMonthYr() {
		return monthYr;
	}

	public void setMonthYr(String monthYr) {
		this.monthYr = monthYr;
	}

	public String getUpDowntimeMinutes() {
		return UpDowntimeMinutes;
	}

	public void setUpDowntimeMinutes(String upDowntimeMinutes) {
		UpDowntimeMinutes = upDowntimeMinutes;
	}

	public String getTicketcount() {
		return Ticketcount;
	}

	public void setTicketcount(String ticketcount) {
		Ticketcount = ticketcount;
	}


	
}
