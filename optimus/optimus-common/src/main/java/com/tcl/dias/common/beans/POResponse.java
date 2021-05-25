
package com.tcl.dias.common.beans;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the POResponse.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "TCL_Service_Id",
    "Order_Id",
    "PO_Number",
    "PO_Date",
    "PO_Status",
    "Remark"
})
public class POResponse {

    @JsonProperty("TCL_Service_Id")
    private String tCLServiceId;
    @JsonProperty("Order_Id")
    private String orderId;
    @JsonProperty("PO_Number")
    private String pONumber;
    @JsonProperty("PO_Date")
    private Integer pODate;
    @JsonProperty("PO_Status")
    private String pOStatus;
    @JsonProperty("Remark")
    private String remark;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("TCL_Service_Id")
    public String getTCLServiceId() {
        return tCLServiceId;
    }

    @JsonProperty("TCL_Service_Id")
    public void setTCLServiceId(String tCLServiceId) {
        this.tCLServiceId = tCLServiceId;
    }

    @JsonProperty("Order_Id")
    public String getOrderId() {
        return orderId;
    }

    @JsonProperty("Order_Id")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @JsonProperty("PO_Number")
    public String getPONumber() {
        return pONumber;
    }

    @JsonProperty("PO_Number")
    public void setPONumber(String pONumber) {
        this.pONumber = pONumber;
    }

    @JsonProperty("PO_Date")
    public Integer getPODate() {
        return pODate;
    }

    @JsonProperty("PO_Date")
    public void setPODate(Integer pODate) {
        this.pODate = pODate;
    }

    @JsonProperty("PO_Status")
    public String getPOStatus() {
        return pOStatus;
    }

    @JsonProperty("PO_Status")
    public void setPOStatus(String pOStatus) {
        this.pOStatus = pOStatus;
    }

    @JsonProperty("Remark")
    public String getRemark() {
        return remark;
    }

    @JsonProperty("Remark")
    public void setRemark(String remark) {
        this.remark = remark;
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
