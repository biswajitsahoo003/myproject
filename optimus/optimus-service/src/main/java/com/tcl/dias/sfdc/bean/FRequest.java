package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * This file contains the FRequest.java class.
 * 
 *
 * @author PARUNACH
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "Port_Circuit_Capacity__c",
    "Status",
    "Request_Type__c",
    "Address_Line1_A_End__c",
        "Address_Line2_A_End__c",
    "Address_A_End__c",
    "Other_POP_A_End__c",
    "Pin_Zip_A_End__c",
    "Available_Telecom_PRI_Provider_A_End__c",
    "Country_A_End__c",
    "State_A_End__c",
    "Continent_A_End__c",
    "City_A_End__c",
    "Online_ILL_Auto__c",
    "SPECIAL_REQUIREMENTS__c",
    "Interface__c",
    "ILL_Local_Loop_Capacity__c",
    "ILL_Local_Loop_Capacity_Unit__c",
    "Site_Contact_Name_A_End__c",
    "Site_Local_Contact_Number_A_End__c",
    "id",
        "Address_Line1_B_End__c",
        "Address_Line2_B_End__c",
        "Master_VRF__c",
        "No_of_VRFs__c"

})
public class FRequest {

    @JsonProperty("Port_Circuit_Capacity__c")
    private String portCircuitCapacityC;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Request_Type__c")
    private String requestTypeC;
    @JsonProperty("Address_Line1_A_End__c")
    private String addressLine1AEndC;
    @JsonProperty("Address_Line2_A_End__c")
    private String addressLine2AEndC;
    @JsonProperty("Address_A_End__c")
    private String addressAEndC;
    @JsonProperty("Other_POP_A_End__c")
    private String otherPOPAEndC;
    @JsonProperty("Pin_Zip_A_End__c")
    private String pinZipAEndC;
    @JsonProperty("Available_Telecom_PRI_Provider_A_End__c")
    private String availableTelecomPRIProviderAEndC;
    @JsonProperty("Country_A_End__c")
    private String countryAEndC;
    @JsonProperty("State_A_End__c")
    private String stateAEndC;
    @JsonProperty("Continent_A_End__c")
    private String continentAEndC;
    @JsonProperty("City_A_End__c")
    private String cityAEndC;
    @JsonProperty("Interface__c")
    private String interfaceC;
    @JsonProperty("ILL_Local_Loop_Capacity__c")
    private String illLocalLoopCapacityC;
    @JsonProperty("ILL_Local_Loop_Capacity_Unit__c")
    private String illLocalLoopCapacityUnitC;
    @JsonProperty("Address_Line1_B_End__c")
    private String addressLine1BEndC;
    @JsonProperty("Address_Line2_B_End__c")
    private String addressLine2BEndC;
    @JsonProperty("Address_B_End__c")
    private String addressBEndC;
    @JsonProperty("Other_POP_B_End__c")
    private String otherPOPBEndC;
    @JsonProperty("Pin_Zip_B_End__c")
    private String pinZipBEndC;
    @JsonProperty("Available_Telecom_PRI_Provider_B_End__c")
    private String availableTelecomPRIProviderBEndC;
    @JsonProperty("Country_B_End__c")
    private String countryBEndC;
    @JsonProperty("State_B_End__c")
    private String stateBEndC;
    @JsonProperty("Continent_B_End__c")
    private String continentBEndC;
    @JsonProperty("City_B_End__c")
    private String cityBEndC;
    @JsonProperty("SPECIAL_REQUIREMENTS__c")
    private String specialRequirementsC;
    @JsonProperty("Site_Contact_Name_A_End__c")
    private String siteContactNameAEndC;
    @JsonProperty("Site_Local_Contact_Number_A_End__c")
    private String siteLocalContactNumberAEndC;
    @JsonProperty("id")
    private String id;
    @JsonProperty("Sales_Remarks__c")
    private String salesRemarks;
    @JsonProperty("Type_of_Task__c")
    private String typeOfTaskC;
    @JsonProperty("Interface_A_End__c")
    private String interfaceAEndC;
    @JsonProperty("Interface_B_End__c")
    private String interfaceBEndC;
    @JsonProperty("Local_Loop_Capacity_A_End__c")
    private String localLoopCapacityAEndc;
    
    
    @JsonProperty("Master_VRF__c")
    private String masterVRFC;
    
