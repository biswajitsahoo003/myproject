
package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcStagingOpportunityBean.java class. used to connect
 * with sdfc
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "opportunity", "ProgramManagerName" })
public class SfdcStagingOpportunityBean extends BaseBean {

	@JsonProperty("opportunity")
	private SfdcStaging opportunity;

	@JsonProperty("AccountCUID")
	private String accountCUID;

	@JsonProperty("CustomerContractingEntity")
	private String customerContractingEntity;

	@JsonProperty("ProgramManagerName")
	private String programManagerName;

	@JsonProperty("opportunity")
	public SfdcStaging getOpportunity() {
		return opportunity;
	}

	@JsonProperty("opportunity")
	public void setOpportunity(SfdcStaging opportunity) {
		this.opportunity = opportunity;
	}

	@JsonProperty("PartnerCUID")
	private String partnerCUID;

	public String getProgramManagerName() {
		return programManagerName;
	}

	public void setProgramManagerName(String programManagerName) {
		this.programManagerName = programManagerName;
	}

	public String getAccountCUID() {
		return accountCUID;
	}

	public void setAccountCUID(String accountCUID) {
		this.accountCUID = accountCUID;
	}

	public String getCustomerContractingEntity() {
		return customerContractingEntity;
	}

	public void setCustomerContractingEntity(String customerContractingEntity) {
		this.customerContractingEntity = customerContractingEntity;
	}

	public String getPartnerCUID() {
		return partnerCUID;
	}

	public void setPartnerCUID(String partnerCUID) {
		this.partnerCUID = partnerCUID;
	}
}
