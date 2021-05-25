
package com.tcl.dias.common.sfdc.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the BCRRequest.java class. used for sfdc
 * 
 * @author Muthuselvi S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "create_request_v1",
    "update_request_v1"
})
public class BCRRequest {

    @JsonProperty("create_request_v1")
    private List<CreateRequestV1> createRequestV1 = null;
    
    @JsonProperty("update_request_v1")
    private List<UpdateRequstV1> updateRequstV1 = null;
    
    

    @JsonProperty("update_request_v1")
    public List<UpdateRequstV1> getUpdateRequstV1() {
		return updateRequstV1;
	}

    @JsonProperty("update_request_v1")
	public void setUpdateRequstV1(List<UpdateRequstV1> updateRequstV1) {
		this.updateRequstV1 = updateRequstV1;
	}

	@JsonProperty("create_request_v1")
    public List<CreateRequestV1> getCreateRequestV1() {
        return createRequestV1;
    }

    @JsonProperty("create_request_v1")
    public void setCreateRequestV1(List<CreateRequestV1> createRequestV1) {
        this.createRequestV1 = createRequestV1;
    }

}
