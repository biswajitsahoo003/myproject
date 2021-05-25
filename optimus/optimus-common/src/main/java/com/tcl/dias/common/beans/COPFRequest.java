
package com.tcl.dias.common.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "create_request_v1"
})
/**
 * 
 * This file contains the COPFRequest bean class for COPF id request to SFDC.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class COPFRequest {

    @JsonProperty("create_request_v1")
    private List<CreateRequestV1> createRequestV1 = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("create_request_v1")
    public List<CreateRequestV1> getCreateRequestV1() {
        return createRequestV1;
    }

    @JsonProperty("create_request_v1")
    public void setCreateRequestV1(List<CreateRequestV1> createRequestV1) {
        this.createRequestV1 = createRequestV1;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
