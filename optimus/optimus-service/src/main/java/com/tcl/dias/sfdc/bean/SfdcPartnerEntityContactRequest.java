package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This class is the parent request class of partner entity create contact request payload
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"create_request_v1"})
public class SfdcPartnerEntityContactRequest extends BaseBean {

    @JsonProperty("create_request_v1")
    private SfdcPartnerEntityContactBean sfdcPartnerEntityContactBean;

    private String referenceId;

    @JsonProperty("create_request_v1")
    public SfdcPartnerEntityContactBean getSfdcPartnerEntityContactBean() {
        return sfdcPartnerEntityContactBean;
    }

    @JsonProperty("create_request_v1")
    public void setSfdcPartnerEntityContactBean(SfdcPartnerEntityContactBean sfdcPartnerEntityContactBean) {
        this.sfdcPartnerEntityContactBean = sfdcPartnerEntityContactBean;
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
