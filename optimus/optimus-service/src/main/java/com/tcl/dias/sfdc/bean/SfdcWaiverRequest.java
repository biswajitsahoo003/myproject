package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "create_request_v1" })
public class SfdcWaiverRequest extends BaseBean{

    @JsonProperty("create_request_v1")
    private List<SfdcWaiverRequestBean> createRequestV1;

    public List<SfdcWaiverRequestBean> getCreateRequestV1() {
        return createRequestV1;
    }

    public void setCreateRequestV1(List<SfdcWaiverRequestBean> createRequestV1) {
        this.createRequestV1 = createRequestV1;
    }
}
