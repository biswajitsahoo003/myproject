package com.tcl.dias.oms.gsc.beans;

import com.tcl.dias.oms.entity.entities.OrderToLe;

import java.util.Date;

/**
 * Order legal entity which is related to customer and supplier information
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscOrderLeBean {

    private Integer orderLeId;

    private Integer currencyId;

    private Integer customerLegalEntityId;

    private Integer supplierLegalEntityId;

    private Double finalMrc;

    private Double finalNrc;

    private Double finalArc;

    private Double proposedMrc;

    private Double proposedNrc;

    private Double proposedArc;

    private Double totalTcv;

    private String tpsSfdcCopfId;

    private Date startDate;

    private Date endDate;

    private String stage;

    private String paymentCurrency;

    private String termsInMonths;

    private String orderType;

    private Integer erfServiceInventoryParentOrderId;

    private String sourceSystem;

    private String orderCategory;

    private String productFamily;

    /**
     * Get GscOrderLeBean from OrderToLe
     *
     * @param orderToLe
     * @return {@link GscOrderLeBean}
     */
    public static GscOrderLeBean fromOrderToLe(OrderToLe orderToLe) {
        GscOrderLeBean gscOrderLeBean = new GscOrderLeBean();
        gscOrderLeBean.orderLeId = orderToLe.getId();
        gscOrderLeBean.currencyId = orderToLe.getCurrencyId();
        gscOrderLeBean.customerLegalEntityId = orderToLe.getErfCusCustomerLegalEntityId();
        gscOrderLeBean.supplierLegalEntityId = orderToLe.getErfCusSpLegalEntityId();
        gscOrderLeBean.endDate = orderToLe.getEndDate();
        gscOrderLeBean.startDate = orderToLe.getStartDate();
        gscOrderLeBean.finalArc = orderToLe.getFinalArc();
        gscOrderLeBean.finalMrc = orderToLe.getFinalMrc();
        gscOrderLeBean.finalNrc = orderToLe.getFinalNrc();
        gscOrderLeBean.proposedArc = orderToLe.getProposedArc();
        gscOrderLeBean.proposedNrc = orderToLe.getProposedNrc();
        gscOrderLeBean.proposedMrc = orderToLe.getProposedMrc();
        gscOrderLeBean.stage = orderToLe.getStage();
        gscOrderLeBean.totalTcv = orderToLe.getTotalTcv();
        gscOrderLeBean.tpsSfdcCopfId = orderToLe.getTpsSfdcCopfId();
        gscOrderLeBean.paymentCurrency = orderToLe.getCurrencyCode();
        gscOrderLeBean.termsInMonths = orderToLe.getTermInMonths();
        gscOrderLeBean.sourceSystem = orderToLe.getSourceSystem();
        gscOrderLeBean.orderCategory = orderToLe.getOrderCategory();
        gscOrderLeBean.erfServiceInventoryParentOrderId = orderToLe.getErfServiceInventoryParentOrderId();
        gscOrderLeBean.orderType = orderToLe.getOrderType();
        return gscOrderLeBean;
    }

    public Integer getOrderLeId() {
        return orderLeId;
    }

    public void setOrderLeId(Integer orderLeId) {
        this.orderLeId = orderLeId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getCustomerLegalEntityId() {
        return customerLegalEntityId;
    }

    public void setCustomerLegalEntityId(Integer customerLegalEntityId) {
        this.customerLegalEntityId = customerLegalEntityId;
    }

    public Integer getSupplierLegalEntityId() {
        return supplierLegalEntityId;
    }

    public void setSupplierLegalEntityId(Integer supplierLegalEntityId) {
        this.supplierLegalEntityId = supplierLegalEntityId;
    }

    public Double getFinalMrc() {
        return finalMrc;
    }

    public void setFinalMrc(Double finalMrc) {
        this.finalMrc = finalMrc;
    }

    public Double getFinalNrc() {
        return finalNrc;
    }

    public void setFinalNrc(Double finalNrc) {
        this.finalNrc = finalNrc;
    }

    public Double getFinalArc() {
        return finalArc;
    }

    public void setFinalArc(Double finalArc) {
        this.finalArc = finalArc;
    }

    public Double getProposedMrc() {
        return proposedMrc;
    }

    public void setProposedMrc(Double proposedMrc) {
        this.proposedMrc = proposedMrc;
    }

    public Double getProposedNrc() {
        return proposedNrc;
    }

    public void setProposedNrc(Double proposedNrc) {
        this.proposedNrc = proposedNrc;
    }

    public Double getProposedArc() {
        return proposedArc;
    }

    public void setProposedArc(Double proposedArc) {
        this.proposedArc = proposedArc;
    }

    public Double getTotalTcv() {
        return totalTcv;
    }

    public void setTotalTcv(Double totalTcv) {
        this.totalTcv = totalTcv;
    }

    public String getTpsSfdcCopfId() {
        return tpsSfdcCopfId;
    }

    public void setTpsSfdcCopfId(String tpsSfdcCopfId) {
        this.tpsSfdcCopfId = tpsSfdcCopfId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public String getTermsInMonths() {
        return termsInMonths;
    }

    public void setTermsInMonths(String termsInMonths) {
        this.termsInMonths = termsInMonths;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Integer getErfServiceInventoryParentOrderId() {
        return erfServiceInventoryParentOrderId;
    }

    public void setErfServiceInventoryParentOrderId(Integer erfServiceInventoryParentOrderId) {
        this.erfServiceInventoryParentOrderId = erfServiceInventoryParentOrderId;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }

    public String getProductFamily() {
        return productFamily;
    }

    public void setProductFamily(String productFamily) {
        this.productFamily = productFamily;
    }
}
