package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class DedicatedNumberBean extends TaskDetailsBaseBean {
    private String numberType;
    private List<String> listOfNumbers;
    private String trunkGroup; 
    private List<VoiceAdvanceEnrichment> configurationDetails;
    private String numberProcurementCompletionDate;
    
	public List<VoiceAdvanceEnrichment> getConfigurationDetails() {
		return configurationDetails;
	}

	public void setConfigurationDetails(List<VoiceAdvanceEnrichment> configurationDetails) {
		this.configurationDetails = configurationDetails;
	}

	public String getNumberType() {
		return numberType;
	}
	
	public void setNumberType(String numberType) {
		this.numberType = numberType;
	}
	
	public List<String> getListOfNumbers() {
		return listOfNumbers;
	}
	
	public void setListOfNumbers(List<String> listOfNumbers) {
		this.listOfNumbers = listOfNumbers;
	}
	
	public String getTrunkGroup() {
		return trunkGroup;
	}
	
	public void setTrunkGroup(String trunkGroup) {
		this.trunkGroup = trunkGroup;
	}

	public String getNumberProcurementCompletionDate() {
		return numberProcurementCompletionDate;
	}

	public void setNumberProcurementCompletionDate(String numberProcurementCompletionDate) {
		this.numberProcurementCompletionDate = numberProcurementCompletionDate;
	}

    
}
