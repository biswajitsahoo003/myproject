package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({

		// information
		"feasibility_response_id", "record_type", "provider_name", "response_related_to",
		"Predicted_Access_Feasibility", "access_feasibility_category", "delivery_timeLine", "validity_period",
		"addressOfAEnd", "local_loop_bw", "local_loop_interface", "POP_DIST_KM_SERVICE_MOD", "feasibility_remarks",

		// commercials
		"lm_nrc_bw_onwl", "gpon_operator_name", "prow_cpst_type", "prow_value_otc", "lm_arc_prow_onwl",
		"lm_nrc_nerental_onwl", "lm_arc_bw_onwl",

		// capex
		"network_capex", "min_hh_fatg", "hh_name", "lm_nrc_ospcapex_onwl", "capex_in_building", "lm_nrc_mux_onwl",

		// pop
		"pop_name", "pop_network_loc_id", "POP_Network_Location_Type", "pop_address", "pop_city", "pop_state",
		"pop_country"

})

/**
 * @author krutsrin
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class FRAManResponse {

	@JsonProperty("feasibility_response_id")
	private String fraId;

	@JsonProperty("record_type")
	private String recordType;

	private String createdbyOrUpdatedby;
	
	private String createdtimeOrUpdatedtime;

	@JsonProperty("provider_name")
	private String providerName;

	@JsonProperty("response_related_to")
	private String responseRelatedTo;

	@JsonProperty("Predicted_Access_Feasibility")
	private String feasibilityStatus;

	@JsonProperty("access_feasibility_category")
	private String feasibilityStatusCategory;

	@JsonProperty("delivery_timeLine")
	private String deliveryTimeLine;

	@JsonProperty("validity_period")
	private String validityPeriod;

	@JsonProperty("addressOfAEnd")
	private String addressAEnd;

	@JsonProperty("local_loop_bw")
	private String localLoopBw;

	@JsonProperty("local_loop_interface")
	private String localLoopInterface;

	@JsonProperty("POP_DIST_KM_SERVICE_MOD")
	private String popDistanceInKmServiceMod;

	@JsonProperty("feasibility_remarks")
	private String feasibilityRemarks;

	// Commercials

	@JsonProperty("lm_nrc_bw_onwl")
	private String otcOrNrcInstallation;

	@JsonProperty("gpon_operator_name")
	private String gponOperatorName;

	@JsonProperty("prow_cpst_type")
	private String prowCostType;

	@JsonProperty("lm_nrc_prow_onwl")
	private String provValueotc;

	@JsonProperty("lm_arc_prow_onwl")
	private String provValueArc;

	@JsonProperty("lm_nrc_nerental_onwl")
	private String arcLrcOrNeRental;

	@JsonProperty("lm_arc_bw_onwl")
	private String arcBw;

	// capex

	@JsonProperty("network_capex")
	private String networkCapex;

	@JsonProperty("min_hh_fatg")
	private String ospDistanceInMetersForAEnd;

	@JsonProperty("hh_name")
	private String hhNameForAEnd;

	@JsonProperty("lm_nrc_ospcapex_onwl")
	private String capexOspForAEnd;

	@JsonProperty("capex_in_building")
	private String capexInBuildingForAEnd;

	@JsonProperty("lm_nrc_mux_onwl")
	private String capexEquipmentsForAEnd;

	// pop

	@JsonProperty("pop_name")
	private String popName;

	@JsonProperty("pop_network_loc_id")
	private String uniquePopId;

	@JsonProperty("POP_Network_Location_Type")
	private String serviceType;

	@JsonProperty("pop_address")
	private String popAddress;

	@JsonProperty("pop_city")
	private String popCity;

	@JsonProperty("pop_state")
	private String popState;

	@JsonProperty("pop_country")
	private String popCountry;
	
	
	public String getCreatedbyOrUpdatedby() {
		return createdbyOrUpdatedby;
	}

	public void setCreatedbyOrUpdatedby(String createdbyOrUpdatedby) {
		this.createdbyOrUpdatedby = createdbyOrUpdatedby;
	}

	public String getCreatedtimeOrUpdatedtime() {
		return createdtimeOrUpdatedtime;
	}

	public void setCreatedtimeOrUpdatedtime(String createdtimeOrUpdatedtime) {
		this.createdtimeOrUpdatedtime = createdtimeOrUpdatedtime;
	}

	public String getFraId() {
		return fraId;
	}

	public void setFraId(String fraId) {
		this.fraId = fraId;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getResponseRelatedTo() {
		return responseRelatedTo;
	}

	public void setResponseRelatedTo(String responseRelatedTo) {
		this.responseRelatedTo = responseRelatedTo;
	}

	public String getFeasibilityStatus() {
		return feasibilityStatus;
	}

	public void setFeasibilityStatus(String feasibilityStatus) {
		this.feasibilityStatus = feasibilityStatus;
	}

	public String getFeasibilityStatusCategory() {
		return feasibilityStatusCategory;
	}

	public void setFeasibilityStatusCategory(String feasibilityStatusCategory) {
		this.feasibilityStatusCategory = feasibilityStatusCategory;
	}

	public String getDeliveryTimeLine() {
		return deliveryTimeLine;
	}

	public void setDeliveryTimeLine(String deliveryTimeLine) {
		this.deliveryTimeLine = deliveryTimeLine;
	}

	public String getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(String validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public String getAddressAEnd() {
		return addressAEnd;
	}

	public void setAddressAEnd(String addressAEnd) {
		this.addressAEnd = addressAEnd;
	}

	public String getLocalLoopBw() {
		return localLoopBw;
	}

	public void setLocalLoopBw(String localLoopBw) {
		this.localLoopBw = localLoopBw;
	}

	public String getLocalLoopInterface() {
		return localLoopInterface;
	}

	public void setLocalLoopInterface(String localLoopInterface) {
		this.localLoopInterface = localLoopInterface;
	}

	public String getPopDistanceInKmServiceMod() {
		return popDistanceInKmServiceMod;
	}

	public void setPopDistanceInKmServiceMod(String popDistanceInKmServiceMod) {
		this.popDistanceInKmServiceMod = popDistanceInKmServiceMod;
	}

	public String getFeasibilityRemarks() {
		return feasibilityRemarks;
	}

	public void setFeasibilityRemarks(String feasibilityRemarks) {
		this.feasibilityRemarks = feasibilityRemarks;
	}

	public String getOtcOrNrcInstallation() {
		return otcOrNrcInstallation;
	}

	public void setOtcOrNrcInstallation(String otcOrNrcInstallation) {
		this.otcOrNrcInstallation = otcOrNrcInstallation;
	}


	public String getGponOperatorName() {
		return gponOperatorName;
	}

	public void setGponOperatorName(String gponOperatorName) {
		this.gponOperatorName = gponOperatorName;
	}

	public String getProwCostType() {
		return prowCostType;
	}

	public void setProwCostType(String prowCostType) {
		this.prowCostType = prowCostType;
	}

	public String getProvValueotc() {
		return provValueotc;
	}

	public void setProvValueotc(String provValueotc) {
		this.provValueotc = provValueotc;
	}

	public String getProvValueArc() {
		return provValueArc;
	}

	public void setProvValueArc(String provValueArc) {
		this.provValueArc = provValueArc;
	}

	public String getArcLrcOrNeRental() {
		return arcLrcOrNeRental;
	}

	public void setArcLrcOrNeRental(String arcLrcOrNeRental) {
		this.arcLrcOrNeRental = arcLrcOrNeRental;
	}

	public String getArcBw() {
		return arcBw;
	}

	public void setArcBw(String arcBw) {
		this.arcBw = arcBw;
	}

	public String getNetworkCapex() {
		return networkCapex;
	}

	public void setNetworkCapex(String networkCapex) {
		this.networkCapex = networkCapex;
	}

	public String getOspDistanceInMetersForAEnd() {
		return ospDistanceInMetersForAEnd;
	}

	public void setOspDistanceInMetersForAEnd(String ospDistanceInMetersForAEnd) {
		this.ospDistanceInMetersForAEnd = ospDistanceInMetersForAEnd;
	}

	public String getHhNameForAEnd() {
		return hhNameForAEnd;
	}

	public void setHhNameForAEnd(String hhNameForAEnd) {
		this.hhNameForAEnd = hhNameForAEnd;
	}

	public String getCapexOspForAEnd() {
		return capexOspForAEnd;
	}

	public void setCapexOspForAEnd(String capexOspForAEnd) {
		this.capexOspForAEnd = capexOspForAEnd;
	}

	public String getCapexInBuildingForAEnd() {
		return capexInBuildingForAEnd;
	}

	public void setCapexInBuildingForAEnd(String capexInBuildingForAEnd) {
		this.capexInBuildingForAEnd = capexInBuildingForAEnd;
	}

	public String getCapexEquipmentsForAEnd() {
		return capexEquipmentsForAEnd;
	}

	public void setCapexEquipmentsForAEnd(String capexEquipmentsForAEnd) {
		this.capexEquipmentsForAEnd = capexEquipmentsForAEnd;
	}

	public String getPopName() {
		return popName;
	}

	public void setPopName(String popName) {
		this.popName = popName;
	}

	public String getUniquePopId() {
		return uniquePopId;
	}

	public void setUniquePopId(String uniquePopId) {
		this.uniquePopId = uniquePopId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getPopAddress() {
		return popAddress;
	}

	public void setPopAddress(String popAddress) {
		this.popAddress = popAddress;
	}

	public String getPopCity() {
		return popCity;
	}

	public void setPopCity(String popCity) {
		this.popCity = popCity;
	}

	public String getPopState() {
		return popState;
	}

	public void setPopState(String popState) {
		this.popState = popState;
	}

	public String getPopCountry() {
		return popCountry;
	}

	public void setPopCountry(String popCountry) {
		this.popCountry = popCountry;
	}

}
