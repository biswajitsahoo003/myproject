package com.tcl.dias.oms.partner.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcl.dias.common.sfdc.response.bean.DealRegistrationResponseBean;

/**
 * DealRegistrationResponse.
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2019Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "message", "data"})
public class DealRegistrationResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private List<DealRegistrationResponseBean> dealRegistrationResponseBean;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DealRegistrationResponseBean> getDealRegistrationResponseBean() {
        return dealRegistrationResponseBean;
    }

    public void setDealRegistrationResponseBean(List<DealRegistrationResponseBean> dealRegistrationResponseBean) {
        this.dealRegistrationResponseBean = dealRegistrationResponseBean;
    }
}
