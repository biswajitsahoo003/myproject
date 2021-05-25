package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;

/**
 * GetMuxInfo Asynchronous mux info cramer service class
 * 
 * @author NAVEENKUMAR G
 *
 */
public class GetMuxInfo implements Serializable {

	private static final long serialVersionUID = 6557686793714991667L;
	private String serviceId;
	private String requestId;
	private String muxIP;
	private String muxName;
	private String bay;
	private String eorid;
	private String EORProvisionStatus;
	private String floor;
	private String muxprovisionStatus;
	private String priority;
	private String wing;
	private String message;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getMuxIP() {
		return muxIP;
	}

	public void setMuxIP(String muxIP) {
		this.muxIP = muxIP;
	}

	public String getMuxName() {
		return muxName;
	}

	public void setMuxName(String muxName) {
		this.muxName = muxName;
	}

	public String getBay() {
		return bay;
	}

	public void setBay(String bay) {
		this.bay = bay;
	}

	public String getEorid() {
		return eorid;
	}

	public void setEorid(String eorid) {
		this.eorid = eorid;
	}

	public String getEORProvisionStatus() {
		return EORProvisionStatus;
	}

	public void setEORProvisionStatus(String eORProvisionStatus) {
		EORProvisionStatus = eORProvisionStatus;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getMuxprovisionStatus() {
		return muxprovisionStatus;
	}

	public void setMuxprovisionStatus(String muxprovisionStatus) {
		this.muxprovisionStatus = muxprovisionStatus;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getWing() {
		return wing;
	}

	public void setWing(String wing) {
		this.wing = wing;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
