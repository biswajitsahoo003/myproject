package com.tcl.dias.oms.gsc.tiger.beans;

public class InterConnectTypeDetail {
	private Integer interConnectionDetailId;
	private Address serviceAddress;
	private String codec;
	private String publicIPAddresses;
	private Integer noOfConcurrentChannels;
	private Integer existingConnectionId;

	public Integer getInterConnectionDetailId() {
		return interConnectionDetailId;
	}

	public void setInterConnectionDetailId(Integer interConnectionDetailId) {
		this.interConnectionDetailId = interConnectionDetailId;
	}

	public Address getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(Address serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public String getPublicIPAddresses() {
		return publicIPAddresses;
	}

	public void setPublicIPAddresses(String publicIPAddresses) {
		this.publicIPAddresses = publicIPAddresses;
	}

	public Integer getNoOfConcurrentChannels() {
		return noOfConcurrentChannels;
	}

	public void setNoOfConcurrentChannels(Integer noOfConcurrentChannels) {
		this.noOfConcurrentChannels = noOfConcurrentChannels;
	}

	public Integer getExistingConnectionId() {
		return existingConnectionId;
	}

	public void setExistingConnectionId(Integer existingConnectionId) {
		this.existingConnectionId = existingConnectionId;
	}
}
