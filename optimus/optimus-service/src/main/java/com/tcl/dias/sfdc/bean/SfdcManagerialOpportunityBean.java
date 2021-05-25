package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcManagerialOpportunityBean.java class. used to
 * connect with sdfc
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

@JsonPropertyOrder({ "opportunity","OwnerName", "AccountCUID", "CustomerContractingEntity", "ProgramManagerName",
		"CommunicationRecipient","ParentOpportunityName" })
public class SfdcManagerialOpportunityBean extends BaseBean {

	@JsonProperty("opportunity")
	private SfdcOpportunityBean opportunity;
	@JsonProperty("AccountCUID")
	private String accountCUID;
	@JsonProperty("CustomerContractingEntity")
	private String customerContractingEntity;
	@JsonProperty("ProgramManagerName")
	private String programManagerName;
	@JsonProperty("CommunicationRecipient")
	private String communicationRecipient;
	@JsonProperty("ParentOpportunityName")
	private String parentOpportunityName;
	@JsonProperty("OwnerName")
	private String ownerName;
	@JsonProperty("PartnerCUID")
	private String partnerCUID;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JsonProperty("CampaignName")
	private String campaignName;
	@JsonProperty("ParentTerminationOpportunityName")
	private String parentTerminationOpportunityName;

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	@JsonProperty("opportunity")
	public SfdcOpportunityBean getOpportunity() {
		return opportunity;
	}

	@JsonProperty("opportunity")
	public void setOpportunity(SfdcOpportunityBean opportunity) {
		this.opportunity = opportunity;
	}

	@JsonProperty("AccountCUID")
	public String getAccountCUID() {
		return accountCUID;
	}

	@JsonProperty("AccountCUID")
	public void setAccountCUID(String accountCUID) {
		this.accountCUID = accountCUID;
	}

	@JsonProperty("CustomerContractingEntity")
	public String getCustomerContractingEntity() {
		return customerContractingEntity;
	}

	@JsonProperty("CustomerContractingEntity")
	public void setCustomerContractingEntity(String customerContractingEntity) {
		this.customerContractingEntity = customerContractingEntity;
	}

	@JsonProperty("ProgramManagerName")
	public String getProgramManagerName() {
		return programManagerName;
	}

	@JsonProperty("ProgramManagerName")
	public void setProgramManagerName(String programManagerName) {
		this.programManagerName = programManagerName;
	}
	
	@JsonProperty("ParentOpportunityName")
	public String getParentOpportunityName() {
		return parentOpportunityName;
	}

	@JsonProperty("ParentOpportunityName")
	public void setParentOpportunityName(String parentOpportunityName) {
		this.parentOpportunityName = parentOpportunityName;
	}


	public String getCommunicationRecipient() {
		return communicationRecipient;
	}

	public void setCommunicationRecipient(String communicationRecipient) {
		this.communicationRecipient = communicationRecipient;
	}

	public String getPartnerCUID() {
		return partnerCUID;
	}

	public void setPartnerCUID(String partnerCUID) {
		this.partnerCUID = partnerCUID;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getParentTerminationOpportunityName() {
		return parentTerminationOpportunityName;
	}

	public void setParentTerminationOpportunityName(String parentTerminationOpportunityName) {
		this.parentTerminationOpportunityName = parentTerminationOpportunityName;
	}
	
	
}
