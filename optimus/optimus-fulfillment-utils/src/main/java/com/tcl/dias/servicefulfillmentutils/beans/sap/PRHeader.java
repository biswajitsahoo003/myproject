
package com.tcl.dias.servicefulfillmentutils.beans.sap;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * Bean Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "Circuit_Type", "COPF_Id", "Type_of_Deal", "Opportunity_Category", "CUID", "Received_Date",
		"Customer_Name", "PR_Lines","ServiceId_QuoteRef","Opportunity_Region" })
public class PRHeader {

	@JsonProperty("Circuit_Type")
	private String circuitType;
	@JsonProperty("COPF_Id")
	private String cOPFId;
	@JsonProperty("Type_of_Deal")
	private String typeOfDeal;
	@JsonProperty("Opportunity_Category")
	private String opportunityCategory;
	@JsonProperty("CUID")
	private String cUID;
	@JsonProperty("Received_Date")
	private String receivedDate;
	@JsonProperty("Customer_Name")
	private String customerName;
	@JsonProperty("PR_Lines")
	private List<PRLine> pRLines = null;
	@JsonProperty("ServiceId_QuoteRef")
	private String serviceIdQuoteRef;
	
	@JsonProperty("Opportunity_Region")
	private String opportunityRegion;
	
	@JsonProperty("Opportunity_Region")
	public String getOpportunityRegion() {
		return opportunityRegion;
	}
	@JsonProperty("Opportunity_Region")
	public void setOpportunityRegion(String opportunityRegion) {
		this.opportunityRegion = opportunityRegion;
	}

	@JsonProperty("Circuit_Type")
	public String getCircuitType() {
		return circuitType;
	}

	@JsonProperty("Circuit_Type")
	public void setCircuitType(String circuitType) {
		this.circuitType = circuitType;
	}

	@JsonProperty("COPF_Id")
	public String getCOPFId() {
		return cOPFId;
	}

	@JsonProperty("COPF_Id")
	public void setCOPFId(String cOPFId) {
		this.cOPFId = cOPFId;
	}

	@JsonProperty("Type_of_Deal")
	public String getTypeOfDeal() {
		return typeOfDeal;
	}

	@JsonProperty("Type_of_Deal")
	public void setTypeOfDeal(String typeOfDeal) {
		this.typeOfDeal = typeOfDeal;
	}

	@JsonProperty("Opportunity_Category")
	public String getOpportunityCategory() {
		return opportunityCategory;
	}

	@JsonProperty("Opportunity_Category")
	public void setOpportunityCategory(String opportunityCategory) {
		this.opportunityCategory = opportunityCategory;
	}

	@JsonProperty("CUID")
	public String getCUID() {
		return cUID;
	}

	@JsonProperty("CUID")
	public void setCUID(String cUID) {
		this.cUID = cUID;
	}

	@JsonProperty("Received_Date")
	public String getReceivedDate() {
		return receivedDate;
	}

	@JsonProperty("Received_Date")
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}

	@JsonProperty("Customer_Name")
	public String getCustomerName() {
		return customerName;
	}

	@JsonProperty("Customer_Name")
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@JsonProperty("PR_Lines")
	public List<PRLine> getPRLines() {
		return pRLines;
	}

	@JsonProperty("PR_Lines")
	public void setPRLines(List<PRLine> pRLines) {
		this.pRLines = pRLines;
	}
	public String getServiceIdQuoteRef() {
		return serviceIdQuoteRef;
	}
	public void setServiceIdQuoteRef(String serviceIdQuoteRef) {
		this.serviceIdQuoteRef = serviceIdQuoteRef;
	}
	
	
	
}
