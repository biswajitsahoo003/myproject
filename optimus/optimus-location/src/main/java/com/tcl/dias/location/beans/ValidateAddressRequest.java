package com.tcl.dias.location.beans;

import java.util.List;

/**
 * This class is used to hold the data needed for bulk upload of NPL sites
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ValidateAddressRequest {
	
	private String addressA;
	private	String countryA; 
	private String stateAArg;
	private String cityAArg; 
	private String pincodeA;
	private String localityA; 
	private boolean isIndiaA;
	private String typeA;
	private List<LocationOfferingDetailNPL> locationDetailList;
	private String offeringName;
	private String addressB;
	private	String countryB;
	private String stateBArg;
	private String cityBArg;
	private String pincodeB;
	private String localityB;
	private boolean isIndiaB;
	private String typeB;
	
	
	
	public String getAddressA() {
		return addressA;
	}
	public void setAddressA(String addressA) {
		this.addressA = addressA;
	}
	public String getCountryA() {
		return countryA;
	}
	public void setCountryA(String countryA) {
		this.countryA = countryA;
	}
	public String getStateAArg() {
		return stateAArg;
	}
	public void setStateAArg(String stateAArg) {
		this.stateAArg = stateAArg;
	}
	public String getCityAArg() {
		return cityAArg;
	}
	public void setCityAArg(String cityAArg) {
		this.cityAArg = cityAArg;
	}
	public String getPincodeA() {
		return pincodeA;
	}
	public void setPincodeA(String pincodeA) {
		this.pincodeA = pincodeA;
	}
	public String getLocalityA() {
		return localityA;
	}
	public void setLocalityA(String localityA) {
		this.localityA = localityA;
	}
	public boolean isIndiaA() {
		return isIndiaA;
	}
	public void setIndiaA(boolean isIndiaA) {
		this.isIndiaA = isIndiaA;
	}
	public String getTypeA() {
		return typeA;
	}
	public void setTypeA(String typeA) {
		this.typeA = typeA;
	}
	public List<LocationOfferingDetailNPL> getLocationDetailList() {
		return locationDetailList;
	}
	public void setLocationDetailList(List<LocationOfferingDetailNPL> locationDetailList) {
		this.locationDetailList = locationDetailList;
	}
	public String getOfferingName() {
		return offeringName;
	}
	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}
	public String getAddressB() {
		return addressB;
	}
	public void setAddressB(String addressB) {
		this.addressB = addressB;
	}
	public String getCountryB() {
		return countryB;
	}
	public void setCountryB(String countryB) {
		this.countryB = countryB;
	}
	public String getStateBArg() {
		return stateBArg;
	}
	public void setStateBArg(String stateBArg) {
		this.stateBArg = stateBArg;
	}
	public String getCityBArg() {
		return cityBArg;
	}
	public void setCityBArg(String cityBArg) {
		this.cityBArg = cityBArg;
	}
	public String getPincodeB() {
		return pincodeB;
	}
	public void setPincodeB(String pincodeB) {
		this.pincodeB = pincodeB;
	}
	public String getLocalityB() {
		return localityB;
	}
	public void setLocalityB(String localityB) {
		this.localityB = localityB;
	}
	public boolean isIndiaB() {
		return isIndiaB;
	}
	public void setIndiaB(boolean isIndiaB) {
		this.isIndiaB = isIndiaB;
	}
	public String getTypeB() {
		return typeB;
	}
	public void setTypeB(String typeB) {
		this.typeB = typeB;
	}
	@Override
	public String toString() {
		return "ValidateAddressRequest [addressA=" + addressA + ", countryA=" + countryA + ", stateAArg=" + stateAArg
				+ ", cityAArg=" + cityAArg + ", pincodeA=" + pincodeA + ", localityA=" + localityA + ", isIndiaA="
				+ isIndiaA + ", typeA=" + typeA + ", locationDetailList=" + locationDetailList + ", offeringName="
				+ offeringName + ", addressB=" + addressB + ", countryB=" + countryB + ", stateBArg=" + stateBArg
				+ ", cityBArg=" + cityBArg + ", pincodeB=" + pincodeB + ", localityB=" + localityB + ", isIndiaB="
				+ isIndiaB + ", typeB=" + typeB + ", getAddressA()=" + getAddressA() + ", getCountryA()="
				+ getCountryA() + ", getStateAArg()=" + getStateAArg() + ", getCityAArg()=" + getCityAArg()
				+ ", getPincodeA()=" + getPincodeA() + ", getLocalityA()=" + getLocalityA() + ", isIndiaA()="
				+ isIndiaA() + ", getTypeA()=" + getTypeA() + ", getLocationDetailList()=" + getLocationDetailList()
				+ ", getOfferingName()=" + getOfferingName() + ", getAddressB()=" + getAddressB() + ", getCountryB()="
				+ getCountryB() + ", getStateBArg()=" + getStateBArg() + ", getCityBArg()=" + getCityBArg()
				+ ", getPincodeB()=" + getPincodeB() + ", getLocalityB()=" + getLocalityB() + ", isIndiaB()="
				+ isIndiaB() + ", getTypeB()=" + getTypeB() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
	

}
