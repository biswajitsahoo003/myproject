package com.tcl.dias.common.servicefulfillment.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.constants.CommonConstants;

/**
 * 
 * This file contains the ServiceInfoBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class ServiceInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4870915106620359940L;
	private Integer serviceId;
	private String serviceCode;
	private String siteCode;
	private Double arc;
	private Double nrc;
	private Timestamp serviceCommisionDate;
	private String billingAccountId;
	private String bwPortSpeed;
	private String bwPortspeedAltName;
	private String bwUnit;
	private Timestamp createdTime;
	private String demarcationFloor;
	private String demarcationRack;
	private String demarcationRoom;
	private String demarcationBuilding;
	private String demarcationWing;
	private String popLocationId;
	private String locationId;
	private String siteAddress;
	private String sourcePincode;
	private String sourceLocality;
	private String sourceAddressLineOne;
	private String sourceAddressLineTwo;
	private String destinationPincode;
	private String destinationLocality;
	private String destinationAddressLineOne;
	private String destinationAddressLineTwo;
	private String destinationCountry;
	private String destinationCity;
	private String sourceCountry;
	private String sourceCity;
	private String sourceState;
	private String destinationState;
	private String city;
	private String popSiteAddress;
	private String copfId;
	private String feasibilityId;
	private String isTaxAvailable = CommonConstants.N;
	private String lastMileType;
	private String lastMileProvider;
	private String lastMileBw;
	private String lastMileBwUnit;
	private String siteEndInterface;
	private String serviceOption;
	private String localItContactName;
	private String localItContactEmailId;
	private String localItContactMobileNo;
	private Map<String, String> serviceDetailsAttributes;
	private Map<String, String> feasibilityAttributes;

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Timestamp getServiceCommisionDate() {
		return serviceCommisionDate;
	}

	public void setServiceCommisionDate(Timestamp serviceCommisionDate) {
		this.serviceCommisionDate = serviceCommisionDate;
	}

	public String getBillingAccountId() {
		return billingAccountId;
	}

	public void setBillingAccountId(String billingAccountId) {
		this.billingAccountId = billingAccountId;
	}

	public String getBwPortSpeed() {
		return bwPortSpeed;
	}

	public void setBwPortSpeed(String bwPortSpeed) {
		this.bwPortSpeed = bwPortSpeed;
	}

	public String getBwPortspeedAltName() {
		return bwPortspeedAltName;
	}

	public void setBwPortspeedAltName(String bwPortspeedAltName) {
		this.bwPortspeedAltName = bwPortspeedAltName;
	}

	public String getBwUnit() {
		return bwUnit;
	}

	public void setBwUnit(String bwUnit) {
		this.bwUnit = bwUnit;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getDemarcationFloor() {
		return demarcationFloor;
	}

	public void setDemarcationFloor(String demarcationFloor) {
		this.demarcationFloor = demarcationFloor;
	}

	public String getDemarcationRack() {
		return demarcationRack;
	}

	public void setDemarcationRack(String demarcationRack) {
		this.demarcationRack = demarcationRack;
	}

	public String getDemarcationRoom() {
		return demarcationRoom;
	}

	public void setDemarcationRoom(String demarcationRoom) {
		this.demarcationRoom = demarcationRoom;
	}

	public String getPopLocationId() {
		return popLocationId;
	}

	public void setPopLocationId(String popLocationId) {
		this.popLocationId = popLocationId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getPopSiteAddress() {
		return popSiteAddress;
	}

	public void setPopSiteAddress(String popSiteAddress) {
		this.popSiteAddress = popSiteAddress;
	}

	public String getCopfId() {
		return copfId;
	}

	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}

	public String getFeasibilityId() {
		return feasibilityId;
	}

	public void setFeasibilityId(String feasibilityId) {
		this.feasibilityId = feasibilityId;
	}

	public String getLastMileType() {
		return lastMileType;
	}

	public void setLastMileType(String lastMileType) {
		this.lastMileType = lastMileType;
	}

	public String getLastMileProvider() {
		return lastMileProvider;
	}

	public void setLastMileProvider(String lastMileProvider) {
		this.lastMileProvider = lastMileProvider;
	}

	public String getLastMileBw() {
		return lastMileBw;
	}

	public void setLastMileBw(String lastMileBw) {
		this.lastMileBw = lastMileBw;
	}

	public String getLastMileBwUnit() {
		return lastMileBwUnit;
	}

	public void setLastMileBwUnit(String lastMileBwUnit) {
		this.lastMileBwUnit = lastMileBwUnit;
	}

	public String getSiteEndInterface() {
		return siteEndInterface;
	}

	public void setSiteEndInterface(String siteEndInterface) {
		this.siteEndInterface = siteEndInterface;
	}

	public String getServiceOption() {
		return serviceOption;
	}

	public void setServiceOption(String serviceOption) {
		this.serviceOption = serviceOption;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public Map<String, String> getServiceDetailsAttributes() {
		return serviceDetailsAttributes;
	}

	public void setServiceDetailsAttributes(Map<String, String> serviceDetailsAttributes) {
		this.serviceDetailsAttributes = serviceDetailsAttributes;
	}

	public String getLocalItContactName() {
		return localItContactName;
	}

	public void setLocalItContactName(String localItContactName) {
		this.localItContactName = localItContactName;
	}

	public String getLocalItContactEmailId() {
		return localItContactEmailId;
	}

	public void setLocalItContactEmailId(String localItContactEmailId) {
		this.localItContactEmailId = localItContactEmailId;
	}

	public String getLocalItContactMobileNo() {
		return localItContactMobileNo;
	}

	public void setLocalItContactMobileNo(String localItContactMobileNo) {
		this.localItContactMobileNo = localItContactMobileNo;
	}

	public Map<String, String> getFeasibilityAttributes() {
		return feasibilityAttributes;
	}

	public void setFeasibilityAttributes(Map<String, String> feasibilityAttributes) {
		this.feasibilityAttributes = feasibilityAttributes;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDemarcationBuilding() {
		return demarcationBuilding;
	}

	public void setDemarcationBuilding(String demarcationBuilding) {
		this.demarcationBuilding = demarcationBuilding;
	}

	public String getDemarcationWing() {
		return demarcationWing;
	}

	public void setDemarcationWing(String demarcationWing) {
		this.demarcationWing = demarcationWing;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getIsTaxAvailable() {
		return isTaxAvailable;
	}

	public void setIsTaxAvailable(String isTaxAvailable) {
		this.isTaxAvailable = isTaxAvailable;
	}

	public String getSourcePincode() {
		return sourcePincode;
	}

	public void setSourcePincode(String sourcePincode) {
		this.sourcePincode = sourcePincode;
	}

	public String getSourceLocality() {
		return sourceLocality;
	}

	public void setSourceLocality(String sourceLocality) {
		this.sourceLocality = sourceLocality;
	}

	public String getSourceAddressLineOne() {
		return sourceAddressLineOne;
	}

	public void setSourceAddressLineOne(String sourceAddressLineOne) {
		this.sourceAddressLineOne = sourceAddressLineOne;
	}

	public String getSourceAddressLineTwo() {
		return sourceAddressLineTwo;
	}

	public void setSourceAddressLineTwo(String sourceAddressLineTwo) {
		this.sourceAddressLineTwo = sourceAddressLineTwo;
	}

	public String getDestinationPincode() {
		return destinationPincode;
	}

	public void setDestinationPincode(String destinationPincode) {
		this.destinationPincode = destinationPincode;
	}

	public String getDestinationLocality() {
		return destinationLocality;
	}

	public void setDestinationLocality(String destinationLocality) {
		this.destinationLocality = destinationLocality;
	}

	public String getDestinationAddressLineOne() {
		return destinationAddressLineOne;
	}

	public void setDestinationAddressLineOne(String destinationAddressLineOne) {
		this.destinationAddressLineOne = destinationAddressLineOne;
	}

	public String getDestinationAddressLineTwo() {
		return destinationAddressLineTwo;
	}

	public void setDestinationAddressLineTwo(String destinationAddressLineTwo) {
		this.destinationAddressLineTwo = destinationAddressLineTwo;
	}

	public String getDestinationCountry() {
		return destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getDestinationCity() {
		return destinationCity;
	}

	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}

	public String getSourceCountry() {
		return sourceCountry;
	}

	public void setSourceCountry(String sourceCountry) {
		this.sourceCountry = sourceCountry;
	}

	public String getSourceCity() {
		return sourceCity;
	}

	public void setSourceCity(String sourceCity) {
		this.sourceCity = sourceCity;
	}

	public String getSourceState() {
		return sourceState;
	}

	public void setSourceState(String sourceState) {
		this.sourceState = sourceState;
	}

	public String getDestinationState() {
		return destinationState;
	}

	public void setDestinationState(String destinationState) {
		this.destinationState = destinationState;
	}

	@Override
	public String toString() {
		return "ServiceInfoBean [serviceId=" + serviceId + ", siteCode=" + siteCode + ", arc=" + arc + ", nrc=" + nrc
				+ ", serviceCommisionDate=" + serviceCommisionDate + ", billingAccountId=" + billingAccountId
				+ ", bwPortSpeed=" + bwPortSpeed + ", bwPortspeedAltName=" + bwPortspeedAltName + ", bwUnit=" + bwUnit
				+ ", createdTime=" + createdTime + ", demarcationFloor=" + demarcationFloor + ", demarcationRack="
				+ demarcationRack + ", demarcationRoom=" + demarcationRoom + ", popLocationId=" + popLocationId
				+ ", locationId=" + locationId + ", siteAddress=" + siteAddress + ", popSiteAddress=" + popSiteAddress
				+ ", copfId=" + copfId + ", feasibilityId=" + feasibilityId + ", lastMileType=" + lastMileType
				+ ", lastMileProvider=" + lastMileProvider + ", lastMileBw=" + lastMileBw + ", lastMileBwUnit="
				+ lastMileBwUnit + ", siteEndInterface=" + siteEndInterface + ", serviceOption=" + serviceOption
				+ ", localItContactName=" + localItContactName + ", localItContactEmailId=" + localItContactEmailId
				+ ", localItContactMobileNo=" + localItContactMobileNo + ", serviceDetailsAttributes="
				+ serviceDetailsAttributes + "]";
	}

}
