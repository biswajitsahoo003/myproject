package com.tcl.dias.customer.bean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Bean including Commission details to be shown
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CallidusDetailsBean {

    private String opptyId;
    private String circuitId;
    private String obDate;
    private String invoiceDate;
    private String productName;
    private Double billedValueInBaseCurrency;
    private Double incentiveValueInBaseCurrency;
    private String commissionedPercentage;
    private String partnerBaseCurrency;
    private String customerName;


    public String getOpptyId() {
        return opptyId;
    }

    public void setOpptyId(String opptyId) {
        this.opptyId = opptyId;
    }

    public String getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(String circuitId) {
        this.circuitId = circuitId;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getObDate() {
        return obDate;
    }

    public void setObDate(String obDate) {
        this.obDate = obDate;
    }

    public String getPartnerBaseCurrency() {
        return partnerBaseCurrency;
    }

    public void setPartnerBaseCurrency(String partnerBaseCurrency) {
        this.partnerBaseCurrency = partnerBaseCurrency;
    }

    public Double getBilledValueInBaseCurrency() {
        return billedValueInBaseCurrency;
    }

    public void setBilledValueInBaseCurrency(Double billedValueInBaseCurrency) {
        this.billedValueInBaseCurrency = billedValueInBaseCurrency;
    }

    public Double getIncentiveValueInBaseCurrency() {
        return incentiveValueInBaseCurrency;
    }

    public void setIncentiveValueInBaseCurrency(Double incentiveValueInBaseCurrency) {
        this.incentiveValueInBaseCurrency = incentiveValueInBaseCurrency;
    }

    public String getCommissionedPercentage() {
        return commissionedPercentage;
    }

    public void setCommissionedPercentage(String commissionedPercentage) {
        this.commissionedPercentage = commissionedPercentage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public static List<CallidusDetailsBean> fromCallidusPartnerCommissions(List<CallidusPartnerCommisions> callidusPartnerCommisions) {
        return callidusPartnerCommisions.stream().map(callidusPartnerCommision -> {
            CallidusDetailsBean callidusDetailsBean = new CallidusDetailsBean();
            callidusDetailsBean.setOpptyId(callidusPartnerCommision.getOpptyId());
            callidusDetailsBean.setCircuitId(callidusPartnerCommision.getCircuitId());
            callidusDetailsBean.setObDate(callidusPartnerCommision.getObDate());
            callidusDetailsBean.setInvoiceDate(callidusPartnerCommision.getCompDate());
            callidusDetailsBean.setProductName(callidusPartnerCommision.getServiceName());
            callidusDetailsBean.setBilledValueInBaseCurrency(callidusPartnerCommision.getBilledValueInBaseCurrency());
            callidusDetailsBean.setIncentiveValueInBaseCurrency(callidusPartnerCommision.getIncentiveValueInBaseCurrency());
            callidusDetailsBean.setCommissionedPercentage(callidusPartnerCommision.getCommissionedPercentage());
            callidusDetailsBean.setPartnerBaseCurrency(callidusPartnerCommision.getPartnerBaseCurrency());
            callidusDetailsBean.setCustomerName(callidusPartnerCommision.getCustomerName());
            return callidusDetailsBean;
        }).collect(Collectors.toList());
    }
}
