
package com.tcl.dias.servicefulfillmentutils.beans.sap;

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
@JsonPropertyOrder({ "Opportunity_Id", "PR_Number", "Status", "Remark" })
public class PRResponse {

	@JsonProperty("Opportunity_Id")
	private Integer opportunityId;
	@JsonProperty("PR_Number")
	private String prNumber;
	
	@JsonProperty("PR_Status")
	private String prStatus;
	
	@JsonProperty("PO_Number")
	private String poNumber;
	@JsonProperty("PO_Status")
	private String poStatus;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("Remark")
	private String remark;

	public Integer getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(Integer opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getPrNumber() {
		return prNumber;
	}

	public void setPrNumber(String prNumber) {
		this.prNumber = prNumber;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getPoStatus() {
		return poStatus;
	}

	public void setPoStatus(String poStatus) {
		this.poStatus = poStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPrStatus() {
		return prStatus;
	}

	public void setPrStatus(String prStatus) {
		this.prStatus = prStatus;
	}

	@Override
	public String toString() {
		return "PRResponse [opportunityId=" + opportunityId + ", prNumber=" + prNumber + ", poNumber=" + poNumber
				+ ", poStatus=" + poStatus + ", status=" + status + ", remark=" + remark + "]";
	}

}
