package com.tcl.dias.oms.npl.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.oms.beans.SolutionDetail;

/**
 * This file contains the NplLinkSitesBean.java class. Bean class
 *
 * @author THAMIZHSELVI PERUMAL
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NplLinksUpdateBean {

    private Integer quoteId;
    private Integer orderId;
    private Integer quoteleId;
    private Integer customerId;
    private List<Integer> orderLeIds;
    private String productName;
    private List<SolutionDetail> solutions;
    private List<NplLinkSitesBean> links;
    private Boolean isSuccess=true;
    @JsonIgnore
    private boolean isManualFeasible;

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getQuoteleId() {
        return quoteleId;
    }

    public void setQuoteleId(Integer quoteleId) {
        this.quoteleId = quoteleId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List<Integer> getOrderLeIds() {
        return orderLeIds;
    }

    public void setOrderLeIds(List<Integer> orderLeIds) {
        this.orderLeIds = orderLeIds;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<SolutionDetail> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<SolutionDetail> solutions) {
        this.solutions = solutions;
    }

    public List<NplLinkSitesBean> getLinks() {
        return links;
    }

    public void setLinks(List<NplLinkSitesBean> links) {
        this.links = links;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public boolean isManualFeasible() {
        return isManualFeasible;
    }

    public void setManualFeasible(boolean manualFeasible) {
        isManualFeasible = manualFeasible;
    }
}
