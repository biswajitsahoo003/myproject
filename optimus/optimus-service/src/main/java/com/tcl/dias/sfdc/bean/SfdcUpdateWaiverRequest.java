package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "update_request_v1" })
public class SfdcUpdateWaiverRequest extends BaseBean{

    @JsonProperty("update_request_v1")
    private List<SfdcWaiverRequestBean> updateRequestV1;

    public List<SfdcWaiverRequestBean> getUpdateRequestV1() {
        return updateRequestV1;
    }

    public void setUpdateRequestV1(List<SfdcWaiverRequestBean> updateRequestV1) {
        this.updateRequestV1 = updateRequestV1;
    }
}