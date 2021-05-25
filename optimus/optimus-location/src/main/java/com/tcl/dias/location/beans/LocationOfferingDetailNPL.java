package com.tcl.dias.location.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.beans.AddressDetail;

/**
 * This class used for multiple excel location upload
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
@JsonInclude(Include.NON_NULL)
public class LocationOfferingDetailNPL {

	private Integer locationIdSiteA;
	private Integer customerIdSiteA;
	private String popIdSiteA;
	private String tierSiteA;
	private AddressDetail userAddressSiteA;
	private AddressDetail apiAddressSiteA;
	private Integer erfCusCustomerLeIdSiteA;
	private AddressDetail addressSiteA;
	private String latLongSiteA;
	private String offeringNameSiteA;
	private String typeSiteA;
	
	private Integer locationIdSiteB;
	private Integer customerIdSiteB;
	private String popIdSiteB;
	private String tierSiteB;
	private AddressDetail userAddressSiteB;
	private AddressDetail apiAddressSiteB;
	private Integer erfCusCustomerLeIdSiteB;
	private AddressDetail addressSiteB;
	private String latLongSiteB;
	private String offeringNameSiteB;
	private String typeSiteB;
	public Integer getLocationIdSiteA() {
		return locationIdSiteA;
	}
	public void setLocationIdSiteA(Integer locationIdSiteA) {
		this.locationIdSiteA = locationIdSiteA;
	}
	public Integer getCustomerIdSiteA() {
		return customerIdSiteA;
	}
	public void setCustomerIdSiteA(Integer customerIdSiteA) {
		this.customerIdSiteA = customerIdSiteA;
	}
	public String getPopIdSiteA() {
		return popIdSiteA;
	}
	public void setPopIdSiteA(String popIdSiteA) {
		this.popIdSiteA = popIdSiteA;
	}
	public String getTierSiteA() {
		return tierSiteA;
	}
	public void setTierSiteA(String tierSiteA) {
		this.tierSiteA = tierSiteA;
	}
	public AddressDetail getUserAddressSiteA() {
		return userAddressSiteA;
	}
	public void setUserAddressSiteA(AddressDetail userAddressSiteA) {
		this.userAddressSiteA = userAddressSiteA;
	}
	public AddressDetail getApiAddressSiteA() {
		return apiAddressSiteA;
	}
	public void setApiAddressSiteA(AddressDetail apiAddressSiteA) {
		this.apiAddressSiteA = apiAddressSiteA;
	}
	public Integer getErfCusCustomerLeIdSiteA() {
		return erfCusCustomerLeIdSiteA;
	}
	public void setErfCusCustomerLeIdSiteA(Integer erfCusCustomerLeIdSiteA) {
		this.erfCusCustomerLeIdSiteA = erfCusCustomerLeIdSiteA;
	}
	public AddressDetail getAddressSiteA() {
		return addressSiteA;
	}
	public void setAddressSiteA(AddressDetail addressSiteA) {
		this.addressSiteA = addressSiteA;
	}
	public String getLatLongSiteA() {
		return latLongSiteA;
	}
	public void setLatLongSiteA(String latLongSiteA) {
		this.latLongSiteA = latLongSiteA;
	}
	public String getOfferingNameSiteA() {
		return offeringNameSiteA;
	}
	public void setOfferingNameSiteA(String offeringNameSiteA) {
		this.offeringNameSiteA = offeringNameSiteA;
	}
	public String getTypeSiteA() {
		return typeSiteA;
	}
	public void setTypeSiteA(String typeSiteA) {
		this.typeSiteA = typeSiteA;
	}
	public Integer getLocationIdSiteB() {
		return locationIdSiteB;
	}
	public void setLocationIdSiteB(Integer locationIdSiteB) {
		this.locationIdSiteB = locationIdSiteB;
	}
	public Integer getCustomerIdSiteB() {
		return customerIdSiteB;
	}
	public void setCustomerIdSiteB(Integer customerIdSiteB) {
		this.customerIdSiteB = customerIdSiteB;
	}
	public String getPopIdSiteB() {
		return popIdSiteB;
	}
	public void setPopIdSiteB(String popIdSiteB) {
		this.popIdSiteB = popIdSiteB;
	}
	public String getTierSiteB() {
		return tierSiteB;
	}
	public void setTierSiteB(String tierSiteB) {
		this.tierSiteB = tierSiteB;
	}
	public AddressDetail getUserAddressSiteB() {
		return userAddressSiteB;
	}
	public void setUserAddressSiteB(AddressDetail userAddressSiteB) {
		this.userAddressSiteB = userAddressSiteB;
	}
	public AddressDetail getApiAddressSiteB() {
		return apiAddressSiteB;
	}
	public void setApiAddressSiteB(AddressDetail apiAddressSiteB) {
		this.apiAddressSiteB = apiAddressSiteB;
	}
	public Integer getErfCusCustomerLeIdSiteB() {
		return erfCusCustomerLeIdSiteB;
	}
	public void setErfCusCustomerLeIdSiteB(Integer erfCusCustomerLeIdSiteB) {
		this.erfCusCustomerLeIdSiteB = erfCusCustomerLeIdSiteB;
	}
	public AddressDetail getAddressSiteB() {
		return addressSiteB;
	}
	public void setAddressSiteB(AddressDetail addressSiteB) {
		this.addressSiteB = addressSiteB;
	}
	public String getLatLongSiteB() {
		return latLongSiteB;
	}
	public void setLatLongSiteB(String latLongSiteB) {
		this.latLongSiteB = latLongSiteB;
	}
	public String getOfferingNameSiteB() {
		return offeringNameSiteB;
	}
	public void setOfferingNameSiteB(String offeringNameSiteB) {
		this.offeringNameSiteB = offeringNameSiteB;
	}
	public String getTypeSiteB() {
		return typeSiteB;
	}
	public void setTypeSiteB(String typeSiteB) {
		this.typeSiteB = typeSiteB;
	}
	@Override
	public String toString() {
		return "LocationOfferingDetailNPL [locationIdSiteA=" + locationIdSiteA + ", customerIdSiteA=" + customerIdSiteA
				+ ", popIdSiteA=" + popIdSiteA + ", tierSiteA=" + tierSiteA + ", userAddressSiteA=" + userAddressSiteA
				+ ", apiAddressSiteA=" + apiAddressSiteA + ", erfCusCustomerLeIdSiteA=" + erfCusCustomerLeIdSiteA
				+ ", addressSiteA=" + addressSiteA + ", latLongSiteA=" + latLongSiteA + ", offeringNameSiteA="
				+ offeringNameSiteA + ", typeSiteA=" + typeSiteA + ", locationIdSiteB=" + locationIdSiteB
				+ ", customerIdSiteB=" + customerIdSiteB + ", popIdSiteB=" + popIdSiteB + ", tierSiteB=" + tierSiteB
				+ ", userAddressSiteB=" + userAddressSiteB + ", apiAddressSiteB=" + apiAddressSiteB
				+ ", erfCusCustomerLeIdSiteB=" + erfCusCustomerLeIdSiteB + ", addressSiteB=" + addressSiteB
				+ ", latLongSiteB=" + latLongSiteB + ", offeringNameSiteB=" + offeringNameSiteB + ", typeSiteB="
				+ typeSiteB + "]";
	}
	
	
	
	
	
}
