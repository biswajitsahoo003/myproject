package com.tcl.dias.servicehandover.util;

public class ContractGstAddress {

	String contractGstinAddressOne;
	String contractGstinAddressTwo;
	String contractGstinAddressThree;
	String contractGstinAddressCity;
	String contractGstinAddressState;
	String contractGstinAddressCountry;
	String contractGstinAddressPincode;
	String contractGstNumber;
	public String getContractGstinAddressOne() {
		return contractGstinAddressOne;
	}

	public void setContractGstinAddressOne(String ContractGstinAddressOne) {
		this.contractGstinAddressOne = ContractGstinAddressOne;
	}

	public String getContractGstinAddressTwo() {
		return contractGstinAddressTwo;
	}

	public void setContractGstinAddressTwo(String ContractGstinAddressTwo) {
		this.contractGstinAddressTwo = ContractGstinAddressTwo;
	}

	public String getContractGstinAddressThree() {
		return contractGstinAddressThree;
	}

	public void setContractGstinAddressThree(String ContractGstinAddressThree) {
		this.contractGstinAddressThree = ContractGstinAddressThree;
	}

	public String getContractGstinAddressCity() {
		return contractGstinAddressCity;
	}

	public void setContractGstinAddressCity(String ContractGstinAddressCity) {
		this.contractGstinAddressCity = ContractGstinAddressCity;
	}

	public String getContractGstinAddressState() {
		return contractGstinAddressState;
	}

	public void setContractGstinAddressState(String ContractGstinAddressState) {
		this.contractGstinAddressState = ContractGstinAddressState;
	}

	public String getContractGstinAddressCountry() {
		return contractGstinAddressCountry;
	}

	public void setContractGstinAddressCountry(String ContractGstinAddressCountry) {
		this.contractGstinAddressCountry = ContractGstinAddressCountry;
	}

	public String getContractGstinAddressPincode() {
		return contractGstinAddressPincode;
	}

	public void setContractGstinAddressPincode(String ContractGstinAddressPincode) {
		this.contractGstinAddressPincode = ContractGstinAddressPincode;
	}

	public String getContractGstNumber() {
		return contractGstNumber;
	}

	public void setContractGstNumber(String contractGstNumber) {
		this.contractGstNumber = contractGstNumber;
	}

	@Override
	public String toString() {
		return "CONTRACT_GSTIN_ADDR1=" + contractGstinAddressOne + ";CONTRACT_GSTIN_ADDR2=" + contractGstinAddressTwo
				+ ";CONTRACT_GSTIN_ADDR3=" + contractGstinAddressThree + ";CONTRACT_GSTIN_CITY="
				+ contractGstinAddressCity + ";CONTRACT_GSTIN_STATE=" + contractGstinAddressState
				+ ";CONTRACT_GSTIN_COUNTRY=" + contractGstinAddressCountry + ";CONTRACT_GSTIN_PINCODE="
				+ contractGstinAddressPincode + ";";
	}

}
