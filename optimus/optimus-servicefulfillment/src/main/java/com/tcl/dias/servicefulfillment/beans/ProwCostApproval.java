package com.tcl.dias.servicefulfillment.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited used for prow request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProwCostApproval extends TaskDetailsBaseBean {

	private String pRowCostApproved;

	private String costApprovalRemarks;

	public String getpRowCostApproved() {
		return pRowCostApproved;
	}

	public void setpRowCostApproved(String pRowCostApproved) {
		this.pRowCostApproved = pRowCostApproved;
	}

	public String getCostApprovalRemarks() {
		return costApprovalRemarks;
	}

	public void setCostApprovalRemarks(String costApprovalRemarks) {
		this.costApprovalRemarks = costApprovalRemarks;
	}

}
