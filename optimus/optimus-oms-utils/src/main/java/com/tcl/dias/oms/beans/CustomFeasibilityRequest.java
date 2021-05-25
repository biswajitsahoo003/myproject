package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This file contains the CustomFeasibilityRequest.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFeasibilityRequest {

	private String accessType;
	private String feasibilityStatus;
	private String providerName;
	private String chargeableDistance;
	private String sfdcFeasibilityId;
	private String tclPop;
	private String tclPopId;
	private String connectedBuilding;
	private String connectedCustomer;
	private String arcBw;
	private String otc;
	private String inBuildingCapex;
	private String muxCost;
	private String neRental;
	private String ospCapex;
	private String ospDistance;
	private String city;
	private String mhHhId;
	private String deliveryTimeline;
	private String bandwidth;
	private String interfce;
	private String mastHeight;
	private String typeOfLm;
	private String p2pPresence;
	private String pmpPresence;
	private String hopDist;
	private String btsId;
	private String otcBw;
	private String mastCost;
	private String offnetBtsId;
	private String btsAddress;
	private String tentativeMastHeight;
	private String type;

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getFeasibilityStatus() {
		return feasibilityStatus;
	}

	public void setFeasibilityStatus(String feasibilityStatus) {
		this.feasibilityStatus = feasibilityStatus;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getChargeableDistance() {
		return chargeableDistance;
	}

	public void setChargeableDistance(String chargeableDistance) {
		this.chargeableDistance = chargeableDistance;
	}

	public String getTclPop() {
		return tclPop;
	}

	public void setTclPop(String tclPop) {
		this.tclPop = tclPop;
	}

	public String getTclPopId() {
		return tclPopId;
	}

	public void setTclPopId(String tclPopId) {
		this.tclPopId = tclPopId;
	}

	public String getConnectedBuilding() {
		return connectedBuilding;
	}

	public void setConnectedBuilding(String connectedBuilding) {
		this.connectedBuilding = connectedBuilding;
	}

	public String getConnectedCustomer() {
		return connectedCustomer;
	}

	public void setConnectedCustomer(String connectedCustomer) {
		this.connectedCustomer = connectedCustomer;
	}

	public String getArcBw() {
		return arcBw;
	}

	public void setArcBw(String arcBw) {
		this.arcBw = arcBw;
	}

	public String getOtc() {
		return otc;
	}

	public void setOtc(String otc) {
		this.otc = otc;
	}

	public String getInBuildingCapex() {
		return inBuildingCapex;
	}

	public void setInBuildingCapex(String inBuildingCapex) {
		this.inBuildingCapex = inBuildingCapex;
	}

	public String getMuxCost() {
		return muxCost;
	}

	public void setMuxCost(String muxCost) {
		this.muxCost = muxCost;
	}

	public String getNeRental() {
		return neRental;
	}

	public void setNeRental(String neRental) {
		this.neRental = neRental;
	}

	public String getOspCapex() {
		return ospCapex;
	}

	public void setOspCapex(String ospCapex) {
		this.ospCapex = ospCapex;
	}

	public String getOspDistance() {
		return ospDistance;
	}

	public void setOspDistance(String ospDistance) {
		this.ospDistance = ospDistance;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMhHhId() {
		return mhHhId;
	}

	public void setMhHhId(String mhHhId) {
		this.mhHhId = mhHhId;
	}

	public String getDeliveryTimeline() {
		return deliveryTimeline;
	}

	public void setDeliveryTimeline(String deliveryTimeline) {
		this.deliveryTimeline = deliveryTimeline;
	}

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getInterfce() {
		return interfce;
	}

	public void setInterfce(String interfce) {
		this.interfce = interfce;
	}

	public String getMastHeight() {
		return mastHeight;
	}

	public void setMastHeight(String mastHeight) {
		this.mastHeight = mastHeight;
	}

	public String getTypeOfLm() {
		return typeOfLm;
	}

	public void setTypeOfLm(String typeOfLm) {
		this.typeOfLm = typeOfLm;
	}

	public String getP2pPresence() {
		return p2pPresence;
	}

	public void setP2pPresence(String p2pPresence) {
		this.p2pPresence = p2pPresence;
	}

	public String getPmpPresence() {
		return pmpPresence;
	}

	public void setPmpPresence(String pmpPresence) {
		this.pmpPresence = pmpPresence;
	}

	public String getHopDist() {
		return hopDist;
	}

	public void setHopDist(String hopDist) {
		this.hopDist = hopDist;
	}

	public String getBtsId() {
		return btsId;
	}

	public void setBtsId(String btsId) {
		this.btsId = btsId;
	}

	public String getOtcBw() {
		return otcBw;
	}

	public void setOtcBw(String otcBw) {
		this.otcBw = otcBw;
	}

	public String getMastCost() {
		return mastCost;
	}

	public void setMastCost(String mastCost) {
		this.mastCost = mastCost;
	}

	public String getOffnetBtsId() {
		return offnetBtsId;
	}

	public void setOffnetBtsId(String offnetBtsId) {
		this.offnetBtsId = offnetBtsId;
	}

	public String getBtsAddress() {
		return btsAddress;
	}

	public void setBtsAddress(String btsAddress) {
		this.btsAddress = btsAddress;
	}

	public String getTentativeMastHeight() {
		return tentativeMastHeight;
	}

	public void setTentativeMastHeight(String tentativeMastHeight) {
		this.tentativeMastHeight = tentativeMastHeight;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSfdcFeasibilityId() {
		return sfdcFeasibilityId;
	}

	public void setSfdcFeasibilityId(String sfdcFeasibilityId) {
		this.sfdcFeasibilityId = sfdcFeasibilityId;
	}

	@Override
	public String toString() {
		return "CustomFeasibilityRequest [accessType=" + accessType + ", FeasibilityStatus=" + feasibilityStatus
				+ ", providerName=" + providerName + ", chargeableDistance=" + chargeableDistance + ", tclPop=" + tclPop
				+ ", tclPopId=" + tclPopId + ", connectedBuilding=" + connectedBuilding + ", connectedCustomer="
				+ connectedCustomer + ", arcBw=" + arcBw + ", otc=" + otc + ", inBuildingCapex=" + inBuildingCapex
				+ ", muxCost=" + muxCost + ", neRental=" + neRental + ", ospCapex=" + ospCapex + ", ospDistance="
				+ ospDistance + ", city=" + city + ", mhHhId=" + mhHhId + ", deliveryTimeline=" + deliveryTimeline
				+ ", bandwidth=" + bandwidth + ", interfce=" + interfce + ", mastHeight=" + mastHeight + ", typeOfLm="
				+ typeOfLm + ", p2pPresence=" + p2pPresence + ", pmpPresence=" + pmpPresence + ", hopDist=" + hopDist
				+ ", btsId=" + btsId + ", otcBw=" + otcBw + ", mastCost=" + mastCost + ", offnetBtsId=" + offnetBtsId
				+ ", btsAddress=" + btsAddress + ", tentativeMastHeight=" + tentativeMastHeight + "]";
	}

}
