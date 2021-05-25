package com.tcl.dias.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "Contact Mail ID", "Contact Name", "Customer Site Name", "Contact Number", "Account",
		"Requested End Date", "Requested By Date", "Asset", "Requested Start Date", "Short Description", "Product",
		"Detail Description", "Multiple Service ID's Affected", "Service Identifier", "serviceIdDetails" })
public class CommonVariablesBean {

	@JsonProperty("Contact Mail ID")
	private String contactMailID;
	@JsonProperty("Contact Name")
	private String contactName;
	@JsonProperty("Customer Site Name")
	private String customerSiteName;
	@JsonProperty("Contact Number")
	private String contactNumber;
	@JsonProperty("Account")
	private String account;
	@JsonProperty("Requested End Date")
	private String requestedEndDate;
	@JsonProperty("Requested By Date")
	private String requestedByDate;
	@JsonProperty("Asset")
	private String asset;
	@JsonProperty("Requested Start Date")
	private String requestedStartDate;
	@JsonProperty("Short Description")
	private String shortDescription;
	@JsonProperty("Product")
	private String product;
	@JsonProperty("Detail Description")
	private String detailDescription;
	@JsonProperty("Multiple Service ID's Affected")
	private String multipleServiceIDSAffected;
	@JsonProperty("Service Identifier")
	private String serviceIdentifier;
	@JsonProperty("serviceIdDetails")
	private List<ServiceIdDetails> serviceIdDetails;

	@JsonProperty("Contact Mail ID")
	public String getContactMailID() {
		return contactMailID;
	}

	@JsonProperty("Contact Mail ID")
	public void setContactMailID(String contactMailID) {
		this.contactMailID = contactMailID;
	}

	@JsonProperty("Contact Name")
	public String getContactName() {
		return contactName;
	}

	@JsonProperty("Contact Name")
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	@JsonProperty("Customer Site Name")
	public String getCustomerSiteName() {
		return customerSiteName;
	}

	@JsonProperty("Customer Site Name")
	public void setCustomerSiteName(String customerSiteName) {
		this.customerSiteName = customerSiteName;
	}

	@JsonProperty("Contact Number")
	public String getContactNumber() {
		return contactNumber;
	}

	@JsonProperty("Contact Number")
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	@JsonProperty("Account")
	public String getAccount() {
		return account;
	}

	@JsonProperty("Account")
	public void setAccount(String account) {
		this.account = account;
	}

	@JsonProperty("Requested End Date")
	public String getRequestedEndDate() {
		return requestedEndDate;
	}

	@JsonProperty("Requested End Date")
	public void setRequestedEndDate(String requestedEndDate) {
		this.requestedEndDate = requestedEndDate;
	}

	@JsonProperty("Requested By Date")
	public String getRequestedByDate() {
		return requestedByDate;
	}

	@JsonProperty("Requested By Date")
	public void setRequestedByDate(String requestedByDate) {
		this.requestedByDate = requestedByDate;
	}

	@JsonProperty("Asset")
	public String getAsset() {
		return asset;
	}

	@JsonProperty("Asset")
	public void setAsset(String asset) {
		this.asset = asset;
	}

	@JsonProperty("Requested Start Date")
	public String getRequestedStartDate() {
		return requestedStartDate;
	}

	@JsonProperty("Requested Start Date")
	public void setRequestedStartDate(String requestedStartDate) {
		this.requestedStartDate = requestedStartDate;
	}

	@JsonProperty("Short Description")
	public String getShortDescription() {
		return shortDescription;
	}

	@JsonProperty("Short Description")
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	@JsonProperty("Product")
	public String getProduct() {
		return product;
	}

	@JsonProperty("Product")
	public void setProduct(String product) {
		this.product = product;
	}

	@JsonProperty("Detail Description")
	public String getDetailDescription() {
		return detailDescription;
	}

	@JsonProperty("Detail Description")
	public void setDetailDescription(String detailDescription) {
		this.detailDescription = detailDescription;
	}

	@JsonProperty("Multiple Service ID's Affected")
	public String getMultipleServiceIDSAffected() {
		return multipleServiceIDSAffected;
	}

	@JsonProperty("Multiple Service ID's Affected")
	public void setMultipleServiceIDSAffected(String multipleServiceIDSAffected) {
		this.multipleServiceIDSAffected = multipleServiceIDSAffected;
	}

	@JsonProperty("Service Identifier")
	public String getServiceIdentifier() {
		return serviceIdentifier;
	}

	@JsonProperty("Service Identifier")
	public void setServiceIdentifier(String serviceIdentifier) {
		this.serviceIdentifier = serviceIdentifier;
	}

	@JsonProperty("serviceIdDetails")
	public List<ServiceIdDetails> getServiceIdDetails() {
		return serviceIdDetails;
	}

	@JsonProperty("serviceIdDetails")
	public void setServiceIdDetails(List<ServiceIdDetails> serviceIdDetails) {
		this.serviceIdDetails = serviceIdDetails;
	}
	

}
