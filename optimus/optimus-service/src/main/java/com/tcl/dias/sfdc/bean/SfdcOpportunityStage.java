
package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcOpportunityStage.java class.
 * used to connect with sdfc
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "StageName", "Opportunity_ID__c" })
public class SfdcOpportunityStage  extends BaseBean{

	@JsonProperty("StageName")
	private String stageName;
	@JsonProperty("Opportunity_ID__c")
	private String opportunityIDC;

	@JsonProperty("StageName")
	public String getStageName() {
		return stageName;
	}

	@JsonProperty("StageName")
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	@JsonProperty("Opportunity_ID__c")
	public String getOpportunityIDC() {
		return opportunityIDC;
	}

	@JsonProperty("Opportunity_ID__c")
	public void setOpportunityIDC(String opportunityIDC) {
		this.opportunityIDC = opportunityIDC;
	}

}
