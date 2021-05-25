package com.tcl.dias.l2oworkflowutils.beans;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the CommercialTaskDataBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "discountApprovalLevel",
    "siteArc",
    "siteNrc",
    "siteMrc",
    "siteTcv",
    "approverComments",
    "rejectedComments",
    "pricingResponse"
})
public class CommercialTaskDataBean {

    @JsonProperty("discountApprovalLevel")
    private Integer discountApprovalLevel;
    @JsonProperty("siteArc")
    private Double siteArc;
    @JsonProperty("siteNrc")
    private Double siteNrc;
    @JsonProperty("siteMrc")
    private Double siteMrc;
    @JsonProperty("siteTcv")
    private Double siteTcv;
    @JsonProperty("approverComments")
    private String approverComments;
    @JsonProperty("rejectedComments")
    private String rejectedComments;
    @JsonProperty("pricingResponse")
    private String pricingResponse;

    @JsonProperty("discountApprovalLevel")
    public Integer getDiscountApprovalLevel() {
        return discountApprovalLevel;
    }

    @JsonProperty("discountApprovalLevel")
    public void setDiscountApprovalLevel(Integer discountApprovalLevel) {
        this.discountApprovalLevel = discountApprovalLevel;
    }

    @JsonProperty("siteArc")
    public Double getSiteArc() {
        return siteArc;
    }

    @JsonProperty("siteArc")
    public void setSiteArc(Double siteArc) {
        this.siteArc = siteArc;
    }

    @JsonProperty("siteNrc")
    public Double getSiteNrc() {
        return siteNrc;
    }

    @JsonProperty("siteNrc")
    public void setSiteNrc(Double siteNrc) {
        this.siteNrc = siteNrc;
    }

    @JsonProperty("siteMrc")
    public Double getSiteMrc() {
        return siteMrc;
    }

    @JsonProperty("siteMrc")
    public void setSiteMrc(Double siteMrc) {
        this.siteMrc = siteMrc;
    }

    @JsonProperty("siteTcv")
    public Double getSiteTcv() {
        return siteTcv;
    }

    @JsonProperty("siteTcv")
    public void setSiteTcv(Double siteTcv) {
        this.siteTcv = siteTcv;
    }

    @JsonProperty("approverComments")
    public String getApproverComments() {
        return approverComments;
    }

    @JsonProperty("approverComments")
    public void setApproverComments(String approverComments) {
        this.approverComments = approverComments;
    }

    @JsonProperty("rejectedComments")
    public String getRejectedComments() {
        return rejectedComments;
    }

    @JsonProperty("rejectedComments")
    public void setRejectedComments(String rejectedComments) {
        this.rejectedComments = rejectedComments;
    }

    @JsonProperty("pricingResponse")
    public String getPricingResponse() {
        return pricingResponse;
    }

    @JsonProperty("pricingResponse")
    public void setPricingResponse(String pricingResponse) {
        this.pricingResponse = pricingResponse;
    }}
