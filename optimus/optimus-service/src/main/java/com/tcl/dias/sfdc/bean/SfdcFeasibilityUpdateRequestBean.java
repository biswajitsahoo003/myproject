package com.tcl.dias.sfdc.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This file contains the SfdcFeasibilityUpdateRequestBean.java class.
 * 
 *
 * @author PARUNACH
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SfdcFeasibilityUpdateRequestBean extends BaseBean {

	@JsonProperty("update_request_v1")
    private List<CreateFeasibilityRequest> updateRequestV1 = null;

	public List<CreateFeasibilityRequest> getUpdateRequestV1() {
		return updateRequestV1;
	}

	public void setUpdateRequestV1(List<CreateFeasibilityRequest> updateRequestV1) {
		this.updateRequestV1 = updateRequestV1;
	}
    
	
}
