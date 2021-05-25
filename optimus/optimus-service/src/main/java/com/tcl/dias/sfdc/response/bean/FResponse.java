package com.tcl.dias.sfdc.response.bean;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the FResponse.java class.
 * 
 *
 * @author PARUNACH
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "attributes",
    "Status",
    "Request_Type__c",
    "City_A_End__c",
    "Continent_A_End__c",
    "Port_Circuit_Capacity__c",
    "Available_Telecom_PRI_Provider_A_End__c",
    "Country_A_End__c",
    "Products_Services__c",
    "Pin_Zip_A_End__c",
    "ADDRESS_LINE1_A_End__c",
    "Address_A_End__c",
    "RecordTypeId",
    "Other_POP_A_End__c",
    "State_A_End__c",
    "Id",
    "Online_ILL_Auto__c",
    "SPECIAL_REQUIREMENTS__c",
    "Cloud_Enablement__c",
    "Interface__c",
    "Site_Contact_Name_A_End__c",
    "Site_Local_Contact_Number_A_End__c"
    
})
public class FResponse {

    @JsonProperty("attributes")
    private Attributes attributes;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Request_Type__c")
    private String requestTypeC;
    @JsonProperty("City_A_End__c")
    private String cityAEndC;
    @JsonProperty("Continent_A_End__c")
    private String continentAEndC;
    @JsonProperty("Port_Circuit_Capacity__c")
    private String portCircuitCapacityC;
    @JsonProperty("Available_Telecom_PRI_Provider_A_End__c")
    private String availableTelecomPRIProviderAEndC;
    @JsonProperty("Country_A_End__c")
    private String countryAEndC;
    @JsonProperty("Products_Services__c")
    private String productsServicesC;
    @JsonProperty("Pin_Zip_A_End__c")
    private String pinZipAEndC;
    @JsonProperty("ADDRESS_LINE1_A_End__c")
    private String aDDRESSLINE1AEndC;
    @JsonProperty("Address_A_End__c")
    private String addressAEndC;
    @JsonProperty("RecordTypeId")
    private String recordTypeId;
    @JsonProperty("Other_POP_A_End__c")
    private String otherPOPAEndC;
    @JsonProperty("State_A_End__c")
    private String stateAEndC;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Online_ILL_Auto__c")
    private String onlineILLAutoC;
    @JsonProperty("SPECIAL_REQUIREMENTS__c")
    private String specialRequirementsC;
    @JsonProperty("Cloud_Enablement__c")
    private String cloudEnablementC;
    @JsonProperty("Interface__c")
    private String interfaceC;
    @JsonProperty("Site_Contact_Name_A_End__c")
    private String siteContactNameAEndC;
    @JsonProperty("Site_Local_Contact_Number_A_End__c")
    private String siteLocalContactNumberAEndC;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("attributes")
    public Attributes getAttributes() {
        return attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("Status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("Request_Type__c")
    public String getRequestTypeC() {
        return requestTypeC;
    }

    @JsonProperty("Request_Type__c")
    public void setRequestTypeC(String requestTypeC) {
        this.requestTypeC = requestTypeC;
    }

    @JsonProperty("City_A_End__c")
    public String getCityAEndC() {
        return cityAEndC;
    }

    @JsonProperty("City_A_End__c")
    public void setCityAEndC(String cityAEndC) {
        this.cityAEndC = cityAEndC;
    }

    @JsonProperty("Continent_A_End__c")
    public String getContinentAEndC() {
        return continentAEndC;
    }

    @JsonProperty("Continent_A_End__c")
    public void setContinentAEndC(String continentAEndC) {
        this.continentAEndC = continentAEndC;
    }

    @JsonProperty("Port_Circuit_Capacity__c")
    public String getPortCircuitCapacityC() {
        return portCircuitCapacityC;
    }

    @JsonProperty("Port_Circuit_Capacity__c")
    public void setPortCircuitCapacityC(String portCircuitCapacityC) {
        this.portCircuitCapacityC = portCircuitCapacityC;
    }

    @JsonProperty("Available_Telecom_PRI_Provider_A_End__c")
    public String getAvailableTelecomPRIProviderAEndC() {
        return availableTelecomPRIProviderAEndC;
    }

    @JsonProperty("Available_Telecom_PRI_Provider_A_End__c")
    public void setAvailableTelecomPRIProviderAEndC(String availableTelecomPRIProviderAEndC) {
        this.availableTelecomPRIProviderAEndC = availableTelecomPRIProviderAEndC;
    }

    @JsonProperty("Country_A_End__c")
    public String getCountryAEndC() {
        return countryAEndC;
    }

    @JsonProperty("Country_A_End__c")
    public void setCountryAEndC(String countryAEndC) {
        this.countryAEndC = countryAEndC;
    }

    @JsonProperty("Products_Services__c")
    public String getProductsServicesC() {
        return productsServicesC;
    }

    @JsonProperty("Products_Services__c")
    public void setProductsServicesC(String productsServicesC) {
        this.productsServicesC = productsServicesC;
    }

    @JsonProperty("Pin_Zip_A_End__c")
    public String getPinZipAEndC() {
        return pinZipAEndC;
    }

    @JsonProperty("Pin_Zip_A_End__c")
    public void setPinZipAEndC(String pinZipAEndC) {
        this.pinZipAEndC = pinZipAEndC;
    }

    @JsonProperty("ADDRESS_LINE1_A_End__c")
    public String getADDRESSLINE1AEndC() {
        return aDDRESSLINE1AEndC;
    }

    @JsonProperty("ADDRESS_LINE1_A_End__c")
    public void setADDRESSLINE1AEndC(String aDDRESSLINE1AEndC) {
        this.aDDRESSLINE1AEndC = aDDRESSLINE1AEndC;
    }

    @JsonProperty("Address_A_End__c")
    public String getAddressAEndC() {
        return addressAEndC;
    }

    @JsonProperty("Address_A_End__c")
    public void setAddressAEndC(String addressAEndC) {
        this.addressAEndC = addressAEndC;
    }

    @JsonProperty("RecordTypeId")
    public String getRecordTypeId() {
        return recordTypeId;
    }

    @JsonProperty("RecordTypeId")
    public void setRecordTypeId(String recordTypeId) {
        this.recordTypeId = recordTypeId;
    }

    @JsonProperty("Other_POP_A_End__c")
    public String getOtherPOPAEndC() {
        return otherPOPAEndC;
    }

    @JsonProperty("Other_POP_A_End__c")
    public void setOtherPOPAEndC(String otherPOPAEndC) {
        this.otherPOPAEndC = otherPOPAEndC;
    }

    @JsonProperty("State_A_End__c")
    public String getStateAEndC() {
        return stateAEndC;
    }

    @JsonProperty("State_A_End__c")
    public void setStateAEndC(String stateAEndC) {
        this.stateAEndC = stateAEndC;
    }

    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("Online_ILL_Auto__c")
    public String getOnlineILLAutoC() {
        return onlineILLAutoC;
    }

    @JsonProperty("Online_ILL_Auto__c")
    public void setOnlineILLAutoC(String onlineILLAutoC) {
        this.onlineILLAutoC = onlineILLAutoC;
    }

	public String getSpecialRequirementsC() {
		return specialRequirementsC;
	}

	public void setSpecialRequirementsC(String specialRequirementsC) {
		this.specialRequirementsC = specialRequirementsC;
	}

	public String getaDDRESSLINE1AEndC() {
		return aDDRESSLINE1AEndC;
	}

	public void setaDDRESSLINE1AEndC(String aDDRESSLINE1AEndC) {
		this.aDDRESSLINE1AEndC = aDDRESSLINE1AEndC;
	}

	public String getCloudEnablementC() {
		return cloudEnablementC;
	}

	public void setCloudEnablementC(String cloudEnablementC) {
		this.cloudEnablementC = cloudEnablementC;
	}

	public String getInterfaceC() {
		return interfaceC;
	}

	public void setInterfaceC(String interfaceC) {
		this.interfaceC = interfaceC;
	}

	public String getSiteContactNameAEndC() {
		return siteContactNameAEndC;
	}

	public void setSiteContactNameAEndC(String siteContactNameAEndC) {
		this.siteContactNameAEndC = siteContactNameAEndC;
	}

	public String getSiteLocalContactNumberAEndC() {
		return siteLocalContactNumberAEndC;
	}

	public void setSiteLocalContactNumberAEndC(String siteLocalContactNumberAEndC) {
		this.siteLocalContactNumberAEndC = siteLocalContactNumberAEndC;
	}

	
}

