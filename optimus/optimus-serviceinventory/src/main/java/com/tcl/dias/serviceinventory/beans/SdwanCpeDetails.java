package com.tcl.dias.serviceinventory.beans;

import com.tcl.dias.common.beans.Attributes;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SdwanCpeDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cpeName;
	private String cpeStatus;
	private String cpeAvailability;
	private List<Integer> cpeAtributeid;
	private String sdwanServiceId;
	private String underlayServiceId;
	private List<NetworkSiteDetails> networkSiteDetails;
	private List<String> controllers;
	private List<Attributes> links;

	public SdwanCpeDetails() {
		this.cpeStatus = "Offline";
	}

	public String getCpeName() {
		return cpeName;
	}

	public void setCpeName(String cpeName) {
		this.cpeName = cpeName;
	}

	public String getCpeStatus() {
		return cpeStatus;
	}

	public void setCpeStatus(String cpeStatus) {
		this.cpeStatus = cpeStatus;
	}

	public String getCpeAvailability() {
		return cpeAvailability;
	}

	public void setCpeAvailability(String cpeAvailability) {
		this.cpeAvailability = cpeAvailability;
	}

	public List<Integer> getCpeAtributeid() {
		return cpeAtributeid;
	}

	public void setCpeAtributeid(List<Integer> cpeAtributeid) {
		this.cpeAtributeid = cpeAtributeid;
	}

	public List<NetworkSiteDetails> getNetworkSiteDetails() {
		return networkSiteDetails;
	}

	public void setNetworkSiteDetails(List<NetworkSiteDetails> networkSiteDetails) {
		this.networkSiteDetails = networkSiteDetails;
	}

	public String getSdwanServiceId() {
		return sdwanServiceId;
	}

	public void setSdwanServiceId(String sdwanServiceId) {
		this.sdwanServiceId = sdwanServiceId;
	}

	public String getUnderlayServiceId() {
		return underlayServiceId;
	}

	public void setUnderlayServiceId(String underlayServiceId) {
		this.underlayServiceId = underlayServiceId;
	}

	public List<String> getControllers() {
		return controllers;
	}

	public void setControllers(List<String> controllers) {
		this.controllers = controllers;
	}

	public List<Attributes> getLinks() {
		return links;
	}

	public void setLinks(List<Attributes> links) {
		this.links = links;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SdwanCpeDetails that = (SdwanCpeDetails) o;
		return cpeName.equals(that.cpeName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpeName);
	}
}
