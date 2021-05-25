package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({

		"feasibility_response_id", "record_type", "provider", "response_related_to", "Predicted_Access_Feasibility",
		"access_feasibility_category", "delivery_timeLine", "validity_period", "addressOfAEnd", "local_loop_bw",
		"local_loop_interface", "l2_report", "feasibility_remarks",

		// commercials
		"lm_nrc_bw_onrf", // orc nrc
		"lm_nrc_mast_onrf", // mast
		"lm_arc_converter_charges_onrf", // arc convertor charges
		"lm_arc_bw_backhaul_onrf", // arc-bw

		// RF Response
		"l2_vendor_name", "survey_completed_date", "survey_for", "latitude_final", "longitude_final",
		"primary_bts_name",

		"bts_num_BTS", // primary BTS id
		"primary_bts_ip_address", // primary BTS ip address
		"primary_first_sector_id",

		"bts_lat", "bts_long", "bts_site_address", "bts_city",

		"customer_building_height", "additional_no_of_link", "cable_dist_bw_mast_pole_loc_server_rm",
		"Mast_3KM_avg_mast_ht", // poleMast height
		"antenna_type", "obstruction_ht", "obstruction_latitude", "obstruction_longitude"

})


/**
 * @author krutsrin
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class FRAUbrResponse {

	@JsonProperty("feasibility_response_id")
	private String fraId;
	
	 private String createdbyOrUpdatedby;
		
	private String createdtimeOrUpdatedtime;

	@JsonProperty("record_type")
	private String recordType;

	@JsonProperty("provider_name")
	private String provider;

//	@JsonProperty("provider_reference_number")
//	private String providerRefNo;

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

	@JsonProperty("l2_report")
	private String l2Report;

	@JsonProperty("feasibility_remarks")
	private String feasibilityRemarks;

	// commercials

	@JsonProperty("lm_nrc_bw_onrf")
	private String orcOrNrcInstallation;

	@JsonProperty("lm_nrc_mast_onrf")
	private String mastCharges;

	@JsonProperty("lm_arc_converter_charges_onrf")
	private String arcConverterCharges;

	@JsonProperty("lm_arc_bw_backhaul_onrf")
	private String arcBw;

	// Response
	@JsonProperty("l2_vendor_name")
	private String l2VendorName;

	@JsonProperty("survey_completed_date")
	private String surveyCompletedDate;

	@JsonProperty("survey_for")
	private String surveyFor;

	@JsonProperty("latitude_final")
	private String latitudeFinal;

	@JsonProperty("longitude_final")
	private String longitudeFinal;

	@JsonProperty("primary_bts_name")
	private String primaryBtsName;

	@JsonProperty("bts_num_BTS")
	private String primaryBtsID;

	@JsonProperty("primary_bts_ip_address")
	private String primaryBtsIpAddress;

	@JsonProperty("primary_first_sector_id")
	private String primaryFirstSectorId;

	@JsonProperty("bts_lat")
	private String btsLatitude;

	@JsonProperty("bts_long")
	private String btsLongitude;

	@JsonProperty("bts_site_address")
	private String btsSiteAddress;

	@JsonProperty("bts_city")
	private String btsCity;

	@JsonProperty("customer_building_height")
	private String customerBuildingHeightInMeters;

	@JsonProperty("additional_no_of_link")
	private String additionalNumberOfLinks;

	@JsonProperty("cable_dist_bw_mast_pole_loc_server_rm")
	private String cableDistanceBetweenMastOrPole;

	@JsonProperty("Mast_3KM_avg_mast_ht")
	private String mastHeight;

	@JsonProperty("antenna_type")
	private String antennaType;

	@JsonProperty("obstruction_ht")
	private String obstructionHeight;

	@JsonProperty("obstruction_latitude")
	private String obstructionLatitude;

	@JsonProperty("obstruction_longitude")
	private String obstructionLongitude;

	public String getFraId() {
		return fraId;
	}
	
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

	public void setFraId(String fraId) {
		this.fraId = fraId;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	/*public String getProviderRefNo() {
		return providerRefNo;
	}

	public void setProviderRefNo(String providerRefNo) {
		this.providerRefNo = providerRefNo;
	} */

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

	public String getL2Report() {
		return l2Report;
	}

	public void setL2Report(String l2Report) {
		this.l2Report = l2Report;
	}

	public String getFeasibilityRemarks() {
		return feasibilityRemarks;
	}

	public void setFeasibilityRemarks(String feasibilityRemarks) {
		this.feasibilityRemarks = feasibilityRemarks;
	}

	public String getOrcOrNrcInstallation() {
		return orcOrNrcInstallation;
	}

	public void setOrcOrNrcInstallation(String orcOrNrcInstallation) {
		this.orcOrNrcInstallation = orcOrNrcInstallation;
	}

	public String getArcConverterCharges() {
		return arcConverterCharges;
	}

	public void setArcConverterCharges(String arcConverterCharges) {
		this.arcConverterCharges = arcConverterCharges;
	}

	public String getMastCharges() {
		return mastCharges;
	}

	public void setMastCharges(String mastCharges) {
		this.mastCharges = mastCharges;
	}

	
	public String getArcBw() {
		return arcBw;
	}

	public void setArcBw(String arcBw) {
		this.arcBw = arcBw;
	}

	public String getL2VendorName() {
		return l2VendorName;
	}

	public void setL2VendorName(String l2VendorName) {
		this.l2VendorName = l2VendorName;
	}

	public String getSurveyCompletedDate() {
		return surveyCompletedDate;
	}

	public void setSurveyCompletedDate(String surveyCompletedDate) {
		this.surveyCompletedDate = surveyCompletedDate;
	}

	public String getSurveyFor() {
		return surveyFor;
	}

	public void setSurveyFor(String surveyFor) {
		this.surveyFor = surveyFor;
	}

	public String getLatitudeFinal() {
		return latitudeFinal;
	}

	public void setLatitudeFinal(String latitudeFinal) {
		this.latitudeFinal = latitudeFinal;
	}

	public String getLongitudeFinal() {
		return longitudeFinal;
	}

	public void setLongitudeFinal(String longitudeFinal) {
		this.longitudeFinal = longitudeFinal;
	}

	public String getPrimaryBtsName() {
		return primaryBtsName;
	}

	public void setPrimaryBtsName(String primaryBtsName) {
		this.primaryBtsName = primaryBtsName;
	}

	public String getPrimaryBtsID() {
		return primaryBtsID;
	}

	public void setPrimaryBtsID(String primaryBtsID) {
		this.primaryBtsID = primaryBtsID;
	}

	public String getPrimaryBtsIpAddress() {
		return primaryBtsIpAddress;
	}

	public void setPrimaryBtsIpAddress(String primaryBtsIpAddress) {
		this.primaryBtsIpAddress = primaryBtsIpAddress;
	}

	public String getPrimaryFirstSectorId() {
		return primaryFirstSectorId;
	}

	public void setPrimaryFirstSectorId(String primaryFirstSectorId) {
		this.primaryFirstSectorId = primaryFirstSectorId;
	}

	public String getBtsLatitude() {
		return btsLatitude;
	}

	public void setBtsLatitude(String btsLatitude) {
		this.btsLatitude = btsLatitude;
	}

	public String getBtsLongitude() {
		return btsLongitude;
	}

	public void setBtsLongitude(String btsLongitude) {
		this.btsLongitude = btsLongitude;
	}

	public String getBtsSiteAddress() {
		return btsSiteAddress;
	}

	public void setBtsSiteAddress(String btsSiteAddress) {
		this.btsSiteAddress = btsSiteAddress;
	}

	public String getBtsCity() {
		return btsCity;
	}

	public void setBtsCity(String btsCity) {
		this.btsCity = btsCity;
	}

	public String getCustomerBuildingHeightInMeters() {
		return customerBuildingHeightInMeters;
	}

	public void setCustomerBuildingHeightInMeters(String customerBuildingHeightInMeters) {
		this.customerBuildingHeightInMeters = customerBuildingHeightInMeters;
	}

	public String getAdditionalNumberOfLinks() {
		return additionalNumberOfLinks;
	}

	public void setAdditionalNumberOfLinks(String additionalNumberOfLinks) {
		this.additionalNumberOfLinks = additionalNumberOfLinks;
	}

	public String getCableDistanceBetweenMastOrPole() {
		return cableDistanceBetweenMastOrPole;
	}

	public void setCableDistanceBetweenMastOrPole(String cableDistanceBetweenMastOrPole) {
		this.cableDistanceBetweenMastOrPole = cableDistanceBetweenMastOrPole;
	}

	public String getMastHeight() {
		return mastHeight;
	}

	public void setMastHeight(String mastHeight) {
		this.mastHeight = mastHeight;
	}

	public String getAntennaType() {
		return antennaType;
	}

	public void setAntennaType(String antennaType) {
		this.antennaType = antennaType;
	}

	public String getObstructionHeight() {
		return obstructionHeight;
	}

	public void setObstructionHeight(String obstructionHeight) {
		this.obstructionHeight = obstructionHeight;
	}

	public String getObstructionLatitude() {
		return obstructionLatitude;
	}

	public void setObstructionLatitude(String obstructionLatitude) {
		this.obstructionLatitude = obstructionLatitude;
	}

	public String getObstructionLongitude() {
		return obstructionLongitude;
	}

	public void setObstructionLongitude(String obstructionLongitude) {
		this.obstructionLongitude = obstructionLongitude;
	}

}