    @JsonProperty("No_of_VRFs__c")
    private String noOfVRFsC;
    
    
    @JsonProperty("Master_VRF__c")
    public String getMasterVRFC() {
		return masterVRFC;
	}

    @JsonProperty("Master_VRF__c")
	public void setMasterVRFC(String masterVRFC) {
		this.masterVRFC = masterVRFC;
	}

    @JsonProperty("No_of_VRFs__c")
	public String getNoOfVRFsC() {
		return noOfVRFsC;
	}

    @JsonProperty("No_of_VRFs__c")
	public void setNoOfVRFsC(String noOfVRFsC) {
		this.noOfVRFsC = noOfVRFsC;
	}

    
	@JsonProperty("Port_Circuit_Capacity__c")
    public String getPortCircuitCapacityC() {
        return portCircuitCapacityC;
    }

    @JsonProperty("Port_Circuit_Capacity__c")
    public void setPortCircuitCapacityC(String portCircuitCapacityC) {
        this.portCircuitCapacityC = portCircuitCapacityC;
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

    @JsonProperty("Address_Line1_A_End__c")
    public String getAddressLine1AEndC() {
        return addressLine1AEndC;
    }

    @JsonProperty("Address_Line1_A_End__c")
    public void setAddressLine1AEndC(String addressLine1AEndC) {
        this.addressLine1AEndC = addressLine1AEndC;
    }

    @JsonProperty("Address_Line2_A_End__c")
    public String getAddressLine2AEndC() {
        return addressLine2AEndC;
    }

    @JsonProperty("Address_Line2_A_End__c")
    public void setAddressLine2AEndC(String addressLine2AEndC) {
        this.addressLine2AEndC = addressLine2AEndC;
    }


    @JsonProperty("Address_A_End__c")
    public String getAddressAEndC() {
        return addressAEndC;
    }

    @JsonProperty("Address_A_End__c")
    public void setAddressAEndC(String addressAEndC) {
        this.addressAEndC = addressAEndC;
    }

    @JsonProperty("Other_POP_A_End__c")
    public String getOtherPOPAEndC() {
        return otherPOPAEndC;
    }

    @JsonProperty("Other_POP_A_End__c")
    public void setOtherPOPAEndC(String otherPOPAEndC) {
        this.otherPOPAEndC = otherPOPAEndC;
    }

    @JsonProperty("Pin_Zip_A_End__c")
    public String getPinZipAEndC() {
        return pinZipAEndC;
    }

    @JsonProperty("Pin_Zip_A_End__c")
    public void setPinZipAEndC(String pinZipAEndC) {
        this.pinZipAEndC = pinZipAEndC;
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

    @JsonProperty("State_A_End__c")
    public String getStateAEndC() {
        return stateAEndC;
    }

    @JsonProperty("State_A_End__c")
    public void setStateAEndC(String stateAEndC) {
        this.stateAEndC = stateAEndC;
    }

    @JsonProperty("Continent_A_End__c")
    public String getContinentAEndC() {
        return continentAEndC;
    }

    @JsonProperty("Continent_A_End__c")
    public void setContinentAEndC(String continentAEndC) {
        this.continentAEndC = continentAEndC;
    }

    @JsonProperty("City_A_End__c")
    public String getCityAEndC() {
        return cityAEndC;
    }

    @JsonProperty("City_A_End__c")
    public void setCityAEndC(String cityAEndC) {
        this.cityAEndC = cityAEndC;
    }

	/**
	 * @return the addressLine1BEndC
	 */
	public String getAddressLine1BEndC() {
		return addressLine1BEndC;
	}

	/**
	 * @param addressLine1BEndC the addressLine1BEndC to set
	 */
	public void setAddressLine1BEndC(String addressLine1BEndC) {
		this.addressLine1BEndC = addressLine1BEndC;
	}

    @JsonProperty("Address_Line2_B_End__c")
    public String getAddressLine2BEndC() {
        return addressLine2BEndC;
    }

    @JsonProperty("Address_Line2_B_End__c")
    public void setAddressLine2BEndC(String addressLine2BEndC) {
        this.addressLine2BEndC = addressLine2BEndC;
    }

	/**
	 * @return the addressBEndC
	 */
	public String getAddressBEndC() {
		return addressBEndC;
	}

	/**
	 * @param addressBEndC the addressBEndC to set
	 */
	public void setAddressBEndC(String addressBEndC) {
		this.addressBEndC = addressBEndC;
	}

	/**
	 * @return the otherPOPBEndC
	 */
	public String getOtherPOPBEndC() {
		return otherPOPBEndC;
	}

	/**
	 * @param otherPOPBEndC the otherPOPBEndC to set
	 */
	public void setOtherPOPBEndC(String otherPOPBEndC) {
		this.otherPOPBEndC = otherPOPBEndC;
	}

	/**
	 * @return the pinZipBEndC
	 */
	public String getPinZipBEndC() {
		return pinZipBEndC;
	}

	/**
	 * @param pinZipBEndC the pinZipBEndC to set
	 */
	public void setPinZipBEndC(String pinZipBEndC) {
		this.pinZipBEndC = pinZipBEndC;
	}

	/**
	 * @return the availableTelecomPRIProviderBEndC
	 */
	public String getAvailableTelecomPRIProviderBEndC() {
		return availableTelecomPRIProviderBEndC;
	}

	/**
	 * @param availableTelecomPRIProviderBEndC the availableTelecomPRIProviderBEndC to set
	 */
	public void setAvailableTelecomPRIProviderBEndC(String availableTelecomPRIProviderBEndC) {
		this.availableTelecomPRIProviderBEndC = availableTelecomPRIProviderBEndC;
	}

	/**
	 * @return the countryBEndC
	 */
	public String getCountryBEndC() {
		return countryBEndC;
	}

	/**
	 * @param countryBEndC the countryBEndC to set
	 */
	public void setCountryBEndC(String countryBEndC) {
		this.countryBEndC = countryBEndC;
	}

	/**
	 * @return the stateBEndC
	 */
	public String getStateBEndC() {
		return stateBEndC;
	}

	/**
	 * @param stateBEndC the stateBEndC to set
	 */
	public void setStateBEndC(String stateBEndC) {
		this.stateBEndC = stateBEndC;
	}

	/**
	 * @return the continentBEndC
	 */
	public String getContinentBEndC() {
		return continentBEndC;
	}

	/**
	 * @param continentBEndC the continentBEndC to set
	 */
	public void setContinentBEndC(String continentBEndC) {
		this.continentBEndC = continentBEndC;
	}

	/**
	 * @return the cityBEndC
	 */
	public String getCityBEndC() {
		return cityBEndC;
	}

	/**
	 * @param cityBEndC the cityBEndC to set
	 */
	public void setCityBEndC(String cityBEndC) {
		this.cityBEndC = cityBEndC;
	}

	public String getSpecialRequirementsC() {
		return specialRequirementsC;
	}

	public void setSpecialRequirementsC(String specialRequirementsC) {
		this.specialRequirementsC = specialRequirementsC;
	}

	public String getInterfaceC() {
		return interfaceC;
	}

	public void setInterfaceC(String interfaceC) {
		this.interfaceC = interfaceC;
	}

	public String getIllLocalLoopCapacityC() {
		return illLocalLoopCapacityC;
	}

	public void setIllLocalLoopCapacityC(String illLocalLoopCapacityC) {
		this.illLocalLoopCapacityC = illLocalLoopCapacityC;
	}

	public String getIllLocalLoopCapacityUnitC() {
		return illLocalLoopCapacityUnitC;
	}

	public void setIllLocalLoopCapacityUnitC(String illLocalLoopCapacityUnitC) {
		this.illLocalLoopCapacityUnitC = illLocalLoopCapacityUnitC;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public String getSalesRemarks() {
        return salesRemarks;
    }

    public void setSalesRemarks(String salesRemarks) {
        this.salesRemarks = salesRemarks;
    }

	public String getTypeOfTaskC() {
		return typeOfTaskC;
	}

	public void setTypeOfTaskC(String typeOfTaskC) {
		this.typeOfTaskC = typeOfTaskC;
	}

	public String getInterfaceAEndC() {
		return interfaceAEndC;
	}

	public void setInterfaceAEndC(String interfaceAEndC) {
		this.interfaceAEndC = interfaceAEndC;
	}

	public String getInterfaceBEndC() {
		return interfaceBEndC;
	}

	public void setInterfaceBEndC(String interfaceBEndC) {
		this.interfaceBEndC = interfaceBEndC;
	}

	public String getLocalLoopCapacityAEndc() {
		return localLoopCapacityAEndc;
	}

	public void setLocalLoopCapacityAEndc(String localLoopCapacityAEndc) {
		this.localLoopCapacityAEndc = localLoopCapacityAEndc;
	}
	
}

