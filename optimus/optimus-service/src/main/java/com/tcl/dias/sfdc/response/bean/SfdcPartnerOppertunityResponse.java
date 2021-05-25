
package com.tcl.dias.sfdc.response.bean;

import java.util.ArrayList;
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
    "validationTaskResponse",
    "taskIDList",
    "status",
    "salesSelectedResponse",
    "reqIDList",
    "quoteRespIdList",
    "productResponse",
    "portFeasibilityFlag",
    "portalTempIdList",
    "opptyIDList",
    "opportunityResponse",
    "onnectCapacityTaskIDList",
    "nplOnnetCapacityTaskIDList",
    "message",
    "feasibilityResResponse",
    "feasibilityReqResponse",
    "errorCode",
    "dftResponseWrappers",
    "backUpPortTaskIDList"
})
/**
 * SfdcPartnerOppertunityResponse.class is used for deal registration SFDC of partner
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SfdcPartnerOppertunityResponse {

    @JsonProperty("validationTaskResponse")
    private Object validationTaskResponse;
    @JsonProperty("taskIDList")
    private Object taskIDList;
    @JsonProperty("status")
    private String status;
    @JsonProperty("salesSelectedResponse")
    private Object salesSelectedResponse;
    @JsonProperty("reqIDList")
    private Object reqIDList;
    @JsonProperty("quoteRespIdList")
    private Object quoteRespIdList;
    @JsonProperty("productResponse")
    private Object productResponse;
    @JsonProperty("portFeasibilityFlag")
    private Object portFeasibilityFlag;
    @JsonProperty("portalTempIdList")
    private Object portalTempIdList;
    @JsonProperty("opptyIDList")
    private List<String> opptyIDList = new ArrayList<String>();
    @JsonProperty("opportunityResponse")
    private List<SfdcPartnerDealRegistrationResponse> opportunityResponse = new ArrayList<SfdcPartnerDealRegistrationResponse>();
    @JsonProperty("onnectCapacityTaskIDList")
    private Object onnectCapacityTaskIDList;
    @JsonProperty("nplOnnetCapacityTaskIDList")
    private Object nplOnnetCapacityTaskIDList;
    @JsonProperty("message")
    private String message;
    @JsonProperty("feasibilityResResponse")
    private Object feasibilityResResponse;
    @JsonProperty("feasibilityReqResponse")
    private Object feasibilityReqResponse;
    @JsonProperty("errorCode")
    private Integer errorCode;
    @JsonProperty("dftResponseWrappers")
    private Object dftResponseWrappers;
    @JsonProperty("backUpPortTaskIDList")
    private Object backUpPortTaskIDList;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("validationTaskResponse")
    public Object getValidationTaskResponse() {
        return validationTaskResponse;
    }

    @JsonProperty("validationTaskResponse")
    public void setValidationTaskResponse(Object validationTaskResponse) {
        this.validationTaskResponse = validationTaskResponse;
    }

    @JsonProperty("taskIDList")
    public Object getTaskIDList() {
        return taskIDList;
    }

    @JsonProperty("taskIDList")
    public void setTaskIDList(Object taskIDList) {
        this.taskIDList = taskIDList;
    }



    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }


    @JsonProperty("salesSelectedResponse")
    public Object getSalesSelectedResponse() {
        return salesSelectedResponse;
    }

    @JsonProperty("salesSelectedResponse")
    public void setSalesSelectedResponse(Object salesSelectedResponse) {
        this.salesSelectedResponse = salesSelectedResponse;
    }

    @JsonProperty("reqIDList")
    public Object getReqIDList() {
        return reqIDList;
    }

    @JsonProperty("reqIDList")
    public void setReqIDList(Object reqIDList) {
        this.reqIDList = reqIDList;
    }

    @JsonProperty("quoteRespIdList")
    public Object getQuoteRespIdList() {
        return quoteRespIdList;
    }

    @JsonProperty("quoteRespIdList")
    public void setQuoteRespIdList(Object quoteRespIdList) {
        this.quoteRespIdList = quoteRespIdList;
    }

    @JsonProperty("productResponse")
    public Object getProductResponse() {
        return productResponse;
    }

    @JsonProperty("productResponse")
    public void setProductResponse(Object productResponse) {
        this.productResponse = productResponse;
    }

    @JsonProperty("portFeasibilityFlag")
    public Object getPortFeasibilityFlag() {
        return portFeasibilityFlag;
    }

    @JsonProperty("portFeasibilityFlag")
    public void setPortFeasibilityFlag(Object portFeasibilityFlag) {
        this.portFeasibilityFlag = portFeasibilityFlag;
    }

    @JsonProperty("portalTempIdList")
    public Object getPortalTempIdList() {
        return portalTempIdList;
    }

    @JsonProperty("portalTempIdList")
    public void setPortalTempIdList(Object portalTempIdList) {
        this.portalTempIdList = portalTempIdList;
    }

    @JsonProperty("opptyIDList")
    public List<String> getOpptyIDList() {
        return opptyIDList;
    }

    @JsonProperty("opptyIDList")
    public void setOpptyIDList(List<String> opptyIDList) {
        this.opptyIDList = opptyIDList;
    }

    @JsonProperty("opportunityResponse")
    public List<SfdcPartnerDealRegistrationResponse> getOpportunityResponse() {
        return opportunityResponse;
    }

    @JsonProperty("opportunityResponse")
    public void setOpportunityResponse(List<SfdcPartnerDealRegistrationResponse> opportunityResponse) {
        this.opportunityResponse = opportunityResponse;
    }

    @JsonProperty("onnectCapacityTaskIDList")
    public Object getOnnectCapacityTaskIDList() {
        return onnectCapacityTaskIDList;
    }

    @JsonProperty("onnectCapacityTaskIDList")
    public void setOnnectCapacityTaskIDList(Object onnectCapacityTaskIDList) {
        this.onnectCapacityTaskIDList = onnectCapacityTaskIDList;
    }
    @JsonProperty("nplOnnetCapacityTaskIDList")
    public Object getNplOnnetCapacityTaskIDList() {
        return nplOnnetCapacityTaskIDList;
    }

    @JsonProperty("nplOnnetCapacityTaskIDList")
    public void setNplOnnetCapacityTaskIDList(Object nplOnnetCapacityTaskIDList) {
        this.nplOnnetCapacityTaskIDList = nplOnnetCapacityTaskIDList;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("feasibilityResResponse")
    public Object getFeasibilityResResponse() {
        return feasibilityResResponse;
    }

    @JsonProperty("feasibilityResResponse")
    public void setFeasibilityResResponse(Object feasibilityResResponse) {
        this.feasibilityResResponse = feasibilityResResponse;
    }

    @JsonProperty("feasibilityReqResponse")
    public Object getFeasibilityReqResponse() {
        return feasibilityReqResponse;
    }

    @JsonProperty("feasibilityReqResponse")
    public void setFeasibilityReqResponse(Object feasibilityReqResponse) {
        this.feasibilityReqResponse = feasibilityReqResponse;
    }

    @JsonProperty("errorCode")
    public Integer getErrorCode() {
        return errorCode;
    }

    @JsonProperty("errorCode")
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    @JsonProperty("dftResponseWrappers")
    public Object getDftResponseWrappers() {
        return dftResponseWrappers;
    }

    @JsonProperty("dftResponseWrappers")
    public void setDftResponseWrappers(Object dftResponseWrappers) {
        this.dftResponseWrappers = dftResponseWrappers;
    }

    @JsonProperty("backUpPortTaskIDList")
    public Object getBackUpPortTaskIDList() {
        return backUpPortTaskIDList;
    }

    @JsonProperty("backUpPortTaskIDList")
    public void setBackUpPortTaskIDList(Object backUpPortTaskIDList) {
        this.backUpPortTaskIDList = backUpPortTaskIDList;
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
