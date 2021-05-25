package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;

public class DeletedLineItemDetailsBean {
	private String flavour;
	private String hostname;
	private String actionType;
	private String dataRetention;
	private String dataRetentionPeriod;

	public DeletedLineItemDetailsBean(String flavour, String hostname) {
		this.flavour = flavour;
		this.hostname = hostname;
	}

	public DeletedLineItemDetailsBean(String flavour, String hostname, String actionType, String dataRetention,
			String dataRetentionPeriod) {
		this.flavour = flavour;
		this.hostname = hostname;
		this.actionType = actionType;
		this.dataRetention = dataRetention;
		if (IpcConstants.YES_LOWER_CASE.equals(dataRetention.toLowerCase())) {
			this.dataRetentionPeriod = dataRetentionPeriod;
		} else {
			this.dataRetentionPeriod = IpcConstants.N_BACKSLASH_A;
		}
	}

	public String getFlavour() {
		return flavour;
	}

	public void setFlavour(String flavour) {
		this.flavour = flavour;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getDataRetention() {
		return dataRetention;
	}

	public void setDataRetention(String dataRetention) {
		this.dataRetention = dataRetention;
	}

	public String getDataRetentionPeriod() {
		return dataRetentionPeriod;
	}

	public void setDataRetentionPeriod(String dataRetentionPeriod) {
		this.dataRetentionPeriod = dataRetentionPeriod;
	}
}
