package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.List;

public class DomesticCallingServiceDetail {
	private Integer domesticCallingServiceDetailId;
	private String existingConnectionId;
	private Boolean newConnectionRequired;
	private String noOfEmeregencyNos;
	private Address serviceAddress;
	private String noOfConcurrentChannels;
	private List<DidDetail> didDetails;

	public Integer getDomesticCallingServiceDetailId() {
		return domesticCallingServiceDetailId;
	}

	public void setDomesticCallingServiceDetailId(Integer domesticCallingServiceDetailId) {
		this.domesticCallingServiceDetailId = domesticCallingServiceDetailId;
	}

	public String getExistingConnectionId() {
		return existingConnectionId;
	}

	public void setExistingConnectionId(String existingConnectionId) {
		this.existingConnectionId = existingConnectionId;
	}

	public Boolean getNewConnectionRequired() {
		return newConnectionRequired;
	}

	public void setNewConnectionRequired(Boolean newConnectionRequired) {
		this.newConnectionRequired = newConnectionRequired;
	}

	public String getNoOfEmeregencyNos() {
		return noOfEmeregencyNos;
	}

	public void setNoOfEmeregencyNos(String noOfEmeregencyNos) {
		this.noOfEmeregencyNos = noOfEmeregencyNos;
	}

	public Address getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(Address serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public String getNoOfConcurrentChannels() {
		return noOfConcurrentChannels;
	}

	public void setNoOfConcurrentChannels(String noOfConcurrentChannels) {
		this.noOfConcurrentChannels = noOfConcurrentChannels;
	}

	public List<DidDetail> getDidDetails() {
		return didDetails;
	}

	public void setDidDetails(List<DidDetail> didDetails) {
		this.didDetails = didDetails;
	}
}
