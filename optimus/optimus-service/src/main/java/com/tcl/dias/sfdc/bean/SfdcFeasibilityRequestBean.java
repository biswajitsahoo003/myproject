package com.tcl.dias.sfdc.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This file contains the SfdcFeasibilityRequestBean.java class.
 * 
 *
 * @author PARUNACH
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SfdcFeasibilityRequestBean extends BaseBean {


    @JsonProperty("create_request_v1")
    private List<CreateFeasibilityRequest> createRequestV1 = null;
    
    @JsonProperty("create_request_v1")
    public List<CreateFeasibilityRequest> getCreateRequestV1() {
        return createRequestV1;
    }

    @JsonProperty("create_request_v1")
    public void setCreateRequestV1(List<CreateFeasibilityRequest> createRequestV1) {
        this.createRequestV1 = createRequestV1;
    }

}
