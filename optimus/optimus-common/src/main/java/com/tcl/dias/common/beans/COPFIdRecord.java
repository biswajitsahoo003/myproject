
package com.tcl.dias.common.beans;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "COPF_ID__c",
    "System_Generating_COPF_Id__c",
    "MRC__c",
    "NRC__c",
    "CurrencyIsoCode",
    "Site_Location_ID__c",
    "Location__c",
    "Effective_Date_of_Termination__c",
    "Previous_COPF_ID__c"
})
/**
 * 
 * This file contains the COPFIdRecord request bean
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class COPFIdRecord {

    @JsonProperty("COPF_ID__c")
    private String cOPFIDC;
    @JsonProperty("System_Generating_COPF_Id__c")
    private String systemGeneratingCOPFIdC;
    @JsonProperty("MRC__c")
    private String mRCC;
    @JsonProperty("NRC__c")
    private String nRCC;

	@JsonProperty("CurrencyIsoCode")
    private String currencyIsoCode;
	
	@JsonProperty("Site_Location_ID__c")
    private String siteLocationIdC;
	
	@JsonProperty("Previous_COPF_ID__c")
	private String preCopfId;
	
	@JsonProperty("Location__c")
    private String locationC;
	
	@JsonProperty("Effective_Date_of_Termination__c")
	private String effectiveDateOfTerminationC;
	
	@JsonProperty("Site_Location_ID__c")
    public String getSiteLocationIdC() {
		return siteLocationIdC;
	}
	
	@JsonProperty("Site_Location_ID__c")
	public void setSiteLocationIdC(String siteLocationIdC) {
		this.siteLocationIdC = siteLocationIdC;
	}
	
	@JsonProperty("Location__c")
	public String getLocationC() {
		return locationC;
	}
	
	@JsonProperty("Location__c")
	public void setLocationC(String locationC) {
		this.locationC = locationC;
	}
	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("COPF_ID__c")
    public String getCOPFIDC() {
        return cOPFIDC;
    }

    @JsonProperty("COPF_ID__c")
    public void setCOPFIDC(String cOPFIDC) {
        this.cOPFIDC = cOPFIDC;
    }

    @JsonProperty("System_Generating_COPF_Id__c")
    public String getSystemGeneratingCOPFIdC() {
        return systemGeneratingCOPFIdC;
    }

    @JsonProperty("System_Generating_COPF_Id__c")
    public void setSystemGeneratingCOPFIdC(String systemGeneratingCOPFIdC) {
        this.systemGeneratingCOPFIdC = systemGeneratingCOPFIdC;
    }

    @JsonProperty("MRC__c")
    public String getMRCC() {
        return mRCC;
    }

    @JsonProperty("MRC__c")
    public void setMRCC(String mRCC) {
        this.mRCC = mRCC;
    }

    @JsonProperty("CurrencyIsoCode")
    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    @JsonProperty("CurrencyIsoCode")
    public void setCurrencyIsoCode(String currencyIsoCode) {
        this.currencyIsoCode = currencyIsoCode;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
    @JsonProperty("NRC__c")
	public String getnRCC() {
		return nRCC;
	}
    @JsonProperty("NRC__c")
	public void setnRCC(String nRCC) {
		this.nRCC = nRCC;
	}

    @JsonProperty("Effective_Date_of_Termination__c")
	public String getEffectiveDateOfTerminationC() {
		return effectiveDateOfTerminationC;
	}

    @JsonProperty("Effective_Date_of_Termination__c")
	public void setEffectiveDateOfTerminationC(String effectiveDateOfTerminationC) {
		this.effectiveDateOfTerminationC = effectiveDateOfTerminationC;
	}
    @JsonProperty("Previous_COPF_ID__c")
	public String getPreCopfId() {
		return preCopfId;
	}
    @JsonProperty("Previous_COPF_ID__c")
	public void setPreCopfId(String preCopfId) {
		this.preCopfId = preCopfId;
	}    

}
