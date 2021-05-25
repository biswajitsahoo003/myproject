package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 
 * This file contains the CustomeFeasibilityRequestNpl.java class.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomeFeasibilityRequestNpl {
	@JsonProperty("accessType_A")
	private String accessTypeA;
	@JsonProperty("feasibilityStatus_A")
	private String feasibilityStatusA;
	@JsonProperty("providerName_A")
	private String providerNameA;
	@JsonProperty("chargeableDistance_A")
	private String chargeableDistanceA;
	@JsonProperty("sfdcFeasibilityId_A")
	private String sfdcFeasibilityIdA;
	@JsonProperty("tclPop_A")
	private String tclPopA;
	@JsonProperty("tclPopId_A")
	private String tclPopIdA;
	@JsonProperty("connectedBuilding_A")
	private String connectedBuildingA;
	@JsonProperty("connectedCustomer_A")
	private String connectedCustomerA;
	@JsonProperty("arcBw_A")
	private String arcBwA;
	@JsonProperty("otc_A")
	private String otcA;
	@JsonProperty("inBuildingCapex_A")
	private String inBuildingCapexA;
	@JsonProperty("muxCost_A")
	private String muxCostA;
	@JsonProperty("neRental_A")
	private String neRentalA;
	@JsonProperty("ospCapex_A")
	private String ospCapexA;
	@JsonProperty("ospDistance_A")
	private String ospDistanceA;
	@JsonProperty("city_A")
	private String cityA;
	@JsonProperty("mhHhId_A")
	private String mhHhIdA;
	@JsonProperty("deliveryTimeline_A")
	private String deliveryTimelineA;
	@JsonProperty("bandwidth_A")
	private String bandwidthA;
	@JsonProperty("interfce_A")
	private String interfceA;
	@JsonProperty("mastHeight_A")
	private String mastHeightA;
	@JsonProperty("typeOfLm_A")
	private String typeOfLmA;
	@JsonProperty("p2pPresence_A")
	private String p2pPresenceA;
	@JsonProperty("pmpPresence_A")
	private String pmpPresenceA;
	@JsonProperty("hopDist_A")
	private String hopDistA;
	@JsonProperty("btsId_A")
	private String btsIdA;
	@JsonProperty("otcBw_A")
	private String otcBwA;
	@JsonProperty("mastCost_A")
	private String mastCostA;
	@JsonProperty("offnetBtsId_A")
	private String offnetBtsIdA;
	@JsonProperty("btsAddress_A")
	private String btsAddressA;
	@JsonProperty("tentativeMastHeight_A")
	private String tentativeMastHeightA;
	@JsonProperty("type_A")
	private String typeA;
	@JsonProperty("accessType_B")
	private String accessTypeB;
	@JsonProperty("feasibilityStatus_B")
	private String feasibilityStatusB;
	@JsonProperty("providerName_B")
	private String providerNameB;
	@JsonProperty("chargeableDistance_B")
	private String chargeableDistanceB;
	@JsonProperty("sfdcFeasibilityId_B")
	private String sfdcFeasibilityIdB;
	@JsonProperty("tclPop_B")
	private String tclPopB;
	@JsonProperty("tclPopId_B")
	private String tclPopIdB;
	@JsonProperty("connectedBuilding_B")
	private String connectedBuildingB;
	@JsonProperty("connectedCustomer_B")
	private String connectedCustomerB;
	@JsonProperty("arcBw_B")
	private String arcBwB;
	@JsonProperty("otc_B")
	private String otcB;
	@JsonProperty("inBuildingCapex_B")
	private String inBuildingCapexB;
	@JsonProperty("muxCost_B")
	private String muxCostB;
	@JsonProperty("neRental_B")
	private String neRentalB;
	@JsonProperty("ospCapex_B")
	private String ospCapexB;
	@JsonProperty("ospDistance_B")
	private String ospDistanceB;
	@JsonProperty("city_B")
	private String cityB;
	@JsonProperty("mhHhId_B")
	private String mhHhIdB;
	@JsonProperty("deliveryTimeline_B")
	private String deliveryTimelineB;
	@JsonProperty("bandwidth_B")
	private String bandwidthB;
	@JsonProperty("interfce_B")
	private String interfceB;
	@JsonProperty("mastHeight_B")
	private String mastHeightB;
	@JsonProperty("typeOfLm_B")
	private String typeOfLmB;
	@JsonProperty("p2pPresence_B")
	private String p2pPresenceB;
	@JsonProperty("pmpPresence_B")
	private String pmpPresenceB;
	@JsonProperty("hopDist_B")
	private String hopDistB;
	@JsonProperty("btsId_B")
	private String btsIdB;
	@JsonProperty("otcBw_B")
	private String otcBwB;
	@JsonProperty("mastCost_B")
	private String mastCostB;
	@JsonProperty("offnetBtsId_B")
	private String offnetBtsIdB;
	@JsonProperty("btsAddress_B")
	private String btsAddressB;
	@JsonProperty("tentativeMastHeight_B")
	private String tentativeMastHeightB;
	@JsonProperty("type_B")
	private String typeB;
	@JsonProperty("feasiblityRemarks_A")
	private String feasiblityRemarksA;
	@JsonProperty("feasiblityRemarks_B")
	private String feasiblityRemarksB;
	public String getFeasiblityRemarksA() {
		return feasiblityRemarksA;
	}
	public void setFeasiblityRemarksA(String feasiblityRemarksA) {
		this.feasiblityRemarksA = feasiblityRemarksA;
	}
	public String getFeasiblityRemarksB() {
		return feasiblityRemarksB;
	}
	public void setFeasiblityRemarksB(String feasiblityRemarksB) {
		this.feasiblityRemarksB = feasiblityRemarksB;
	}
	public String getAccessTypeA() {
		return accessTypeA;
	}
	public void setAccessTypeA(String accessTypeA) {
		this.accessTypeA = accessTypeA;
	}
	public String getFeasibilityStatusA() {
		return feasibilityStatusA;
	}
	public void setFeasibilityStatusA(String feasibilityStatusA) {
		this.feasibilityStatusA = feasibilityStatusA;
	}
	public String getProviderNameA() {
		return providerNameA;
	}
	public void setProviderNameA(String providerNameA) {
		this.providerNameA = providerNameA;
	}
	public String getChargeableDistanceA() {
		return chargeableDistanceA;
	}
	public void setChargeableDistanceA(String chargeableDistanceA) {
		this.chargeableDistanceA = chargeableDistanceA;
	}
	public String getSfdcFeasibilityIdA() {
		return sfdcFeasibilityIdA;
	}
	public void setSfdcFeasibilityIdA(String sfdcFeasibilityIdA) {
		this.sfdcFeasibilityIdA = sfdcFeasibilityIdA;
	}
	public String getTclPopA() {
		return tclPopA;
	}
	public void setTclPopA(String tclPopA) {
		this.tclPopA = tclPopA;
	}
	public String getTclPopIdA() {
		return tclPopIdA;
	}
	public void setTclPopIdA(String tclPopIdA) {
		this.tclPopIdA = tclPopIdA;
	}
	public String getConnectedBuildingA() {
		return connectedBuildingA;
	}
	public void setConnectedBuildingA(String connectedBuildingA) {
		this.connectedBuildingA = connectedBuildingA;
	}
	public String getConnectedCustomerA() {
		return connectedCustomerA;
	}
	public void setConnectedCustomerA(String connectedCustomerA) {
		this.connectedCustomerA = connectedCustomerA;
	}
	public String getArcBwA() {
		return arcBwA;
	}
	public void setArcBwA(String arcBwA) {
		this.arcBwA = arcBwA;
	}
	public String getOtcA() {
		return otcA;
	}
	public void setOtcA(String otcA) {
		this.otcA = otcA;
	}
	public String getInBuildingCapexA() {
		return inBuildingCapexA;
	}
	public void setInBuildingCapexA(String inBuildingCapexA) {
		this.inBuildingCapexA = inBuildingCapexA;
	}
	public String getMuxCostA() {
		return muxCostA;
	}
	public void setMuxCostA(String muxCostA) {
		this.muxCostA = muxCostA;
	}
	public String getNeRentalA() {
		return neRentalA;
	}
	public void setNeRentalA(String neRentalA) {
		this.neRentalA = neRentalA;
	}
	public String getOspCapexA() {
		return ospCapexA;
	}
	public void setOspCapexA(String ospCapexA) {
		this.ospCapexA = ospCapexA;
	}
	public String getOspDistanceA() {
		return ospDistanceA;
	}
	public void setOspDistanceA(String ospDistanceA) {
		this.ospDistanceA = ospDistanceA;
	}
	public String getCityA() {
		return cityA;
	}
	public void setCityA(String cityA) {
		this.cityA = cityA;
	}
	public String getMhHhIdA() {
		return mhHhIdA;
	}
	public void setMhHhIdA(String mhHhIdA) {
		this.mhHhIdA = mhHhIdA;
	}
	public String getDeliveryTimelineA() {
		return deliveryTimelineA;
	}
	public void setDeliveryTimelineA(String deliveryTimelineA) {
		this.deliveryTimelineA = deliveryTimelineA;
	}
	public String getBandwidthA() {
		return bandwidthA;
	}
	public void setBandwidthA(String bandwidthA) {
		this.bandwidthA = bandwidthA;
	}
	public String getInterfceA() {
		return interfceA;
	}
	public void setInterfceA(String interfceA) {
		this.interfceA = interfceA;
	}
	public String getMastHeightA() {
		return mastHeightA;
	}
	public void setMastHeightA(String mastHeightA) {
		this.mastHeightA = mastHeightA;
	}
	public String getTypeOfLmA() {
		return typeOfLmA;
	}
	public void setTypeOfLmA(String typeOfLmA) {
		this.typeOfLmA = typeOfLmA;
	}
	public String getP2pPresenceA() {
		return p2pPresenceA;
	}
	public void setP2pPresenceA(String p2pPresenceA) {
		this.p2pPresenceA = p2pPresenceA;
	}
	public String getPmpPresenceA() {
		return pmpPresenceA;
	}
	public void setPmpPresenceA(String pmpPresenceA) {
		this.pmpPresenceA = pmpPresenceA;
	}
	public String getHopDistA() {
		return hopDistA;
	}
	public void setHopDistA(String hopDistA) {
		this.hopDistA = hopDistA;
	}
	public String getBtsIdA() {
		return btsIdA;
	}
	public void setBtsIdA(String btsIdA) {
		this.btsIdA = btsIdA;
	}
	public String getOtcBwA() {
		return otcBwA;
	}
	public void setOtcBwA(String otcBwA) {
		this.otcBwA = otcBwA;
	}
	public String getMastCostA() {
		return mastCostA;
	}
	public void setMastCostA(String mastCostA) {
		this.mastCostA = mastCostA;
	}
	public String getOffnetBtsIdA() {
		return offnetBtsIdA;
	}
	public void setOffnetBtsIdA(String offnetBtsIdA) {
		this.offnetBtsIdA = offnetBtsIdA;
	}
	public String getBtsAddressA() {
		return btsAddressA;
	}
	public void setBtsAddressA(String btsAddressA) {
		this.btsAddressA = btsAddressA;
	}
	public String getTentativeMastHeightA() {
		return tentativeMastHeightA;
	}
	public void setTentativeMastHeightA(String tentativeMastHeightA) {
		this.tentativeMastHeightA = tentativeMastHeightA;
	}
	public String getTypeA() {
		return typeA;
	}
	public void setTypeA(String typeA) {
		this.typeA = typeA;
	}
	public String getAccessTypeB() {
		return accessTypeB;
	}
	public void setAccessTypeB(String accessTypeB) {
		this.accessTypeB = accessTypeB;
	}
	public String getFeasibilityStatusB() {
		return feasibilityStatusB;
	}
	public void setFeasibilityStatusB(String feasibilityStatusB) {
		this.feasibilityStatusB = feasibilityStatusB;
	}
	public String getProviderNameB() {
		return providerNameB;
	}
	public void setProviderNameB(String providerNameB) {
		this.providerNameB = providerNameB;
	}
	public String getChargeableDistanceB() {
		return chargeableDistanceB;
	}
	public void setChargeableDistanceB(String chargeableDistanceB) {
		this.chargeableDistanceB = chargeableDistanceB;
	}
	public String getSfdcFeasibilityIdB() {
		return sfdcFeasibilityIdB;
	}
	public void setSfdcFeasibilityIdB(String sfdcFeasibilityIdB) {
		this.sfdcFeasibilityIdB = sfdcFeasibilityIdB;
	}
	public String getTclPopB() {
		return tclPopB;
	}
	public void setTclPopB(String tclPopB) {
		this.tclPopB = tclPopB;
	}
	public String getTclPopIdB() {
		return tclPopIdB;
	}
	public void setTclPopIdB(String tclPopIdB) {
		this.tclPopIdB = tclPopIdB;
	}
	public String getConnectedBuildingB() {
		return connectedBuildingB;
	}
	public void setConnectedBuildingB(String connectedBuildingB) {
		this.connectedBuildingB = connectedBuildingB;
	}
	public String getConnectedCustomerB() {
		return connectedCustomerB;
	}
	public void setConnectedCustomerB(String connectedCustomerB) {
		this.connectedCustomerB = connectedCustomerB;
	}
	public String getArcBwB() {
		return arcBwB;
	}
	public void setArcBwB(String arcBwB) {
		this.arcBwB = arcBwB;
	}
	public String getOtcB() {
		return otcB;
	}
	public void setOtcB(String otcB) {
		this.otcB = otcB;
	}
	public String getInBuildingCapexB() {
		return inBuildingCapexB;
	}
	public void setInBuildingCapexB(String inBuildingCapexB) {
		this.inBuildingCapexB = inBuildingCapexB;
	}
	public String getMuxCostB() {
		return muxCostB;
	}
	public void setMuxCostB(String muxCostB) {
		this.muxCostB = muxCostB;
	}
	public String getNeRentalB() {
		return neRentalB;
	}
	public void setNeRentalB(String neRentalB) {
		this.neRentalB = neRentalB;
	}
	public String getOspCapexB() {
		return ospCapexB;
	}
	public void setOspCapexB(String ospCapexB) {
		this.ospCapexB = ospCapexB;
	}
	public String getOspDistanceB() {
		return ospDistanceB;
	}
	public void setOspDistanceB(String ospDistanceB) {
		this.ospDistanceB = ospDistanceB;
	}
	public String getCityB() {
		return cityB;
	}
	public void setCityB(String cityB) {
		this.cityB = cityB;
	}
	public String getMhHhIdB() {
		return mhHhIdB;
	}
	public void setMhHhIdB(String mhHhIdB) {
		this.mhHhIdB = mhHhIdB;
	}
	public String getDeliveryTimelineB() {
		return deliveryTimelineB;
	}
	public void setDeliveryTimelineB(String deliveryTimelineB) {
		this.deliveryTimelineB = deliveryTimelineB;
	}
	public String getBandwidthB() {
		return bandwidthB;
	}
	public void setBandwidthB(String bandwidthB) {
		this.bandwidthB = bandwidthB;
	}
	public String getInterfceB() {
		return interfceB;
	}
	public void setInterfceB(String interfceB) {
		this.interfceB = interfceB;
	}
	public String getMastHeightB() {
		return mastHeightB;
	}
	public void setMastHeightB(String mastHeightB) {
		this.mastHeightB = mastHeightB;
	}
	public String getTypeOfLmB() {
		return typeOfLmB;
	}
	public void setTypeOfLmB(String typeOfLmB) {
		this.typeOfLmB = typeOfLmB;
	}
	public String getP2pPresenceB() {
		return p2pPresenceB;
	}
	public void setP2pPresenceB(String p2pPresenceB) {
		this.p2pPresenceB = p2pPresenceB;
	}
	public String getPmpPresenceB() {
		return pmpPresenceB;
	}
	public void setPmpPresenceB(String pmpPresenceB) {
		this.pmpPresenceB = pmpPresenceB;
	}
	public String getHopDistB() {
		return hopDistB;
	}
	public void setHopDistB(String hopDistB) {
		this.hopDistB = hopDistB;
	}
	public String getBtsIdB() {
		return btsIdB;
	}
	public void setBtsIdB(String btsIdB) {
		this.btsIdB = btsIdB;
	}
	public String getOtcBwB() {
		return otcBwB;
	}
	public void setOtcBwB(String otcBwB) {
		this.otcBwB = otcBwB;
	}
	public String getMastCostB() {
		return mastCostB;
	}
	public void setMastCostB(String mastCostB) {
		this.mastCostB = mastCostB;
	}
	public String getOffnetBtsIdB() {
		return offnetBtsIdB;
	}
	public void setOffnetBtsIdB(String offnetBtsIdB) {
		this.offnetBtsIdB = offnetBtsIdB;
	}
	public String getBtsAddressB() {
		return btsAddressB;
	}
	public void setBtsAddressB(String btsAddressB) {
		this.btsAddressB = btsAddressB;
	}
	public String getTentativeMastHeightB() {
		return tentativeMastHeightB;
	}
	public void setTentativeMastHeightB(String tentativeMastHeightB) {
		this.tentativeMastHeightB = tentativeMastHeightB;
	}
	public String getTypeB() {
		return typeB;
	}
	public void setTypeB(String typeB) {
		this.typeB = typeB;
	}
	
	
}
