package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({

		// information
	    "task_id",
		"feasibility_response_id",
		// response info
		"record_type",
		"vendor_name",
		"lm_type", 
		"provider_name",
		"other_provider_name",
		"provider_reference_number",
		"provider_request_date",
		"provider_response_date", 
		"response_related_to",
		"Predicted_Access_Feasibility", 
		"access_feasibility_category",
		"delivery_timeLine",
		"validity_period", 
		"local_loop_bw",
		"local_loop_interface",
		"last_mile_contract_term",
		"tcl_pop_address", 
		"POP_DIST_KM_SERVICE_MOD",
		"feasibility_remarks",
		
		// commercials
		"lm_otc_modem_charges_offwl",
		"lm_otc_nrc_installation_offwl",	
		"lm_arc_modem_charges_offwl",
		"lm_arc_bw_offwl"

})

/**
 * @author krutsrin
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class FRAOffnetWirelineResponse {
	

	@JsonProperty("task_id")
	private String taskId;
	
	@JsonProperty("feasibility_response_id")
	private String fraId;
	
	private String createdbyOrUpdatedby;
	
	private String createdtimeOrUpdatedtime;

	@JsonProperty("record_type")
	private String recordType;
		
	@JsonProperty("vendor_name")
	private String vendorName;
	
	@JsonProperty("lm_type")
	private String lmType;
	
	@JsonProperty("provider_name")
	private String providerName;
	
	@JsonProperty("other_provider_name")
	private String otherProviderName;
	
	@JsonProperty("provider_reference_number")
	private String providerReferenceNumber;
	
	@JsonProperty("provider_request_date")
	private String providerRequestDate;
	
	@JsonProperty("provider_response_date")
	private String providerResponseDate;
	
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
	

	@JsonProperty("local_loop_bw")
	private String localLoopBw;

	@JsonProperty("local_loop_interface")
	private String localLoopInterface;
	
	
	@JsonProperty("last_mile_contract_term")
	private String lastMileContractTerm;
	
	@JsonProperty("tcl_pop_address")
	private String tclPopAddress;
	
	@JsonProperty("POP_DIST_KM_SERVICE_MOD")
	private String chargableDistanceInKms;
	
	@JsonProperty("feasibility_remarks")
	private String feasibilityRemarks;
	
	@JsonProperty("lm_otc_modem_charges_offwl")
	private String otcModemCharges;
	
	@JsonProperty("lm_otc_nrc_installation_offwl")
	private String otcOrNrcInstallation;
	
	@JsonProperty("lm_arc_modem_charges_offwl")
	private String arcModemCharges;
	
	@JsonProperty("lm_arc_bw_offwl")
	private String arcBw;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getFraId() {
		return fraId;
	}

	public void setFraId(String fraId) {
		this.fraId = fraId;
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

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getLmType() {
		return lmType;
	}

	public void setLmType(String lmType) {
		this.lmType = lmType;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getOtherProviderName() {
		return otherProviderName;
	}

	public void setOtherProviderName(String otherProviderName) {
		this.otherProviderName = otherProviderName;
	}

	public String getProviderReferenceNumber() {
		return providerReferenceNumber;
	}

	public void setProviderReferenceNumber(String providerReferenceNumber) {
		this.providerReferenceNumber = providerReferenceNumber;
	}

	public String getProviderRequestDate() {
		return providerRequestDate;
	}

	public void setProviderRequestDate(String providerRequestDate) {
		this.providerRequestDate = providerRequestDate;
	}

	public String getProviderResponseDate() {
		return providerResponseDate;
	}

	public void setProviderResponseDate(String providerResponseDate) {
		this.providerResponseDate = providerResponseDate;
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

	public String getLastMileContractTerm() {
		return lastMileContractTerm;
	}

	public void setLastMileContractTerm(String lastMileContractTerm) {
		this.lastMileContractTerm = lastMileContractTerm;
	}

	public String getTclPopAddress() {
		return tclPopAddress;
	}

	public void setTclPopAddress(String tclPopAddress) {
		this.tclPopAddress = tclPopAddress;
	}

	public String getChargableDistanceInKms() {
		return chargableDistanceInKms;
	}

	public void setChargableDistanceInKms(String chargableDistanceInKms) {
		this.chargableDistanceInKms = chargableDistanceInKms;
	}

	public String getFeasibilityRemarks() {
		return feasibilityRemarks;
	}

	public void setFeasibilityRemarks(String feasibilityRemarks) {
		this.feasibilityRemarks = feasibilityRemarks;
	}

	public String getOtcModemCharges() {
		return otcModemCharges;
	}

	public void setOtcModemCharges(String otcModemCharges) {
		this.otcModemCharges = otcModemCharges;
	}

	public String getOtcOrNrcInstallation() {
		return otcOrNrcInstallation;
	}

	public void setOtcOrNrcInstallation(String otcOrNrcInstallation) {
		this.otcOrNrcInstallation = otcOrNrcInstallation;
	}

	public String getArcModemCharges() {
		return arcModemCharges;
	}

	public void setArcModemCharges(String arcModemCharges) {
		this.arcModemCharges = arcModemCharges;
	}

	public String getArcBw() {
		return arcBw;
	}

	public void setArcBw(String arcBw) {
		this.arcBw = arcBw;
	}

}
