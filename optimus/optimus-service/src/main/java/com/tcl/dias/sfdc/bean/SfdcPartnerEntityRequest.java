package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcPartnerEntityRequest.java class.
 * This class is the parent request class of partner entity create request payload
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"create_request_v1"})
public class SfdcPartnerEntityRequest extends BaseBean {

    @JsonProperty("create_request_v1")
    private SfdcPartnerEntityAccountBean sfdcPartnerEntityAccountBean;

    private String referenceId;

    @JsonProperty("create_request_v1")
    public SfdcPartnerEntityAccountBean getSfdcPartnerEntityAccountBean() {
        return sfdcPartnerEntityAccountBean;
    }

    @JsonProperty("create_request_v1")
    public void setSfdcPartnerEntityAccountBean(SfdcPartnerEntityAccountBean sfdcPartnerEntityAccountBean) {
        this.sfdcPartnerEntityAccountBean = sfdcPartnerEntityAccountBean;
    }

    @JsonIgnore
    @JsonProperty(value = "referenceId")
    public String getReferenceId() {
        return referenceId;
    }
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

}
