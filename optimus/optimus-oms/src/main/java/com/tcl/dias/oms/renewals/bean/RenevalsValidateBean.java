package com.tcl.dias.oms.renewals.bean;

public class RenevalsValidateBean {

	private String serviceId;
	private String leId;
	private String custId;
	private String product;
	private String offeringName;
	private String circuitStatus;
	private String serviceStatus;
	private String isActive;
	private String remarks;
	private String locationId;
	private boolean validLeId;
	private boolean validCustId;
	private boolean validProduct;
	private boolean dataAvailable;
	private boolean crossConnectValid;
	private boolean validCircuit;
	private boolean validisActive;
	private boolean validLocationId;
	private boolean validserviceStatus;
	private boolean gstStatus;
		
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getLeId() {
		return leId;
	}
	public void setLeId(String leId) {
		this.leId = leId;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public boolean isValidLeId() {
		return validLeId;
	}
	public void setValidLeId(boolean validLeId) {
		this.validLeId = validLeId;
	}
	public boolean isValidCustId() {
		return validCustId;
	}
	public void setValidCustId(boolean validCustId) {
		this.validCustId = validCustId;
	}
	public boolean isValidProduct() {
		return validProduct;
	}
	public void setValidProduct(boolean validProduct) {
		this.validProduct = validProduct;
	}
	public boolean isDataAvailable() {
		return dataAvailable;
	}
	public void setDataAvailable(boolean dataAvailable) {
		this.dataAvailable = dataAvailable;
	}
	public boolean isCrossConnectValid() {
		return crossConnectValid;
	}
	public void setCrossConnectValid(boolean crossConnectValid) {
		this.crossConnectValid = crossConnectValid;
	}
	public String getOfferingName() {
		return offeringName;
	}
	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}
	
	
	public String getCircuitStatus() {
		return circuitStatus;
	}
	public void setCircuitStatus(String circuitStatus) {
		this.circuitStatus = circuitStatus;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public boolean isValidCircuit() {
		return validCircuit;
	}
	public void setValidCircuit(boolean validCircuit) {
		this.validCircuit = validCircuit;
	}
	
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public boolean isValidisActive() {
		return validisActive;
	}
	public void setValidisActive(boolean validisActive) {
		this.validisActive = validisActive;
	}
	public boolean isValidLocationId() {
		return validLocationId;
	}
	public void setValidLocationId(boolean validLocationId) {
		this.validLocationId = validLocationId;
	}
	
	public String getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	
	public boolean isValidserviceStatus() {
		return validserviceStatus;
	}
	public void setValidserviceStatus(boolean validserviceStatus) {
		this.validserviceStatus = validserviceStatus;
	}
	
	public boolean isGstStatus() {
		return gstStatus;
	}
	public void setGstStatus(boolean gstStatus) {
		this.gstStatus = gstStatus;
	}
	@Override
	public String toString() {
		return "RenevalsValidateBean [serviceId=" + serviceId + ", leId=" + leId + ", custId=" + custId + ", product="
				+ product + ", offeringName=" + offeringName + ", validLeId=" + validLeId + ", validCustId="
				+ validCustId + ", validProduct=" + validProduct + ", dataAvailable=" + dataAvailable
				+ ", crossConnectValid=" + crossConnectValid + "]";
	}
	
}
