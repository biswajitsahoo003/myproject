package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateResponseAttribute {
	 
	private String rowId;
	
	public String getId() {
		return rowId;
	}

	public void setId(String id) {
		this.rowId = id;
	}

	@JsonProperty("feasibility_response_id")
	 private String feasibilityResponseId;
	 
	@JsonProperty("record_type")
	 private String recordType;
	
	 @JsonProperty("provider_name")
	 private String providerName;
	 
	 @JsonProperty("Provider_Ref_No")
	 private String Provider_Ref_No;
	 
	 @JsonProperty("Predicted_Access_Feasibility")
	 private String feasibilityStatus;
	 
	 @JsonProperty("OTC_Total")
	 @JsonAlias({"cover_asset", "asset"})
	 private String otcTotal;
	 
	 @JsonProperty("Arc_Total")
	 private String arcTotal;
	 
	 @JsonProperty("TotalCapex")
	 private String totalCapex;
	 
	 
	 
	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String getFeasibilityStatus() {
		return feasibilityStatus;
	}

	public void setFeasibilityStatus(String feasibilityStatus) {
		this.feasibilityStatus = feasibilityStatus;
	}

	public String getOtcTotal() {
		return otcTotal;
	}

	public void setOtcTotal(String otcTotal) {
		this.otcTotal = otcTotal;
	}

	public String getArcTotal() {
		return arcTotal;
	}

	public void setArcTotal(String arcTotal) {
		this.arcTotal = arcTotal;
	}

	public String getTotalCapex() {
		return totalCapex;
	}

	public void setTotalCapex(String totalCapex) {
		this.totalCapex = totalCapex;
	}

	public String getFeasibilityResponseId() {
		return feasibilityResponseId;
	}

	public void setFeasibilityResponseId(String feasibilityResponseId) {
		this.feasibilityResponseId = feasibilityResponseId;
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

	public String getProvider_Ref_No() {
		return Provider_Ref_No;
	}

	public void setProvider_Ref_No(String provider_Ref_No) {
		Provider_Ref_No = provider_Ref_No;
	}


	 

}
