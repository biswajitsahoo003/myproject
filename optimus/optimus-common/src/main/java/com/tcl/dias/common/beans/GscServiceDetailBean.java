package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * File for gsc related service details
 *
 *  @author Anusha Unni
 *  @link http://www.tatacommunications.com/
 *  @copyright 2020 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GscServiceDetailBean implements Serializable {

	private String id;
	private String sfdcCUID;
	private String ipAddress;
	private String sipTrunkGroup;
	private String tclSwitch;
	private String circuitID;
	private String erfCusCustomerId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSfdcCUID() {
		return sfdcCUID;
	}

	public void setSfdcCUID(String sfdcCUID) {
		this.sfdcCUID = sfdcCUID;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getSipTrunkGroup() {
		return sipTrunkGroup;
	}

	public void setSipTrunkGroup(String sipTrunkGroup) {
		this.sipTrunkGroup = sipTrunkGroup;
	}

	public String getTclSwitch() {
		return tclSwitch;
	}

	public void setTclSwitch(String tclSwitch) {
		this.tclSwitch = tclSwitch;
	}

	public String getCircuitID() {
		return circuitID;
	}

	public void setCircuitID(String circuitID) {
		this.circuitID = circuitID;
	}

	public String getErfCusCustomerId() {
		return erfCusCustomerId;
	}

	public void setErfCusCustomerId(String erfCusCustomerId) {
		this.erfCusCustomerId = erfCusCustomerId;
	}
}
