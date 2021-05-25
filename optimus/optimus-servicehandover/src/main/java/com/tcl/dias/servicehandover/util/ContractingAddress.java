package com.tcl.dias.servicehandover.util;

public class ContractingAddress {

	String contractingAddressOne;
	String contractingAddressTwo;
	String contractingAddressThree;
	String contractingCity;
	String contractingState;
	String contractingCountry;
	String contractingPincode;

	public String getContractingAddressOne() {
		return contractingAddressOne;
	}

	public void setContractingAddressOne(String contractingAddressOne) {
		this.contractingAddressOne = contractingAddressOne;
	}

	public String getContractingAddressTwo() {
		return contractingAddressTwo;
	}

	public void setContractingAddressTwo(String contractingAddressTwo) {
		this.contractingAddressTwo = contractingAddressTwo;
	}

	public String getContractingAddressThree() {
		return contractingAddressThree;
	}

	public void setContractingAddressThree(String contractingAddressThree) {
		this.contractingAddressThree = contractingAddressThree;
	}

	public String getContractingCity() {
		return contractingCity;
	}

	public void setContractingCity(String contractingCity) {
		this.contractingCity = contractingCity;
	}

	public String getContractingState() {
		return contractingState;
	}

	public void setContractingState(String contractingState) {
		this.contractingState = contractingState;
	}

	public String getContractingCountry() {
		return contractingCountry;
	}

	public void setContractingCountry(String contractingCountry) {
		this.contractingCountry = contractingCountry;
	}

	public String getContractingPincode() {
		return contractingPincode;
	}

	public void setContractingPincode(String contractingPincode) {
		this.contractingPincode = contractingPincode;
	}

	@Override
	public String toString() {
		return "CONTRACTING_ADDR1=" + contractingAddressOne + ";CONTRACTING_ADDR2=" + contractingAddressTwo
				+ ";CONTRACTING_ADDR3=" + contractingAddressThree + ";CONTRACTING_CITY=" + contractingCity
				+ ";CONTRACTING_STATE=" + contractingState + ";CONTRACTING_COUNTRY=" + contractingCountry
				+ ";CONTRACTING_PINCODE=" + contractingPincode + ";";
	}

}
