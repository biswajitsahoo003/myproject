package com.tcl.dias.servicefulfillmentutils.beans.webex;

public class VoiceServicesDetailsBean {
	
	private String numberType;      
	private String sourceCountry;    
	private String destinationCountry;   
    private String quantity;   
	private String terminationNumber;
	
	public String getNumberType() {
		return numberType;
	}
	
	public void setNumberType(String numberType) {
		this.numberType = numberType;
	}
	
	public String getSourceCountry() {
		return sourceCountry;
	}
	
	public void setSourceCountry(String sourceCountry) {
		this.sourceCountry = sourceCountry;
	}
	
	public String getDestinationCountry() {
		return destinationCountry;
	}
	
	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}
	
	public String getQuantity() {
		return quantity;
	}
	
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	public String getTerminationNumber() {
		return terminationNumber;
	}
	
	public void setTerminationNumber(String terminationNumber) {
		this.terminationNumber = terminationNumber;
	}
	
	

}
