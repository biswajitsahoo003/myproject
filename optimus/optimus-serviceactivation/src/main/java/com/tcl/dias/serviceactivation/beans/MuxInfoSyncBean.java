package com.tcl.dias.serviceactivation.beans;

import java.io.Serializable;

public class MuxInfoSyncBean implements Serializable {

	private static final long serialVersionUID = -9158343118036268274L;

	private String serviceId;
	private String requestId;
	private String bandwidthUnit;
	private String bandwidthValue;
	private String burstableBandwidthUnit;
	private String burstableBandwidthValue;
	private String feasibilityId;
	private String muxIp;
	private String muxName;
	private String portType;
	private String copfId;
	private String orderType;
	private String requestType;
	private String requestingSystem;
	private String serviceType;
	
	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

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

	public String getBandwidthUnit() {
		return bandwidthUnit;
	}

	public void setBandwidthUnit(String bandwidthUnit) {
		this.bandwidthUnit = bandwidthUnit;
	}

	public String getBandwidthValue() {
		return bandwidthValue;
	}

	public void setBandwidthValue(String bandwidthValue) {
		this.bandwidthValue = bandwidthValue;
	}

	public String getFeasibilityId() {
		return feasibilityId;
	}

	public void setFeasibilityId(String feasibilityId) {
		this.feasibilityId = feasibilityId;
	}

	public String getMuxIp() {
		return muxIp;
	}

	public void setMuxIp(String muxIp) {
		this.muxIp = muxIp;
	}

	public String getMuxName() {
		return muxName;
	}

	public void setMuxName(String muxName) {
		this.muxName = muxName;
	}

	public String getPortType() {
		return portType;
	}

	public void setPortType(String portType) {
		this.portType = portType;
	}

	public String getCopfId() {
		return copfId;
	}

	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getRequestingSystem() {
		return requestingSystem;
	}

	public void setRequestingSystem(String requestingSystem) {
		this.requestingSystem = requestingSystem;
	}

	public String getBurstableBandwidthUnit() {
		return burstableBandwidthUnit;
	}

	public void setBurstableBandwidthUnit(String burstableBandwidthUnit) {
		this.burstableBandwidthUnit = burstableBandwidthUnit;
	}

	public String getBurstableBandwidthValue() {
		return burstableBandwidthValue;
	}

	public void setBurstableBandwidthValue(String burstableBandwidthValue) {
		this.burstableBandwidthValue = burstableBandwidthValue;
	}

}
